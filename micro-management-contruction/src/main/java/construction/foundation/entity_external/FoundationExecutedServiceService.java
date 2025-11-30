package construction.foundation.entity_external;

import construction.components.executed_services.ExecutedServiceDTO;
import construction.components.executed_services.ExecutedServiceStatus;
import construction.foundation.Foundation;
import construction.foundation.FoundationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors; // Adicionado para o stream

@ApplicationScoped
public class FoundationExecutedServiceService {
    
    @Inject
    FoundationExecutedServiceRepository repository;

    @Inject
    FoundationRepository foundationRepository;

    private FoundationExecutedService mapToEntity(ExecutedServiceDTO dto, String phaseId, Foundation foundation) {
        FoundationExecutedService entity = new FoundationExecutedService();

        entity.setId(UUID.randomUUID().toString());

        entity.setFoundation(foundation);
        entity.setPhaseId(phaseId);

        // FIX DEFINITIVO: nunca mais null ou vazio
        entity.setName(
            dto.getName() != null && !dto.getName().trim().isEmpty() 
                ? dto.getName().trim() 
                : "Serviço sem nome (" + UUID.randomUUID().toString().substring(0, 8) + ")"
        );

        entity.setTeam(
            dto.getTeam() != null && !dto.getTeam().trim().isEmpty()
                ? dto.getTeam().trim()
                : "Equipe Principal"
        );

        entity.setPlannedHours(
            dto.getPlannedHours() > 0 ? dto.getPlannedHours() : 8.0
        );

        entity.setExecutedHours(dto.getExecutedHours() >= 0 ? dto.getExecutedHours() : 0.0);

        // Correção do status: garante que venha em maiúsculo e com nome válido
        String statusStr = dto.getStatus() != null ? dto.getStatus().toUpperCase() : "PLANEJADO";
        statusStr = statusStr.replace("ANDAMENTO", "EM_ANDAMENTO");
        try {
            entity.setStatus(ExecutedServiceStatus.valueOf(statusStr));
        } catch (IllegalArgumentException e) {
            entity.setStatus(ExecutedServiceStatus.PLANEJADO);
        }

        entity.setNotes(dto.getNotes());

        // Progresso calculado corretamente
        if (entity.getPlannedHours() > 0) {
            int progress = (int) Math.round((entity.getExecutedHours() / entity.getPlannedHours()) * 100);
            entity.setProgress(Math.min(100, Math.max(0, progress)));
        } else {
            entity.setProgress(0);
        }

        return entity;
    }

    @Transactional
    public void saveAll(List<ExecutedServiceDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }

        Foundation foundation = foundationRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Foundation não encontrada com ID: " + phaseId));

        List<FoundationExecutedService> entities = dtos.stream()
            // Passa os dados de contexto e a DTO para mapeamento
            .map(dto -> mapToEntity(dto, phaseId, foundation))
            .collect(Collectors.toList());
        
        // Panache fará o UPSET (update ou insert) em lote.
        FoundationExecutedService.persist(entities);
    }
}
