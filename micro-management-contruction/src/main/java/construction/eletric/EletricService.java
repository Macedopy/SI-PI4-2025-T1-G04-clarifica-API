package construction.eletric;

import java.util.Optional;

import construction.eletric.entity_external.*;
import construction.masonry.Masonry;
import construction.user.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class EletricService {
    
    @Inject EletricRepository eletricRepository;    
    @Inject EletricMaterialService eletricMaterialService;
    @Inject EletricToolService eletricToolService; 
    @Inject EletricMachineryService eletricMachineryService;
    @Inject EletricTeamMemberService eletricTeamMemberService;
    @Inject EletricExecutedServiceService eletricExecutedServiceService;
    @Inject EletricPhotoRecordService eletricPhotoRecordService;
    
    @Transactional
    public void saveEletric(Eletric eletric) {
        if (eletric.getName() == null || eletric.getName().isBlank()) {
            throw new IllegalArgumentException("Phase name is mandatory.");
        }
        eletricRepository.persist(eletric);
    }
    
    @Transactional
    public String updateEletric(String userId, EletricDTO dto) {
        User user = User.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found for ID: " + userId);
        }

        Eletric eletric = user.getElectrical();
        if (eletric == null) {
            throw new NotFoundException("Eletric not found for User ID: " + userId + ". Cannot perform update.");
        }
        
        if (dto.getPhaseName() != null && !dto.getPhaseName().isBlank()) {
            eletric.setName(dto.getPhaseName());
        }
        if (dto.getContractor() != null) {
            eletric.setContractor(dto.getContractor());
        }
        
        deleteAllPhaseDetails(eletric.getId());
        
        return eletric.getId();
    }

    @Transactional
    public void deleteAllPhaseDetails(String eletricId) {
        EletricTeamMember.delete("eletric.id", eletricId);
        EletricExecutedService.delete("eletric.id", eletricId);
        EletricMachinery.delete("eletric.id", eletricId);
        EletricMaterial.delete("eletric.id", eletricId);
        EletricTool.delete("eletric.id", eletricId);
        EletricPhotoRecord.delete("eletric.id", eletricId);
    }

    @Transactional
    public void saveAllPhaseDetails(String phaseId, EletricDTO detailsDTO) {
        Eletric eletric = Eletric.findById(phaseId);
        if (eletric == null) {
            throw new NotFoundException("Eletric not found for ID: " + phaseId);
        }
        
        try {
            System.out.println("\n--- INICIANDO saveAllPhaseDetails (Eletric) para phaseId: " + phaseId);
            
            if (detailsDTO.getEquipe() != null && !detailsDTO.getEquipe().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getEquipe().size() + " membros da equipe...");
                eletricTeamMemberService.saveAll(detailsDTO.getEquipe(), phaseId, eletric);
                System.out.println("  ✓ Equipe salva!");
            }

            if (detailsDTO.getServicos() != null && !detailsDTO.getServicos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getServicos().size() + " serviços executados...");
                eletricExecutedServiceService.saveAll(detailsDTO.getServicos(), phaseId, eletric);
                System.out.println("  ✓ Serviços salvos!");
            }

            if (detailsDTO.getMaquinarios() != null && !detailsDTO.getMaquinarios().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMaquinarios().size() + " maquinários...");
                eletricMachineryService.saveAll(detailsDTO.getMaquinarios(), phaseId, eletric);
                System.out.println("  ✓ Maquinários salvos!");
            }

            if (detailsDTO.getMateriais() != null && !detailsDTO.getMateriais().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMateriais().size() + " materiais...");
                eletricMaterialService.saveAll(detailsDTO.getMateriais(), phaseId, eletric);
                System.out.println("  ✓ Materiais salvos!");
            }

            if (detailsDTO.getFerramentas() != null && !detailsDTO.getFerramentas().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFerramentas().size() + " ferramentas...");
                eletricToolService.saveAll(detailsDTO.getFerramentas(), phaseId, eletric);
                System.out.println("  ✓ Ferramentas salvas!");
            }
            
            if (detailsDTO.getFotos() != null && !detailsDTO.getFotos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFotos().size() + " fotos...");
                eletricPhotoRecordService.saveAll(detailsDTO.getFotos(), phaseId, eletric);
                System.out.println("  ✓ Fotos salvas!");
            }
            
            System.out.println("--- FIM saveAllPhaseDetails (Eletric)\n");
        } catch (Exception e) {
            System.err.println("ERRO em saveAllPhaseDetails (Eletric): " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Optional<Eletric> getEletricById(String id) {
        return eletricRepository.findByIdOptional(id);
    }

    public Optional<Eletric> getEletricByCustomerId(String customerId) {
        return eletricRepository.find("userId", customerId).firstResultOptional();
    }
}
