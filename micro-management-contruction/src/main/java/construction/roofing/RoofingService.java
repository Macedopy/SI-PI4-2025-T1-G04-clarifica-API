package construction.roofing;

import java.util.Optional;

import construction.roofing.entity_external.*;
import construction.structure.Structure;
import construction.user.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class RoofingService {
    
    @Inject RoofingRepository roofingRepository;
    @Inject RoofingMaterialService roofingMaterialService;
    @Inject RoofingToolService roofingToolService;
    @Inject RoofingMachineryService roofingMachineryService;
    @Inject RoofingTeamMemberService roofingTeamMemberService;
    @Inject RoofingExecutedServiceService roofingExecutedServiceService;
    @Inject RoofingPhotoRecordService roofingPhotoRecordService;
    
    @Transactional
    public void saveRoofing(Roofing roofing) {
        if (roofing.getName() == null || roofing.getName().isBlank()) {
            throw new IllegalArgumentException("Phase name is mandatory.");
        }
        roofingRepository.persist(roofing);
    }
    
    @Transactional
    public String updateRoofing(String userId, RoofingDTO dto) {
        User user = User.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found for ID: " + userId);
        }

        Roofing roofing = user.getRoofing();
        if (roofing == null) {
            throw new NotFoundException("Roofing not found for User ID: " + userId + ". Cannot perform update.");
        }
        
        if (dto.getPhaseName() != null && !dto.getPhaseName().isBlank()) {
            roofing.setName(dto.getPhaseName());
        }
        if (dto.getContractor() != null) {
            roofing.setContractor(dto.getContractor());
        }
        
        deleteAllPhaseDetails(roofing.getId());
        
        return roofing.getId();
    }

    @Transactional
    public void deleteAllPhaseDetails(String roofingId) {
        RoofingTeamMember.delete("roofing.id", roofingId);
        RoofingExecutedService.delete("roofing.id", roofingId);
        RoofingMachinery.delete("roofing.id", roofingId);
        RoofingMaterial.delete("roofing.id", roofingId);
        RoofingTool.delete("roofing.id", roofingId);
        RoofingPhotoRecord.delete("roofing.id", roofingId);
    }

    @Transactional
    public void saveAllPhaseDetails(String phaseId, RoofingDTO detailsDTO) {
        Roofing roofing = Roofing.findById(phaseId);
        if (roofing == null) {
            throw new NotFoundException("Roofing not found for ID: " + phaseId);
        }
        
        if (detailsDTO.getEquipe() != null && !detailsDTO.getEquipe().isEmpty()) {
            roofingTeamMemberService.saveAll(detailsDTO.getEquipe(), phaseId, roofing);
        }
        
        if (detailsDTO.getServicos() != null && !detailsDTO.getServicos().isEmpty()) {
            roofingExecutedServiceService.saveAll(detailsDTO.getServicos(), phaseId, roofing);
        }
        
        if (detailsDTO.getMaquinarios() != null && !detailsDTO.getMaquinarios().isEmpty()) {
            roofingMachineryService.saveAll(detailsDTO.getMaquinarios(), phaseId, roofing);
        }
        
        if (detailsDTO.getMateriais() != null && !detailsDTO.getMateriais().isEmpty()) {
            roofingMaterialService.saveAll(detailsDTO.getMateriais(), phaseId, roofing);
        }
        
        if (detailsDTO.getFerramentas() != null && !detailsDTO.getFerramentas().isEmpty()) {
            roofingToolService.saveAll(detailsDTO.getFerramentas(), phaseId, roofing);
        }
        
        if (detailsDTO.getFotos() != null && !detailsDTO.getFotos().isEmpty()) {
            roofingPhotoRecordService.saveAll(detailsDTO.getFotos(), phaseId, roofing);
        }
    }

    public Optional<Roofing> getRoofingById(String id) {
        return roofingRepository.findByIdOptional(id);
    }

    public Optional<Roofing> getRoofingByCustomerId(String customerId) {
        return roofingRepository.find("userId", customerId).firstResultOptional();
    }
}
