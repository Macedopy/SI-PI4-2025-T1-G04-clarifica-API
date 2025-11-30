package construction.structure.entity_external;

import construction.components.machinery.Condition;
import construction.components.machinery.FuelUnit;
import construction.components.machinery.MachineryDTO;
import construction.structure.Structure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class StructureMachineryService {

    @Inject
    StructureMachineryRepository repository;

    protected StructureMachinery mapToEntity(MachineryDTO dto, String phaseId, Structure structure) {
        StructureMachinery entity = new StructureMachinery();

        // ✅ Gera novo ID sempre
        entity.setId(UUID.randomUUID().toString());
        
        entity.setPhaseId(phaseId);
        entity.setStructure(structure);

        entity.setName(
            dto.getName() != null && !dto.getName().isBlank() 
            ? dto.getName() 
            : "Equipamento sem nome"
        );

        String category = dto.getCategory();
        if (category == null || category.isBlank()) {
            category = "OTHER";  
        }
        entity.setCategory(category);

        entity.setTotalQuantity(Math.max(1, dto.getTotalQuantity()));
        entity.setInOperation(Math.max(0, dto.getInOperation()));
        entity.setInMaintenance(Math.max(0, dto.getInMaintenance()));

        entity.setHoursWorked(Math.max(0, dto.getHoursWorked()));
        entity.setFuelUsed(Math.max(0, dto.getFuelUsed()));
        
        if (dto.getFuelUnit() != null) {
            try {
                entity.setFuelUnit(FuelUnit.valueOf(dto.getFuelUnit().toUpperCase()));
            } catch (IllegalArgumentException e) {
                entity.setFuelUnit(FuelUnit.LITERS);
            }
        } else {
            entity.setFuelUnit(FuelUnit.LITERS);
        }
        
        if (dto.getCondition() != null && !dto.getCondition().isBlank()) {
            try {
                entity.setCondition(Condition.valueOf(dto.getCondition().toUpperCase()));
            } catch (IllegalArgumentException e) {
                entity.setCondition(Condition.GOOD);
            }
        } else {
            entity.setCondition(Condition.GOOD);
        }
        
        entity.setNotes(dto.getNotes());

        return entity;
    }

    @Transactional
    public void saveAll(List<MachineryDTO> dtos, String phaseId, Structure structure) {
        if (dtos == null || dtos.isEmpty()) return;

        List<StructureMachinery> entities = dtos.stream()
            .map(dto -> mapToEntity(dto, phaseId, structure))
            .collect(Collectors.toList());

        StructureMachinery.persist(entities);
    }
}
