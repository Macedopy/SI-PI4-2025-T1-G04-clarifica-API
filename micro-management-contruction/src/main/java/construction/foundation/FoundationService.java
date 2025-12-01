package construction.foundation;

import java.util.Optional;

import construction.eletric.Eletric;
import construction.foundation.entity_external.FoundationExecutedService;
import construction.foundation.entity_external.FoundationExecutedServiceService;
import construction.foundation.entity_external.FoundationMachinery;
import construction.foundation.entity_external.FoundationMachineryService;
import construction.foundation.entity_external.FoundationMaterial;
import construction.foundation.entity_external.FoundationMaterialService;
import construction.foundation.entity_external.FoundationPhotoRecord;
import construction.foundation.entity_external.FoundationPhotoRecordService;
import construction.foundation.entity_external.FoundationTeamMember;
import construction.foundation.entity_external.FoundationTeamMemberService;
import construction.foundation.entity_external.FoundationTool;
import construction.foundation.entity_external.FoundationToolService;
import construction.user.User;
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
        if (foundation.getName() == null || foundation.getName().isBlank()) {
            throw new IllegalArgumentException("Phase name is mandatory.");
        }
        foundationRepository.persist(foundation);
    }
    
    @Transactional
    public String updateFoundation(String userId, FoundationDTO dto) {
        User user = User.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found for ID: " + userId);
        }

        Foundation foundation = user.getFoundation();
        if (foundation == null) {
            throw new NotFoundException("Foundation not found for User ID: " + userId + ". Cannot perform update.");
        }
        
        if (dto.getPhaseName() != null && !dto.getPhaseName().isBlank()) {
            foundation.setName(dto.getPhaseName());
        }
        if (dto.getContractor() != null) {
            foundation.setContractor(dto.getContractor());
        }
        
        deleteAllPhaseDetails(foundation.getId()); 
        
        return foundation.getId();
    }

    @Transactional
    public void deleteAllPhaseDetails(String foundationId) {
        FoundationTeamMember.delete("foundation.id", foundationId); 
        FoundationExecutedService.delete("foundation.id", foundationId);
        FoundationMachinery.delete("foundation.id", foundationId);
        FoundationMaterial.delete("foundation.id", foundationId);
        FoundationTool.delete("foundation.id", foundationId);
        FoundationPhotoRecord.delete("foundation.id", foundationId);
    }

    @Transactional
    public void saveAllPhaseDetails(String phaseId, FoundationDTO dto) {
        Foundation foundation = Foundation.findById(phaseId);
        if (foundation == null) {
             throw new NotFoundException("Foundation not found for ID: " + phaseId);
        }
        
        if (dto.getEquipe() != null && !dto.getEquipe().isEmpty()) {
            foundationTeamMemberService.saveAll(dto.getEquipe(), phaseId, foundation);
        }
        
        if (dto.getServicos() != null && !dto.getServicos().isEmpty()) {
            foundationExecutedServiceService.saveAll(dto.getServicos(), phaseId);
        }
        
        if (dto.getMaquinarios() != null && !dto.getMaquinarios().isEmpty()) {
            foundationMachineryService.saveAll(dto.getMaquinarios(), phaseId);
        }
        
        if (dto.getMateriais() != null && !dto.getMateriais().isEmpty()) {
            foundationMaterialService.saveAll(dto.getMateriais(), phaseId);
        }
        
        if (dto.getFerramentas() != null && !dto.getFerramentas().isEmpty()) {
            foundationToolService.saveAll(dto.getFerramentas(), phaseId);
        }
        
        if (dto.getFotos() != null && !dto.getFotos().isEmpty()) {
            foundationPhotoRecordService.saveAll(dto.getFotos(), phaseId);
        }
    }

    public Optional<Foundation> getFoundationById(String id) {
        return foundationRepository.findByIdOptional(id);
    }

    public Optional<Foundation> getFoundationByCustomerId(String customerId) {
        return foundationRepository.find("userId", customerId).firstResultOptional();
    }
}
