package construction.roofing.entity_external;

import construction.components.tools.ToolCondition;
import construction.components.tools.ToolDTO;
import construction.roofing.Roofing; // Assumindo existência
import construction.roofing.RoofingRepository; // Assumindo existência
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RoofingToolService {

    @Inject RoofingToolRepository repository;
    @Inject RoofingRepository roofingRepository;

    @Transactional
    public void saveAll(List<ToolDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) return;

        Roofing roofing = roofingRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Roofing não encontrado com ID: " + phaseId));

        for (ToolDTO dto : dtos) {
            RoofingTool entity = mapToEntity(dto);
            entity.setId(dto.getId() != null && !dto.getId().isBlank() ? dto.getId() : UUID.randomUUID().toString());
            entity.setPhaseId(phaseId);
            entity.setRoofing(roofing);

            System.out.println("Persistindo ferramenta (Roofing): " + entity.getId() + " - " + entity.getName());
            repository.persist(entity);
        }
    }

    private RoofingTool mapToEntity(ToolDTO dto) {
        RoofingTool entity = new RoofingTool();

        // Validação manual de nome
        entity.setName(
            dto.getName() != null && !dto.getName().trim().isEmpty()
                ? dto.getName().trim()
                : "Ferramenta sem nome (" + UUID.randomUUID().toString().substring(0, 8) + ")"
        );

        // Validação manual de categoria
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
}