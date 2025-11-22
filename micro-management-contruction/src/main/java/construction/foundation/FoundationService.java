package construction.foundation;

import java.util.Optional;

import construction.foundation.entity_external.FoundationExecutedServiceService;
import construction.foundation.entity_external.FoundationMachineryService;
import construction.foundation.entity_external.FoundationMaterialService;
import construction.foundation.entity_external.FoundationPhotoRecordService;
import construction.foundation.entity_external.FoundationTeamMemberService;
import construction.foundation.entity_external.FoundationToolService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class FoundationService {
    
    @Inject FoundationRepository foundationRepository;    
    @Inject FoundationMaterialService foundationMaterialService;
    @Inject FoundationToolService foundationToolService; 
    @Inject FoundationMachineryService foundationMachineryService;
    @Inject FoundationTeamMemberService foundationTeamMemberService;
    @Inject FoundationExecutedServiceService foundationExecutedServiceService;
    @Inject FoundationPhotoRecordService foundationPhotoRecordService;
    
    @Transactional
    public void saveFoundation(Foundation foundation) {
        foundationRepository.persist(foundation);
    }
    
    @Transactional
    public void updateFoundation(String id, Foundation updatedFoundation) {
        Foundation entity = foundationRepository.findByIdOptional(id)
            .orElseThrow(() -> new NotFoundException("Fase Foundation não encontrada com ID: " + id));
            
        entity.setName(updatedFoundation.getName());
        entity.setContractor(updatedFoundation.getContractor());
        
        foundationRepository.persist(entity);
    }

    @Transactional
    public void saveAllPhaseDetails(String phaseId, FoundationDTO detailsDTO) {
        try {
            System.out.println("\n--- INICIANDO saveAllPhaseDetails para phaseId: " + phaseId);
            
            if (detailsDTO.getEquipe() != null && !detailsDTO.getEquipe().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getEquipe().size() + " membros da equipe...");
                foundationTeamMemberService.saveAll(detailsDTO.getEquipe(), phaseId);
                System.out.println("  ✓ Equipe salva!");
            } else {
                System.out.println("  ⊘ Equipe vazia ou null");
            }

            if (detailsDTO.getServicos() != null && !detailsDTO.getServicos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getServicos().size() + " serviços executados...");
                foundationExecutedServiceService.saveAll(detailsDTO.getServicos(), phaseId);
                System.out.println("  ✓ Serviços salvos!");
            }

            if (detailsDTO.getMaquinarios() != null && !detailsDTO.getMaquinarios().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMaquinarios().size() + " maquinários...");
                foundationMachineryService.saveAll(detailsDTO.getMaquinarios(), phaseId);
                System.out.println("  ✓ Maquinários salvos!");
            }

            if (detailsDTO.getMateriais() != null && !detailsDTO.getMateriais().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMateriais().size() + " materiais...");
                foundationMaterialService.saveAll(detailsDTO.getMateriais(), phaseId);
                System.out.println("  ✓ Materiais salvos!");
            }

            if (detailsDTO.getFerramentas() != null && !detailsDTO.getFerramentas().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFerramentas().size() + " ferramentas...");
                foundationToolService.saveAll(detailsDTO.getFerramentas(), phaseId);
                System.out.println("  ✓ Ferramentas salvas!");
            }
            
            if (detailsDTO.getFotos() != null && !detailsDTO.getFotos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFotos().size() + " fotos...");
                foundationPhotoRecordService.saveAll(detailsDTO.getFotos(), phaseId);
                System.out.println("  ✓ Fotos salvas!");
            }
            
            System.out.println("--- FIM saveAllPhaseDetails\n");
        } catch (Exception e) {
            System.err.println("ERRO em saveAllPhaseDetails: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Optional<Foundation> getFoundationById(String id) {
        return foundationRepository.findByIdOptional(id);
    }
}
