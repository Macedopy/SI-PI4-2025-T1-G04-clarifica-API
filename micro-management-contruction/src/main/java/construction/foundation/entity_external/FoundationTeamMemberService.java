package construction.foundation.entity_external;

import construction.components.team_present.MemberStatus;
import construction.components.team_present.TeamMemberDTO;
import construction.components.team_present.TeamMemberDetails;
import construction.foundation.Foundation;
import construction.foundation.FoundationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FoundationTeamMemberService {
    
    @Inject
    FoundationTeamMemberRepository teamMemberRepository;

    @Inject
    FoundationRepository foundationRepository;

    @Transactional
    public void saveAll(List<TeamMemberDTO> memberDTOs, String phaseId) {
        
        if (memberDTOs == null || memberDTOs.isEmpty()) {
            return;
        }

        Foundation foundation = foundationRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Foundation não encontrada com ID: " + phaseId));

        for (TeamMemberDTO dto : memberDTOs) {
            FoundationTeamMember entity = mapDtoToEntity(dto);
            entity.setId(UUID.randomUUID().toString()); 
            entity.setPhaseId(phaseId);
            entity.setFoundation(foundation);
            
            System.out.println("Persistindo membro: " + entity.getId() + " - " + entity.getDetails().getName());
            teamMemberRepository.persist(entity);
        }
    }

    private FoundationTeamMember mapDtoToEntity(TeamMemberDTO dto) {
        FoundationTeamMember entity = new FoundationTeamMember();
        TeamMemberDetails details = new TeamMemberDetails();
        
        String name = dto.getName();
        if (name == null || name.trim().isEmpty()) {
            name = "Membro sem identificação";
        }
        details.setName(name.trim());
        details.setRole(dto.getRole() != null ? dto.getRole().trim() : "Não informado");
        details.setCpf(dto.getCpf());

        entity.setDetails(details); 
        entity.setHoursWorked(dto.getHoursWorked());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : MemberStatus.PRESENT);
        entity.setNotes(dto.getNotes());
        
        return entity;
    }
}
