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
import java.util.stream.Collectors;

@ApplicationScoped
public class FoundationTeamMemberService {
    
    @Inject
    FoundationTeamMemberRepository teamMemberRepository;

    @Inject
    FoundationRepository foundationRepository;

    private FoundationTeamMember mapToEntity(TeamMemberDTO dto, String phaseId, Foundation foundation) {
        FoundationTeamMember entity = new FoundationTeamMember();

        entity.setId(UUID.randomUUID().toString());

        entity.setPhaseId(phaseId);
        entity.setFoundation(foundation);
        
        if (entity.getDetails() == null) {
            entity.setDetails(new TeamMemberDetails());
        }

        entity.getDetails().setName(dto.getName());
        entity.getDetails().setRole(dto.getRole());
        entity.setHoursWorked(dto.getHoursWorked());
        entity.setStatus(dto.getStatus() != null ? (dto.getStatus()) : MemberStatus.ON_LEAVE);
        
        return entity;
    }

    @Transactional
    public void saveAll(List<TeamMemberDTO> dtos, String phaseId, Foundation foundation) {
        if (dtos == null) return;

        List<FoundationTeamMember> entities = dtos.stream()
            .map(dto -> mapToEntity(dto, phaseId, foundation))
            .collect(Collectors.toList());

        FoundationTeamMember.persist(entities);
    }
}
