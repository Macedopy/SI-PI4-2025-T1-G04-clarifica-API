package construction.terrain_preparation;

import java.util.Optional;

import construction.terrain_preparation.entity_external.*;
import construction.user.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class TerrainPreparationService {
    
    @Inject TerrainPreparationRepository terrainPreparationRepository;
    @Inject TerrainPreparationGeneralInformationService generalInformationService;
    @Inject TerrainPreparationMaterialService terrainPreparationMaterialService;
    @Inject TerrainPreparationMachineryService terrainPreparationMachineryService;
    @Inject TerrainPreparationTeamMemberService terrainPreparationTeamMemberService;
    @Inject TerrainPreparationExecutedServiceService terrainPreparationExecutedServiceService;
    @Inject TerrainPreparationPhotoRecordService terrainPreparationPhotoRecordService;
    
    @Transactional
    public void saveTerrainPreparation(TerrainPreparation terrainPreparation) {
        if (terrainPreparation.getName() == null || terrainPreparation.getName().isBlank()) {
            throw new IllegalArgumentException("Phase name is mandatory.");
        }
        terrainPreparationRepository.persist(terrainPreparation);
    }
    
    @Transactional
    public String updateTerrainPreparation(String userId, TerrainPreparationDTO dto) {
        User user = User.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found for ID: " + userId);
        }

        TerrainPreparation terrainPreparation = user.getTerrainPreparation();
        if (terrainPreparation == null) {
            throw new NotFoundException("TerrainPreparation not found for User ID: " + userId + ". Cannot perform update.");
        }
        
        if (dto.getPhaseName() != null && !dto.getPhaseName().isBlank()) {
            terrainPreparation.setName(dto.getPhaseName());
        }
        if (dto.getContractor() != null) {
            terrainPreparation.setContractor(dto.getContractor());
        }
        
        deleteAllPhaseDetails(terrainPreparation.getId());
        
        return terrainPreparation.getId();
    }

    @Transactional
    public void deleteAllPhaseDetails(String terrainPreparationId) {
        TerrainPreparationGeneralInformation.delete("terrainPreparation.id", terrainPreparationId);
        TerrainPreparationTeamMember.delete("terrainPreparation.id", terrainPreparationId);
        TerrainPreparationExecutedService.delete("terrainPreparation.id", terrainPreparationId);
        TerrainPreparationMachinery.delete("terrainPreparation.id", terrainPreparationId);
        TerrainPreparationMaterial.delete("terrainPreparation.id", terrainPreparationId);
        TerrainPreparationPhotoRecord.delete("terrainPreparation.id", terrainPreparationId);
    }

    @Transactional
    public void saveAllPhaseDetails(String phaseId, TerrainPreparationDTO detailsDTO) {
        TerrainPreparation terrainPreparation = TerrainPreparation.findById(phaseId);
        if (terrainPreparation == null) {
            throw new NotFoundException("TerrainPreparation not found for ID: " + phaseId);
        }
        
        try {
            System.out.println("\n--- INICIANDO saveAllPhaseDetails (TerrainPreparation) para phaseId: " + phaseId);
            
            // ✅ Salva informações gerais como uma entidade de detalhe
            if (detailsDTO.getGeral() != null) {
                System.out.println("  → Salvando informações gerais...");
                generalInformationService.save(detailsDTO.getGeral(), phaseId, terrainPreparation);
                System.out.println("  ✓ Informações gerais salvas!");
            }
            
            if (detailsDTO.getEquipe() != null && !detailsDTO.getEquipe().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getEquipe().size() + " membros da equipe...");
                terrainPreparationTeamMemberService.saveAll(detailsDTO.getEquipe(), phaseId, terrainPreparation);
                System.out.println("  ✓ Equipe salva!");
            }

            if (detailsDTO.getServicos() != null && !detailsDTO.getServicos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getServicos().size() + " serviços executados...");
                terrainPreparationExecutedServiceService.saveAll(detailsDTO.getServicos(), phaseId, terrainPreparation);
                System.out.println("  ✓ Serviços salvos!");
            }

            if (detailsDTO.getMaquinarios() != null && !detailsDTO.getMaquinarios().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMaquinarios().size() + " maquinários...");
                terrainPreparationMachineryService.saveAll(detailsDTO.getMaquinarios(), phaseId, terrainPreparation);
                System.out.println("  ✓ Maquinários salvos!");
            }

            if (detailsDTO.getMateriais() != null && !detailsDTO.getMateriais().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMateriais().size() + " materiais...");
                terrainPreparationMaterialService.saveAll(detailsDTO.getMateriais(), phaseId, terrainPreparation);
                System.out.println("  ✓ Materiais salvos!");
            }
            
            if (detailsDTO.getFotos() != null && !detailsDTO.getFotos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFotos().size() + " fotos...");
                terrainPreparationPhotoRecordService.saveAll(detailsDTO.getFotos(), phaseId, terrainPreparation);
                System.out.println("  ✓ Fotos salvas!");
            }
            
            System.out.println("--- FIM saveAllPhaseDetails (TerrainPreparation)\n");
        } catch (Exception e) {
            System.err.println("ERRO em saveAllPhaseDetails (TerrainPreparation): " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Optional<TerrainPreparation> getTerrainPreparationById(String id) {
        return terrainPreparationRepository.findByIdOptional(id);
    }

    public Optional<TerrainPreparation> getTerrainPreparationByCustomerId(String customerId) {
        return terrainPreparationRepository.find("user.id", customerId).firstResultOptional();
    }

}
