package construction.roofing.entity_external;

import construction.components.machinery.Condition;
import construction.components.machinery.FuelUnit;
import construction.components.machinery.MachineryDTO;
import construction.roofing.Roofing;
import construction.roofing.RoofingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RoofingMachineryService {

    @Inject
    RoofingMachineryRepository repository;

    @Inject
    RoofingRepository roofingRepository;

    @Transactional
    public void saveAll(List<MachineryDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }

        Roofing roofing = roofingRepository.findByIdOptional(phaseId)
                .orElseThrow(() -> new NotFoundException("Roofing (Cobertura) não encontrada com ID: " + phaseId));

        for (MachineryDTO dto : dtos) {
            RoofingMachinery entity = mapToEntity(dto);
            String idToUse;
            if (dto.getId() != null && !dto.getId().isBlank()) {
                idToUse = dto.getId();
            } else {
                idToUse = UUID.randomUUID().toString();
            }
            entity.setId(idToUse);
            entity.setPhaseId(phaseId);
            entity.setRoofing(roofing);

            repository.persist(entity);
        }
    }

    protected RoofingMachinery mapToEntity(MachineryDTO dto) {
        RoofingMachinery entity = new RoofingMachinery();

        // Tratamento para evitar nulos
        entity.setName(
            dto.getName() != null && !dto.getName().isBlank() 
            ? dto.getName() 
            : "Maquinário sem nome"
        );

        String category = dto.getCategory();
        if (category == null || category.isBlank()) {
            category = "OUTROS"; 
        }
        entity.setCategory(category);

        entity.setTotalQuantity(Math.max(1, dto.getTotalQuantity()));
        entity.setInOperation(Math.max(0, dto.getInOperation()));
        entity.setInMaintenance(Math.max(0, dto.getInMaintenance()));

        entity.setHoursWorked(Math.max(0, dto.getHoursWorked()));
        entity.setFuelUsed(Math.max(0, dto.getFuelUsed()));
        
        // Enum FuelUnit com fallback
        if (dto.getFuelUnit() != null) {
            try {
                entity.setFuelUnit(FuelUnit.valueOf(dto.getFuelUnit().toUpperCase()));
            } catch (IllegalArgumentException e) {
                entity.setFuelUnit(FuelUnit.LITERS);
            }
        } else {
            entity.setFuelUnit(FuelUnit.LITERS);
        }
        
        // Enum Condition com fallback
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
}