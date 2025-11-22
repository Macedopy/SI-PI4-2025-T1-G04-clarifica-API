package construction.foundation.entity_external;

import construction.components.machinery.Condition;
import construction.components.machinery.FuelUnit;
import construction.components.machinery.MachineryDTO;
import construction.foundation.Foundation;
import construction.foundation.FoundationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FoundationMachineryService {

    @Inject
    FoundationMachineryRepository repository;

    @Inject
    FoundationRepository foundationRepository;

    @Transactional
    public void saveAll(List<MachineryDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }

        Foundation foundation = foundationRepository.findByIdOptional(phaseId)
                .orElseThrow(() -> new NotFoundException("Foundation não encontrada com ID: " + phaseId));

        for (MachineryDTO dto : dtos) {
            FoundationMachinery entity = mapToEntity(dto);
            String idToUse;
            if (dto.getId() != null && !dto.getId().isBlank()) {
                idToUse = dto.getId();
            } else {
                idToUse = UUID.randomUUID().toString();
            }
            entity.setId(idToUse);
            entity.setPhaseId(phaseId);
            entity.setFoundation(foundation);

            repository.persist(entity);
        }
    }

    protected FoundationMachinery mapToEntity(MachineryDTO dto) {
        FoundationMachinery entity = new FoundationMachinery();

        entity.setName(dto.getName());
        String category = dto.getCategory();
        if (category == null || category.isBlank()) {
            category = "OTHER";  // default category
        }
        entity.setCategory(category);

        entity.setTotalQuantity(dto.getTotalQuantity());
        entity.setInOperation(dto.getInOperation());
        entity.setInMaintenance(dto.getInMaintenance());

        entity.setHoursWorked(dto.getHoursWorked());
        entity.setFuelUsed(dto.getFuelUsed());
        
        if (dto.getFuelUnit() != null) {
            entity.setFuelUnit(FuelUnit.valueOf(dto.getFuelUnit().toUpperCase()));
        }
        
        if (dto.getCondition() != null && !dto.getCondition().isBlank()) {
            entity.setCondition(Condition.valueOf(dto.getCondition().toUpperCase()));
        } else {
            entity.setCondition(Condition.GOOD); // default condition
        }
        
        entity.setNotes(dto.getNotes());

        return entity;
    }
}
