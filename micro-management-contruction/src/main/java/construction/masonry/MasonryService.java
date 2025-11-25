package construction.masonry;

import java.util.Optional;

import construction.masonry.entity_external.MasonryExecutedServiceService;
import construction.masonry.entity_external.MasonryMachineryService;
import construction.masonry.entity_external.MasonryMaterialService;
import construction.masonry.entity_external.MasonryPhotoRecordService;
import construction.masonry.entity_external.MasonryTeamMemberService;
import construction.masonry.entity_external.MasonryToolService;
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
        masonryRepository.persist(masonry);
    }
    
    @Transactional
    public void updateMasonry(String id, Masonry updatedMasonry) {
        Masonry entity = masonryRepository.findByIdOptional(id)
            .orElseThrow(() -> new NotFoundException("Fase Masonry não encontrada com ID: " + id));
            
        entity.setName(updatedMasonry.getName());
        entity.setContractor(updatedMasonry.getContractor());
        
        masonryRepository.persist(entity);
    }

    @Transactional
    public void saveAllPhaseDetails(String phaseId, MasonryDTO detailsDTO) {
        try {
            System.out.println("\n--- INICIANDO saveAllPhaseDetails (Masonry) para phaseId: " + phaseId);
            
            if (detailsDTO.getEquipe() != null && !detailsDTO.getEquipe().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getEquipe().size() + " membros da equipe...");
                masonryTeamMemberService.saveAll(detailsDTO.getEquipe(), phaseId);
                System.out.println("  ✓ Equipe salva!");
            }

            if (detailsDTO.getServicos() != null && !detailsDTO.getServicos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getServicos().size() + " serviços executados...");
                masonryExecutedServiceService.saveAll(detailsDTO.getServicos(), phaseId);
                System.out.println("  ✓ Serviços salvos!");
            }

            if (detailsDTO.getMaquinarios() != null && !detailsDTO.getMaquinarios().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMaquinarios().size() + " maquinários...");
                masonryMachineryService.saveAll(detailsDTO.getMaquinarios(), phaseId);
                System.out.println("  ✓ Maquinários salvos!");
            }

            if (detailsDTO.getMateriais() != null && !detailsDTO.getMateriais().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMateriais().size() + " materiais...");
                masonryMaterialService.saveAll(detailsDTO.getMateriais(), phaseId);
                System.out.println("  ✓ Materiais salvos!");
            }

            if (detailsDTO.getFerramentas() != null && !detailsDTO.getFerramentas().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFerramentas().size() + " ferramentas...");
                masonryToolService.saveAll(detailsDTO.getFerramentas(), phaseId);
                System.out.println("  ✓ Ferramentas salvas!");
            }
            
            if (detailsDTO.getFotos() != null && !detailsDTO.getFotos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFotos().size() + " fotos...");
                masonryPhotoRecordService.saveAll(detailsDTO.getFotos(), phaseId);
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
}