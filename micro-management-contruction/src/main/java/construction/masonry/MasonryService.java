package construction.masonry;

import java.util.Optional;

import construction.masonry.entity_external.*;
import construction.structure.Structure;
import construction.user.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class MasonryService {
    
    @Inject MasonryRepository masonryRepository;    
    @Inject MasonryMaterialService masonryMaterialService;
    @Inject MasonryToolService masonryToolService; 
    @Inject MasonryMachineryService masonryMachineryService;
    @Inject MasonryTeamMemberService masonryTeamMemberService;
    @Inject MasonryExecutedServiceService masonryExecutedServiceService;
    @Inject MasonryPhotoRecordService masonryPhotoRecordService;
    
    @Transactional
    public void saveMasonry(Masonry masonry) {
        if (masonry.getName() == null || masonry.getName().isBlank()) {
            throw new IllegalArgumentException("Phase name is mandatory.");
        }
        masonryRepository.persist(masonry);
    }
    
    @Transactional
    public String updateMasonry(String userId, MasonryDTO dto) {
        User user = User.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found for ID: " + userId);
        }

        Masonry masonry = user.getMasonry();
        if (masonry == null) {
            throw new NotFoundException("Masonry not found for User ID: " + userId + ". Cannot perform update.");
        }
        
        if (dto.getPhaseName() != null && !dto.getPhaseName().isBlank()) {
            masonry.setName(dto.getPhaseName());
        }
        if (dto.getContractor() != null) {
            masonry.setContractor(dto.getContractor());
        }
        
        deleteAllPhaseDetails(masonry.getId());
        
        return masonry.getId();
    }

    @Transactional
    public void deleteAllPhaseDetails(String masonryId) {
        MasonryTeamMember.delete("masonry.id", masonryId);
        MasonryExecutedService.delete("masonry.id", masonryId);
        MasonryMachinery.delete("masonry.id", masonryId);
        MasonryMaterial.delete("masonry.id", masonryId);
        MasonryTool.delete("masonry.id", masonryId);
        MasonryPhotoRecord.delete("masonry.id", masonryId);
    }

    @Transactional
    public void saveAllPhaseDetails(String phaseId, MasonryDTO detailsDTO) {
        Masonry masonry = Masonry.findById(phaseId);
        if (masonry == null) {
            throw new NotFoundException("Masonry not found for ID: " + phaseId);
        }
        
        try {
            System.out.println("\n--- INICIANDO saveAllPhaseDetails (Masonry) para phaseId: " + phaseId);
            
            if (detailsDTO.getEquipe() != null && !detailsDTO.getEquipe().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getEquipe().size() + " membros da equipe...");
                masonryTeamMemberService.saveAll(detailsDTO.getEquipe(), phaseId, masonry);
                System.out.println("  ✓ Equipe salva!");
            }

            if (detailsDTO.getServicos() != null && !detailsDTO.getServicos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getServicos().size() + " serviços executados...");
                masonryExecutedServiceService.saveAll(detailsDTO.getServicos(), phaseId, masonry);
                System.out.println("  ✓ Serviços salvos!");
            }

            if (detailsDTO.getMaquinarios() != null && !detailsDTO.getMaquinarios().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMaquinarios().size() + " maquinários...");
                masonryMachineryService.saveAll(detailsDTO.getMaquinarios(), phaseId, masonry);
                System.out.println("  ✓ Maquinários salvos!");
            }

            if (detailsDTO.getMateriais() != null && !detailsDTO.getMateriais().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMateriais().size() + " materiais...");
                masonryMaterialService.saveAll(detailsDTO.getMateriais(), phaseId, masonry);
                System.out.println("  ✓ Materiais salvos!");
            }

            if (detailsDTO.getFerramentas() != null && !detailsDTO.getFerramentas().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFerramentas().size() + " ferramentas...");
                masonryToolService.saveAll(detailsDTO.getFerramentas(), phaseId, masonry);
                System.out.println("  ✓ Ferramentas salvas!");
            }
            
            if (detailsDTO.getFotos() != null && !detailsDTO.getFotos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFotos().size() + " fotos...");
                masonryPhotoRecordService.saveAll(detailsDTO.getFotos(), phaseId, masonry);
                System.out.println("  ✓ Fotos salvas!");
            }
            
            System.out.println("--- FIM saveAllPhaseDetails (Masonry)\n");
        } catch (Exception e) {
            System.err.println("ERRO em saveAllPhaseDetails (Masonry): " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Optional<Masonry> getMasonryById(String id) {
        return masonryRepository.findByIdOptional(id);
    }

    public Optional<Masonry> getMasonryByCustomerId(String customerId) {
        return masonryRepository.find("userId", customerId).firstResultOptional();
    }
}
