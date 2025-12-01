package construction.coatings;

import java.util.Optional;

import construction.coatings.entity_external.*;
import construction.masonry.Masonry;
import construction.user.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CoatingsService {
    
    @Inject CoatingsRepository coatingsRepository;    
    @Inject CoatingsMaterialService coatingsMaterialService;
    @Inject CoatingsToolService coatingsToolService; 
    @Inject CoatingsMachineryService coatingsMachineryService;
    @Inject CoatingsTeamMemberService coatingsTeamMemberService;
    @Inject CoatingsExecutedServiceService coatingsExecutedServiceService;
    @Inject CoatingsPhotoRecordService coatingsPhotoRecordService;
    
    @Transactional
    public void saveCoatings(Coatings coatings) {
        if (coatings.getName() == null || coatings.getName().isBlank()) {
            throw new IllegalArgumentException("Phase name is mandatory.");
        }
        coatingsRepository.persist(coatings);
    }
    
    @Transactional
    public String updateCoatings(String userId, CoatingsDTO dto) {
        User user = User.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found for ID: " + userId);
        }

        Coatings coatings = user.getCoatings();
        if (coatings == null) {
            throw new NotFoundException("Coatings not found for User ID: " + userId + ". Cannot perform update.");
        }
        
        if (dto.getPhaseName() != null && !dto.getPhaseName().isBlank()) {
            coatings.setName(dto.getPhaseName());
        }
        if (dto.getContractor() != null) {
            coatings.setContractor(dto.getContractor());
        }
        
        deleteAllPhaseDetails(coatings.getId());
        
        return coatings.getId();
    }

    @Transactional
    public void deleteAllPhaseDetails(String coatingsId) {
        CoatingsTeamMember.delete("coatings.id", coatingsId);
        CoatingsExecutedService.delete("coatings.id", coatingsId);
        CoatingsMachinery.delete("coatings.id", coatingsId);
        CoatingsMaterial.delete("coatings.id", coatingsId);
        CoatingsTool.delete("coatings.id", coatingsId);
        CoatingsPhotoRecord.delete("coatings.id", coatingsId);
    }

    @Transactional
    public void saveAllPhaseDetails(String phaseId, CoatingsDTO detailsDTO) {
        Coatings coatings = Coatings.findById(phaseId);
        if (coatings == null) {
            throw new NotFoundException("Coatings not found for ID: " + phaseId);
        }
        
        try {
            System.out.println("\n--- INICIANDO saveAllPhaseDetails (Coatings) para phaseId: " + phaseId);
            
            if (detailsDTO.getEquipe() != null && !detailsDTO.getEquipe().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getEquipe().size() + " membros da equipe...");
                coatingsTeamMemberService.saveAll(detailsDTO.getEquipe(), phaseId, coatings);
                System.out.println("  ✓ Equipe salva!");
            }

            if (detailsDTO.getServicos() != null && !detailsDTO.getServicos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getServicos().size() + " serviços executados...");
                coatingsExecutedServiceService.saveAll(detailsDTO.getServicos(), phaseId, coatings);
                System.out.println("  ✓ Serviços salvos!");
            }

            if (detailsDTO.getMaquinarios() != null && !detailsDTO.getMaquinarios().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMaquinarios().size() + " maquinários...");
                coatingsMachineryService.saveAll(detailsDTO.getMaquinarios(), phaseId, coatings);
                System.out.println("  ✓ Maquinários salvos!");
            }

            if (detailsDTO.getMateriais() != null && !detailsDTO.getMateriais().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMateriais().size() + " materiais...");
                coatingsMaterialService.saveAll(detailsDTO.getMateriais(), phaseId, coatings);
                System.out.println("  ✓ Materiais salvos!");
            }

            if (detailsDTO.getFerramentas() != null && !detailsDTO.getFerramentas().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFerramentas().size() + " ferramentas...");
                coatingsToolService.saveAll(detailsDTO.getFerramentas(), phaseId, coatings);
                System.out.println("  ✓ Ferramentas salvas!");
            }
            
            if (detailsDTO.getFotos() != null && !detailsDTO.getFotos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFotos().size() + " fotos...");
                coatingsPhotoRecordService.saveAll(detailsDTO.getFotos(), phaseId, coatings);
                System.out.println("  ✓ Fotos salvas!");
            }
            
            System.out.println("--- FIM saveAllPhaseDetails (Coatings)\n");
        } catch (Exception e) {
            System.err.println("ERRO em saveAllPhaseDetails (Coatings): " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Optional<Coatings> getCoatingsById(String id) {
        return coatingsRepository.findByIdOptional(id);
    }

    public Optional<Coatings> getCoatingsByCustomerId(String customerId) {
        return coatingsRepository.find("user_id", customerId).firstResultOptional();
    }
}
