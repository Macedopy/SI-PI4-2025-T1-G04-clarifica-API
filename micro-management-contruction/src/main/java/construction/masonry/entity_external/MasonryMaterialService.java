package construction.masonry.entity_external;

import construction.components.used_material.MaterialCategory;
import construction.components.used_material.MaterialDTO;
import construction.components.used_material.MaterialUnit;
import construction.masonry.Masonry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class MasonryMaterialService {

    @Inject MasonryMaterialRepository repository;

    private MasonryMaterial mapToEntity(MaterialDTO dto, String phaseId, Masonry masonry) {
        MasonryMaterial entity = new MasonryMaterial();

        // ✅ Gera novo ID sempre
        entity.setId(UUID.randomUUID().toString());
        
        entity.setPhaseId(phaseId);
        entity.setMasonry(masonry);

        entity.setName(
            dto.getName() != null && !dto.getName().trim().isEmpty()
                ? dto.getName().trim()
                : "Material sem nome (" + UUID.randomUUID().toString().substring(0, 8) + ")"
        );

        try {
            entity.setCategory(dto.getCategory() != null && !dto.getCategory().trim().isEmpty()
                ? MaterialCategory.fromValue(dto.getCategory().trim())
                : MaterialCategory.OTHER);
        } catch (Exception e) {
            entity.setCategory(MaterialCategory.OTHER);
        }

        try {
            entity.setUnit(dto.getUnit() != null && !dto.getUnit().trim().isEmpty()
                ? MaterialUnit.fromString(dto.getUnit().trim())
                : MaterialUnit.UNIT);
        } catch (Exception e) {
            entity.setUnit(MaterialUnit.UNIT);
        }

        entity.setConsumedQuantity(dto.getQuantityUsed() != null ? dto.getQuantityUsed().doubleValue() : 0.0);
        entity.setCurrentStock(dto.getCurrentStock() != null ? dto.getCurrentStock().doubleValue() : 0.0);
        entity.setMinimumStock(dto.getMinimumStock() != null ? dto.getMinimumStock().doubleValue() : 10.0);
        entity.setUrgency(dto.getUrgency());

        entity.updateRestockStatus();

        return entity;
    }

    @Transactional
    public void saveAll(List<MaterialDTO> dtos, String phaseId, Masonry masonry) {
        if (dtos == null || dtos.isEmpty()) return;

        List<MasonryMaterial> entities = dtos.stream()
            .map(dto -> mapToEntity(dto, phaseId, masonry))
            .collect(Collectors.toList());

        MasonryMaterial.persist(entities);
    }
}
