package construction.masonry.entity_external;

import construction.components.machinery.Condition;
import construction.components.machinery.FuelUnit;
import construction.components.machinery.MachineryDTO;
import construction.masonry.Masonry;
import construction.masonry.MasonryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class MasonryMachineryService {

    @Inject
    MasonryMachineryRepository repository;

    @Inject
    MasonryRepository masonryRepository;

    @Transactional
    public void saveAll(List<MachineryDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }

        Masonry masonry = masonryRepository.findByIdOptional(phaseId)
                .orElseThrow(() -> new NotFoundException("Masonry não encontrada com ID: " + phaseId));

        for (MachineryDTO dto : dtos) {
            MasonryMachinery entity = mapToEntity(dto);
            String idToUse;
            if (dto.getId() != null && !dto.getId().isBlank()) {
                idToUse = dto.getId();
            } else {
                idToUse = UUID.randomUUID().toString();
            }
            entity.setId(idToUse);
            entity.setPhaseId(phaseId);
            entity.setMasonry(masonry);

            repository.persist(entity);
        }
    }

    protected MasonryMachinery mapToEntity(MachineryDTO dto) {
        MasonryMachinery entity = new MasonryMachinery();

        // Tratamento de nulos/defaults já que removemos o @NotBlank
        entity.setName(
            dto.getName() != null && !dto.getName().isBlank() 
            ? dto.getName() 
            : "Equipamento sem nome"
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
        
        if (dto.getFuelUnit() != null) {
            try {
                entity.setFuelUnit(FuelUnit.valueOf(dto.getFuelUnit().toUpperCase()));
            } catch (IllegalArgumentException e) {
                entity.setFuelUnit(FuelUnit.LITERS); // Default
            }
        } else {
            entity.setFuelUnit(FuelUnit.LITERS); // Default
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
}