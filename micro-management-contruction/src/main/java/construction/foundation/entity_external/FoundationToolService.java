package construction.foundation.entity_external;

import construction.components.tools.ToolCondition;
import construction.components.tools.ToolDTO;
import construction.foundation.Foundation;
import construction.foundation.FoundationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FoundationToolService {

    @Inject FoundationToolRepository repository;
    @Inject FoundationRepository foundationRepository;

    @Transactional
    public void saveAll(List<ToolDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) return;

        Foundation foundation = foundationRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Foundation não encontrada com ID: " + phaseId));

        for (ToolDTO dto : dtos) {
            FoundationTool entity = mapToEntity(dto);
            entity.setId(dto.getId() != null && !dto.getId().isBlank() ? dto.getId() : UUID.randomUUID().toString());
            entity.setPhaseId(phaseId);
            entity.setFoundation(foundation);

            System.out.println("Persistindo ferramenta: " + entity.getId() + " - " + entity.getName());
            repository.persist(entity);
        }
    }

    private FoundationTool mapToEntity(ToolDTO dto) {
        FoundationTool entity = new FoundationTool();

        // NOME — nunca null ou vazio
        entity.setName(
            dto.getName() != null && !dto.getName().trim().isEmpty()
                ? dto.getName().trim()
                : "Ferramenta sem nome (" + UUID.randomUUID().toString().substring(0, 8) + ")"
        );

        // CATEGORIA — nunca null ou vazio
        entity.setCategory(
            dto.getCategory() != null && !dto.getCategory().trim().isEmpty()
                ? dto.getCategory().trim()
                : "Manual"
        );

        // QUANTIDADE TOTAL — mínimo 1 (int primitivo, não pode null)
        Integer totalQty = dto.getTotalQuantity();
        entity.setTotalQuantity(totalQty != null && totalQty >= 1 ? totalQty : 1);

        // EM USO — pode ser 0
        Integer inUse = dto.getInUse();
        entity.setInUse(inUse != null && inUse >= 0 ? inUse : 0);

        // EM MANUTENÇÃO — pode ser 0
        Integer inMaintenance = dto.getInMaintenance();
        entity.setInMaintenance(inMaintenance != null && inMaintenance >= 0 ? inMaintenance : 0);

        // CONDIÇÃO — fallback seguro
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
