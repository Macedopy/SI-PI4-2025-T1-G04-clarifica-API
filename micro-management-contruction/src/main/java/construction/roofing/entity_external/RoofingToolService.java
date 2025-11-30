package construction.roofing.entity_external;

import construction.components.tools.ToolCondition;
import construction.components.tools.ToolDTO;
import construction.roofing.Roofing;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class RoofingToolService {

    @Inject RoofingToolRepository repository;

    private RoofingTool mapToEntity(ToolDTO dto, String phaseId, Roofing roofing) {
        RoofingTool entity = new RoofingTool();
        
        entity.setId(UUID.randomUUID().toString());
        entity.setPhaseId(phaseId);
        entity.setRoofing(roofing);

        entity.setName(
            dto.getName() != null && !dto.getName().trim().isEmpty()
                ? dto.getName().trim()
                : "Ferramenta sem nome (" + UUID.randomUUID().toString().substring(0, 8) + ")"
        );

        entity.setCategory(
            dto.getCategory() != null && !dto.getCategory().trim().isEmpty()
                ? dto.getCategory().trim()
                : "Manual"
        );

        Integer totalQty = dto.getTotalQuantity();
        entity.setTotalQuantity(totalQty != null && totalQty >= 1 ? totalQty : 1);

        Integer inUse = dto.getInUse();
        entity.setInUse(inUse != null && inUse >= 0 ? inUse : 0);

        Integer inMaintenance = dto.getInMaintenance();
        entity.setInMaintenance(inMaintenance != null && inMaintenance >= 0 ? inMaintenance : 0);

        try {
            String cond = dto.getCondition() != null ? dto.getCondition().toUpperCase() : "GOOD";
            entity.setCondition(ToolCondition.valueOf(cond));
        } catch (Exception e) {
            entity.setCondition(ToolCondition.GOOD);
        }

        entity.setNotes(dto.getNotes());

        return entity;
    }

    @Transactional
    public void saveAll(List<ToolDTO> dtos, String phaseId, Roofing roofing) {
        if (dtos == null || dtos.isEmpty()) return;

        List<RoofingTool> entities = dtos.stream()
            .map(dto -> mapToEntity(dto, phaseId, roofing))
            .collect(Collectors.toList());

        RoofingTool.persist(entities);
    }
}
