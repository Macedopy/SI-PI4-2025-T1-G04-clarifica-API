package construction.cost;

import construction.core.Phase;
import construction.core.Construction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class CostDashboardService {

    @Inject
    MaterialPurchaseService materialService;

    @Inject
    ToolPurchaseService toolService;

    @Inject
    MachineryRecordService machineryService;

    @Inject
    MachineryFuelLogService fuelService;

    @Inject
    MachineryMaintenanceLogService maintenanceService;

    @Inject
    TeamMemberWorkLogService workLogService;

    public CostDashboardDTO getDashboardByConstruction(String constructionId) {
        Construction construction = Construction.findById(constructionId);
        if (construction == null) {
            throw new NotFoundException("Construction not found: " + constructionId);
        }

        // Buscar todas as fases da construção
        List<construction.core.Phase> phases = construction.getPhases();

        double totalMaterials = 0;
        double totalTools = 0;
        double totalMachineryRental = 0;
        double totalFuel = 0;
        double totalMaintenance = 0;
        double totalLabor = 0;
        double totalMachineryValue = 0;

        List<PhaseCostDTO> phaseCosts = new ArrayList<>();

        for (construction.core.Phase phase : phases) {
            String phaseId = phase.getId();

            double materials = materialService.getTotalSpentByPhase(phaseId);
            double tools = toolService.getTotalSpentByPhase(phaseId);
            double machineryRental = machineryService.getTotalRentalCostByPhase(phaseId);
            double fuel = fuelService.getTotalFuelCostByPhase(phaseId);
            double maintenance = maintenanceService.getTotalMaintenanceCostByPhase(phaseId);
            double labor = workLogService.getTotalLaborCostByPhase(phaseId);

            // Somar valor atual dos maquinários desta fase
            List<MachineryRecord> machines = machineryService.listByPhase(phaseId);
            double phaseMachineryValue = machines.stream().mapToDouble(MachineryRecord::getCurrentValue).sum();

            double phaseTotal = materials + tools + machineryRental + fuel + maintenance + labor;

            totalMaterials += materials;
            totalTools += tools;
            totalMachineryRental += machineryRental;
            totalFuel += fuel;
            totalMaintenance += maintenance;
            totalLabor += labor;
            totalMachineryValue += phaseMachineryValue;

            phaseCosts.add(new PhaseCostDTO(
                    phaseId,
                    phase.getName(),
                    phaseTotal,
                    materials,
                    tools,
                    machineryRental,
                    fuel,
                    maintenance,
                    labor,
                    phaseMachineryValue
            ));
        }

        double grandTotal = totalMaterials + totalTools + totalMachineryRental + totalFuel + totalMaintenance + totalLabor;

        CostDashboardDTO dto = new CostDashboardDTO();
        dto.setConstructionId(constructionId);
        dto.setConstructionName(construction.getName());
        dto.setGrandTotal(grandTotal);
        dto.setTotalMaterials(totalMaterials);
        dto.setTotalTools(totalTools);
        dto.setTotalMachineryRental(totalMachineryRental);
        dto.setTotalFuel(totalFuel);
        dto.setTotalMaintenance(totalMaintenance);
        dto.setTotalLabor(totalLabor);
        dto.setTotalMachineryValue(totalMachineryValue);
        dto.setPhaseCosts(phaseCosts);

        return dto;
    }

    public static class PhaseCostDTO {
        private String phaseId;
        private String phaseName;
        private double total;
        private double materials;
        private double tools;
        private double machineryRental;
        private double fuel;
        private double maintenance;
        private double labor;
        private double machineryValue;

        public PhaseCostDTO() {}

        public PhaseCostDTO(String phaseId, String phaseName, double total, double materials,
                            double tools, double machineryRental, double fuel, double maintenance,
                            double labor, double machineryValue) {
            this.phaseId = phaseId;
            this.phaseName = phaseName;
            this.total = total;
            this.materials = materials;
            this.tools = tools;
            this.machineryRental = machineryRental;
            this.fuel = fuel;
            this.maintenance = maintenance;
            this.labor = labor;
            this.machineryValue = machineryValue;
        }

        // Getters and Setters
        public String getPhaseId() { return phaseId; }
        public void setPhaseId(String phaseId) { this.phaseId = phaseId; }

        public String getPhaseName() { return phaseName; }
        public void setPhaseName(String phaseName) { this.phaseName = phaseName; }

        public double getTotal() { return total; }
        public void setTotal(double total) { this.total = total; }

        public double getMaterials() { return materials; }
        public void setMaterials(double materials) { this.materials = materials; }

        public double getTools() { return tools; }
        public void setTools(double tools) { this.tools = tools; }

        public double getMachineryRental() { return machineryRental; }
        public void setMachineryRental(double machineryRental) { this.machineryRental = machineryRental; }

        public double getFuel() { return fuel; }
        public void setFuel(double fuel) { this.fuel = fuel; }

        public double getMaintenance() { return maintenance; }
        public void setMaintenance(double maintenance) { this.maintenance = maintenance; }

        public double getLabor() { return labor; }
        public void setLabor(double labor) { this.labor = labor; }

        public double getMachineryValue() { return machineryValue; }
        public void setMachineryValue(double machineryValue) { this.machineryValue = machineryValue; }
    }
}
