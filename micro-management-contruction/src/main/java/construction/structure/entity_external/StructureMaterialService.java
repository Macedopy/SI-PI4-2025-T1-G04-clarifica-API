package construction.structure.entity_external;

import construction.components.used_material.MaterialCategory;
import construction.components.used_material.MaterialDTO;
import construction.components.used_material.MaterialUnit;
import construction.structure.Structure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class StructureMaterialService {

    @Inject StructureMaterialRepository repository;

    private StructureMaterial mapToEntity(MaterialDTO dto, String phaseId, Structure structure) {
        StructureMaterial entity = new StructureMaterial();

        // ✅ Gera novo ID sempre
        entity.setId(UUID.randomUUID().toString());
        
        entity.setPhaseId(phaseId);
        entity.setStructure(structure);

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
    public void saveAll(List<MaterialDTO> dtos, String phaseId, Structure structure) {
        if (dtos == null || dtos.isEmpty()) return;

        List<StructureMaterial> entities = dtos.stream()
            .map(dto -> mapToEntity(dto, phaseId, structure))
            .collect(Collectors.toList());

        StructureMaterial.persist(entities);
    }
}
