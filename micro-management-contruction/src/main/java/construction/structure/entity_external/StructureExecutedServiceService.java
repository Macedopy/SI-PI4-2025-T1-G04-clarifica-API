package construction.structure.entity_external;

import construction.components.executed_services.ExecutedServiceDTO;
import construction.components.executed_services.ExecutedServiceStatus;
import construction.structure.Structure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class StructureExecutedServiceService {
    
    @Inject
    StructureExecutedServiceRepository repository;

    private StructureExecutedService mapToEntity(ExecutedServiceDTO dto, String phaseId, Structure structure) {
        StructureExecutedService entity = new StructureExecutedService();

        // ✅ Gera novo ID sempre
        entity.setId(UUID.randomUUID().toString());
        
        entity.setStructure(structure);
        entity.setPhaseId(phaseId);

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

        entity.setPlannedHours(dto.getPlannedHours() > 0 ? dto.getPlannedHours() : 8.0);
        entity.setExecutedHours(dto.getExecutedHours() >= 0 ? dto.getExecutedHours() : 0.0);

        String statusStr = dto.getStatus() != null ? dto.getStatus().toUpperCase() : "PLANEJADO";
        statusStr = statusStr.replace("ANDAMENTO", "EM_ANDAMENTO");
        try {
            entity.setStatus(ExecutedServiceStatus.valueOf(statusStr));
        } catch (IllegalArgumentException e) {
            entity.setStatus(ExecutedServiceStatus.PLANEJADO);
        }

        entity.setNotes(dto.getNotes());

        if (entity.getPlannedHours() > 0) {
            int progress = (int) Math.round((entity.getExecutedHours() / entity.getPlannedHours()) * 100);
            entity.setProgress(Math.min(100, Math.max(0, progress)));
        } else {
            entity.setProgress(0);
        }

        return entity;
    }

    @Transactional
    public void saveAll(List<ExecutedServiceDTO> dtos, String phaseId, Structure structure) {
        if (dtos == null || dtos.isEmpty()) return;

        List<StructureExecutedService> entities = dtos.stream()
            .map(dto -> mapToEntity(dto, phaseId, structure))
            .collect(Collectors.toList());
        
        StructureExecutedService.persist(entities);
    }
}
