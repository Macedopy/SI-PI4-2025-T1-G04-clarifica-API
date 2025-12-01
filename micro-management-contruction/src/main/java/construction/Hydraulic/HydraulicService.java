package construction.hydraulic;

import java.util.Optional;

import construction.foundation.Foundation;
import construction.hydraulic.entity_external.*;
import construction.user.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class HydraulicService {

    @Inject HydraulicRepository hydraulicRepository;
    @Inject HydraulicMaterialService hydraulicMaterialService;
    @Inject HydraulicToolService hydraulicToolService;
    @Inject HydraulicMachineryService hydraulicMachineryService;
    @Inject HydraulicTeamMemberService hydraulicTeamMemberService;
    @Inject HydraulicExecutedServiceService hydraulicExecutedServiceService;
    @Inject HydraulicPhotoRecordService hydraulicPhotoRecordService;

    @Transactional
    public void saveHydraulic(Hydraulic hydraulic) {
        if (hydraulic.getName() == null || hydraulic.getName().isBlank()) {
            throw new IllegalArgumentException("Phase name is mandatory.");
        }
        hydraulicRepository.persist(hydraulic);
    }

    @Transactional
    public String updateHydraulic(String userId, HydraulicDTO dto) {
        User user = User.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found for ID: " + userId);
        }

        Hydraulic hydraulic = user.getHydraulic();
        if (hydraulic == null) {
            throw new NotFoundException("Hydraulic not found for User ID: " + userId + ". Cannot perform update.");
        }
        
        if (dto.getPhaseName() != null && !dto.getPhaseName().isBlank()) {
            hydraulic.setName(dto.getPhaseName());
        }
        if (dto.getContractor() != null) {
            hydraulic.setContractor(dto.getContractor());
        }
        
        deleteAllPhaseDetails(hydraulic.getId());
        
        return hydraulic.getId();
    }

    @Transactional
    public void deleteAllPhaseDetails(String hydraulicId) {
        HydraulicTeamMember.delete("hydraulic.id", hydraulicId);
        HydraulicExecutedService.delete("hydraulic.id", hydraulicId);
        HydraulicMachinery.delete("hydraulic.id", hydraulicId);
        HydraulicMaterial.delete("hydraulic.id", hydraulicId);
        HydraulicTool.delete("hydraulic.id", hydraulicId);
        HydraulicPhotoRecord.delete("hydraulic.id", hydraulicId);
    }

    @Transactional
    public void saveAllPhaseDetails(String phaseId, HydraulicDTO detailsDTO) {
        Hydraulic hydraulic = Hydraulic.findById(phaseId);
        if (hydraulic == null) {
            throw new NotFoundException("Hydraulic not found for ID: " + phaseId);
        }
        
        try {
            System.out.println("\n--- INICIANDO saveAllPhaseDetails para phaseId: " + phaseId);

            if (detailsDTO.getEquipe() != null && !detailsDTO.getEquipe().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getEquipe().size() + " membros da equipe...");
                hydraulicTeamMemberService.saveAll(detailsDTO.getEquipe(), phaseId, hydraulic);
                System.out.println("  ✓ Equipe salva!");
            } else {
                System.out.println("  ⊘ Equipe vazia ou null");
            }

            if (detailsDTO.getServicos() != null && !detailsDTO.getServicos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getServicos().size() + " serviços executados...");
                hydraulicExecutedServiceService.saveAll(detailsDTO.getServicos(), phaseId, hydraulic);
                System.out.println("  ✓ Serviços salvos!");
            }

            if (detailsDTO.getMaquinarios() != null && !detailsDTO.getMaquinarios().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMaquinarios().size() + " maquinários...");
                hydraulicMachineryService.saveAll(detailsDTO.getMaquinarios(), phaseId, hydraulic);
                System.out.println("  ✓ Maquinários salvos!");
            }

            if (detailsDTO.getMateriais() != null && !detailsDTO.getMateriais().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMateriais().size() + " materiais...");
                hydraulicMaterialService.saveAll(detailsDTO.getMateriais(), phaseId, hydraulic);
                System.out.println("  ✓ Materiais salvos!");
            }

            if (detailsDTO.getFerramentas() != null && !detailsDTO.getFerramentas().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFerramentas().size() + " ferramentas...");
                hydraulicToolService.saveAll(detailsDTO.getFerramentas(), phaseId, hydraulic);
                System.out.println("  ✓ Ferramentas salvas!");
            }

            if (detailsDTO.getFotos() != null && !detailsDTO.getFotos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFotos().size() + " fotos...");
                hydraulicPhotoRecordService.saveAll(detailsDTO.getFotos(), phaseId, hydraulic);
                System.out.println("  ✓ Fotos salvas!");
            }

            System.out.println("--- FIM saveAllPhaseDetails\n");
        } catch (Exception e) {
            System.err.println("ERRO em saveAllPhaseDetails: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Optional<Hydraulic> getHydraulicById(String id) {
        return hydraulicRepository.findByIdOptional(id);
    }

    public Optional<Hydraulic> getHydrauliconByCustomerId(String customerId) {
        return hydraulicRepository.find("userId", customerId).firstResultOptional();
    }
}
