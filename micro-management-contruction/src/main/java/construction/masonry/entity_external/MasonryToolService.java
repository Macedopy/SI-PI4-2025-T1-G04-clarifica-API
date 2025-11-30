package construction.masonry.entity_external;

import construction.components.tools.ToolCondition;
import construction.components.tools.ToolDTO;
import construction.masonry.Masonry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class MasonryToolService {

    @Inject MasonryToolRepository repository;

    private MasonryTool mapToEntity(ToolDTO dto, String phaseId, Masonry masonry) {
        MasonryTool entity = new MasonryTool();
        
        // ✅ Gera novo ID sempre
        entity.setId(UUID.randomUUID().toString());
        
        entity.setPhaseId(phaseId);
        entity.setMasonry(masonry);

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
    public void saveAll(List<ToolDTO> dtos, String phaseId, Masonry masonry) {
        if (dtos == null || dtos.isEmpty()) return;

        List<MasonryTool> entities = dtos.stream()
            .map(dto -> mapToEntity(dto, phaseId, masonry))
            .collect(Collectors.toList());

        MasonryTool.persist(entities);
    }
}
