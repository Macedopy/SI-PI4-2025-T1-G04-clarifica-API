package construction.foundation.entity_external;

import construction.components.used_material.MaterialCategory;
import construction.components.used_material.MaterialDTO;
import construction.components.used_material.MaterialUnit;
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
public class FoundationMaterialService {

    @Inject FoundationMaterialRepository foundationMaterialRepository;
    @Inject FoundationRepository foundationRepository;

    private FoundationMaterial mapToEntity(MaterialDTO dto, String phaseId, Foundation foundation) {
        FoundationMaterial entity = new FoundationMaterial();

        entity.setId(UUID.randomUUID().toString());

        entity.setPhaseId(phaseId);
        entity.setFoundation(foundation);

        // FIX DEFINITIVO: nunca mais null ou vazio
        entity.setName(
            dto.getName() != null && !dto.getName().trim().isEmpty()
                ? dto.getName().trim()
                : "Material sem nome (" + UUID.randomUUID().toString().substring(0, 8) + ")"
        );

        // Categoria
        try {
            entity.setCategory(dto.getCategory() != null && !dto.getCategory().trim().isEmpty()
                ? MaterialCategory.fromValue(dto.getCategory().trim())
                : MaterialCategory.OTHER);
        } catch (Exception e) {
            entity.setCategory(MaterialCategory.OTHER);
        }

        // Unidade
        try {
            entity.setUnit(dto.getUnit() != null && !dto.getUnit().trim().isEmpty()
                ? MaterialUnit.fromString(dto.getUnit().trim())
                : MaterialUnit.UNIT);
        } catch (Exception e) {
            entity.setUnit(MaterialUnit.UNIT);
        }

        // Quantidades
        entity.setConsumedQuantity(dto.getQuantityUsed() != null ? dto.getQuantityUsed().doubleValue() : 0.0);
        entity.setCurrentStock(dto.getCurrentStock() != null ? dto.getCurrentStock().doubleValue() : 0.0);
        entity.setMinimumStock(dto.getMinimumStock() != null ? dto.getMinimumStock().doubleValue() : 10.0);
        entity.setUrgency(dto.getUrgency());

        // Atualiza flag de reposição
        entity.updateRestockStatus();

        return entity;
    }

    @Transactional
    public void saveAll(List<MaterialDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) return;

        Foundation foundation = foundationRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Foundation não encontrada com ID: " + phaseId));

        List<FoundationMaterial> entities = dtos.stream()
            // Passa os dados de contexto e a DTO para mapeamento
            .map(dto -> mapToEntity(dto, phaseId, foundation))
            .collect(Collectors.toList());

        // Panache fará o UPSET (update ou insert) em lote.
        FoundationMaterial.persist(entities);
    }
}
