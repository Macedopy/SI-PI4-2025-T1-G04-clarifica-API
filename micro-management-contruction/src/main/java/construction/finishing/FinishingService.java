package construction.finishing;

import java.util.Optional;

import construction.eletric.Eletric;
import construction.finishing.entity_external.FinishingExecutedServiceService;
import construction.finishing.entity_external.FinishingMachineryService;
import construction.finishing.entity_external.FinishingMaterialService;
import construction.finishing.entity_external.FinishingPhotoRecordService;
import construction.finishing.entity_external.FinishingTeamMemberService;
import construction.finishing.entity_external.FinishingToolService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class FinishingService {
    
    @Inject FinishingRepository finishingRepository;    
    @Inject FinishingMaterialService finishingMaterialService;
    @Inject FinishingToolService finishingToolService; 
    @Inject FinishingMachineryService finishingMachineryService;
    @Inject FinishingTeamMemberService finishingTeamMemberService;
    @Inject FinishingExecutedServiceService finishingExecutedServiceService;
    @Inject FinishingPhotoRecordService finishingPhotoRecordService;
    
    @Transactional
    public void saveFinishing(Finishing finishing) {
        finishingRepository.persist(finishing);
    }
    
    @Transactional
    public void updateFinishing(String id, Finishing updatedFinishing) {
        Finishing entity = finishingRepository.findByIdOptional(id)
            .orElseThrow(() -> new NotFoundException("Fase Finishing não encontrada com ID: " + id));
            
        entity.setName(updatedFinishing.getName());
        entity.setContractor(updatedFinishing.getContractor());
        
        finishingRepository.persist(entity);
    }

    @Transactional
    public void saveAllPhaseDetails(String phaseId, FinishingDTO detailsDTO) {
        try {
            System.out.println("\n--- INICIANDO saveAllPhaseDetails para phaseId: " + phaseId);
            
            if (detailsDTO.getEquipe() != null && !detailsDTO.getEquipe().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getEquipe().size() + " membros da equipe...");
                finishingTeamMemberService.saveAll(detailsDTO.getEquipe(), phaseId);
                System.out.println("  ✓ Equipe salva!");
            } else {
                System.out.println("  ⊘ Equipe vazia ou null");
            }

            if (detailsDTO.getServicos() != null && !detailsDTO.getServicos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getServicos().size() + " serviços executados...");
                finishingExecutedServiceService.saveAll(detailsDTO.getServicos(), phaseId);
                System.out.println("  ✓ Serviços salvos!");
            }

            if (detailsDTO.getMaquinarios() != null && !detailsDTO.getMaquinarios().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMaquinarios().size() + " maquinários...");
                finishingMachineryService.saveAll(detailsDTO.getMaquinarios(), phaseId);
                System.out.println("  ✓ Maquinários salvos!");
            }

            if (detailsDTO.getMateriais() != null && !detailsDTO.getMateriais().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMateriais().size() + " materiais...");
                finishingMaterialService.saveAll(detailsDTO.getMateriais(), phaseId);
                System.out.println("  ✓ Materiais salvos!");
            }

            if (detailsDTO.getFerramentas() != null && !detailsDTO.getFerramentas().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFerramentas().size() + " ferramentas...");
                finishingToolService.saveAll(detailsDTO.getFerramentas(), phaseId);
                System.out.println("  ✓ Ferramentas salvas!");
            }
            
            if (detailsDTO.getFotos() != null && !detailsDTO.getFotos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFotos().size() + " fotos...");
                finishingPhotoRecordService.saveAll(detailsDTO.getFotos(), phaseId);
                System.out.println("  ✓ Fotos salvas!");
            }
            
            System.out.println("--- FIM saveAllPhaseDetails\n");
        } catch (Exception e) {
            System.err.println("ERRO em saveAllPhaseDetails: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Optional<Finishing> getFinishingById(String id) {
        return finishingRepository.findByIdOptional(id);
    }

    public Optional<Finishing> getFinishingByCustomerId(String customerId) {
        return finishingRepository.find("userId", customerId).firstResultOptional();
    }
}