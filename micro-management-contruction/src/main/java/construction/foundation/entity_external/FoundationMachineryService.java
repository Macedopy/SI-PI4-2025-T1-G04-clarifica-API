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
import java.util.stream.Collectors; // Adicionado para o stream

@ApplicationScoped
public class FoundationMachineryService {

    @Inject
    FoundationMachineryRepository repository;

    @Inject
    FoundationRepository foundationRepository;

    protected FoundationMachinery mapToEntity(MachineryDTO dto, String phaseId, Foundation foundation) {
        FoundationMachinery entity = new FoundationMachinery();

        entity.setId(UUID.randomUUID().toString());

        entity.setPhaseId(phaseId);
        entity.setFoundation(foundation);

        entity.setName(dto.getName());
        String category = dto.getCategory();
        if (category == null || category.isBlank()) {
            category = "OTHER";
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

    @Transactional
    public void saveAll(List<MachineryDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }

        Foundation foundation = foundationRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Foundation não encontrada com ID: " + phaseId));

        List<FoundationMachinery> entities = dtos.stream()
            // Passa os dados de contexto e a DTO para mapeamento
            .map(dto -> mapToEntity(dto, phaseId, foundation))
            .collect(Collectors.toList());

        // Panache fará o UPSET (update ou insert) em lote.
        FoundationMachinery.persist(entities);
    }
}
