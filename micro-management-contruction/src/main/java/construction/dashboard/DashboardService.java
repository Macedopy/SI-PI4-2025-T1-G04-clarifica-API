package construction.dashboard;

import construction.core.Construction;
import construction.core.Phase;
import construction.components.executed_services.ExecutedService;
import construction.components.executed_services.ExecutedServiceStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class DashboardService {

    public DashboardDTO calculateDashboard(String constructionId) {
        Construction construction = Construction.findById(constructionId);
        if (construction == null) {
            throw new NotFoundException("Construction not found: " + constructionId);
        }

        // Buscar serviços de todas as fases com join para evitar LazyInitializationException
        List<ExecutedService> allServices = ExecutedService.find(
                "phase.construction.id = ?1", constructionId
        ).list();

        // Agrupar serviços por fase
        Map<String, List<ExecutedService>> servicesByPhase = new HashMap<>();
        for (ExecutedService service : allServices) {
            String phaseId = service.getPhase().getId();
            servicesByPhase.computeIfAbsent(phaseId, k -> new ArrayList<>()).add(service);
        }

        double totalPlanned = 0;
        double totalExecuted = 0;
        int totalServices = 0;
        int completedServices = 0;
        int inProgressServices = 0;
        int plannedServices = 0;

        Map<String, Integer> phasesProgress = new HashMap<>();
        List<DashboardDTO.PhaseProgressDTO> phaseDetails = new ArrayList<>();

        for (Phase phase : construction.getPhases()) {
            List<ExecutedService> phaseServices = servicesByPhase.getOrDefault(phase.getId(), new ArrayList<>());

            double phasePlanned = 0;
            double phaseExecuted = 0;
            double weightedProgressSum = 0;
            double totalWeight = 0;
            int phaseCompleted = 0;
            int phaseInProgress = 0;
            int phasePlannedCount = 0;

            for (ExecutedService service : phaseServices) {
                totalServices++;
                phasePlanned += service.getPlannedHours();
                phaseExecuted += service.getExecutedHours();

                int serviceProgress;
                if (service.getStatus() == ExecutedServiceStatus.COMPLETED) {
                    // Serviço concluído = 100% independente das horas
                    serviceProgress = 100;
                    completedServices++;
                    phaseCompleted++;
                } else {
                    // Usar o campo progress do serviço (já calculado baseado em horas executadas/planejadas)
                    serviceProgress = service.getProgress();

                    if (service.getStatus() == ExecutedServiceStatus.PLANNED) {
                        plannedServices++;
                        phasePlannedCount++;
                    } else {
                        // STARTED ou IN_PROGRESS
                        inProgressServices++;
                        phaseInProgress++;
                    }
                }

                // Ponderar pelo plannedHours (serviços com mais horas planejadas pesam mais)
                double weight = service.getPlannedHours() > 0 ? service.getPlannedHours() : 1.0;
                weightedProgressSum += serviceProgress * weight;
                totalWeight += weight;
            }

            int phaseProgress = 0;
            if (totalWeight > 0) {
                phaseProgress = (int) Math.round(weightedProgressSum / totalWeight);
                phaseProgress = Math.min(100, Math.max(0, phaseProgress));
            }

            phasesProgress.put(phase.getName(), phaseProgress);

            totalPlanned += phasePlanned;
            totalExecuted += phaseExecuted;

            phaseDetails.add(new DashboardDTO.PhaseProgressDTO(
                    phase.getId(),
                    phase.getName(),
                    phase.getContractor(),
                    phaseProgress,
                    phaseServices.size(),
                    phaseCompleted,
                    phasePlanned,
                    phaseExecuted
            ));
        }

        // Progresso geral = média ponderada do progresso das fases (peso = plannedHours da fase)
        int overallProgress = 0;
        double overallWeightedSum = 0;
        double overallTotalWeight = 0;

        for (Phase phase : construction.getPhases()) {
            List<ExecutedService> phaseServices = servicesByPhase.getOrDefault(phase.getId(), new ArrayList<>());
            double phaseWeight = phaseServices.stream()
                    .mapToDouble(ExecutedService::getPlannedHours)
                    .sum();
            if (phaseWeight == 0) phaseWeight = 1.0; // fase sem serviços ainda conta como peso mínimo

            int phaseProgress = phasesProgress.getOrDefault(phase.getName(), 0);
            overallWeightedSum += phaseProgress * phaseWeight;
            overallTotalWeight += phaseWeight;
        }

        if (overallTotalWeight > 0) {
            overallProgress = (int) Math.round(overallWeightedSum / overallTotalWeight);
            overallProgress = Math.min(100, Math.max(0, overallProgress));
        }

        DashboardDTO dto = new DashboardDTO(
                constructionId,
                totalPlanned,
                totalExecuted,
                overallProgress,
                phasesProgress
        );
        dto.setConstructionName(construction.getName());
        dto.setConstructionStatus(construction.getStatus());
        dto.setPhases(phaseDetails);
        dto.setTotalServices(totalServices);
        dto.setCompletedServices(completedServices);
        dto.setInProgressServices(inProgressServices);
        dto.setPlannedServices(plannedServices);

        return dto;
    }
}
