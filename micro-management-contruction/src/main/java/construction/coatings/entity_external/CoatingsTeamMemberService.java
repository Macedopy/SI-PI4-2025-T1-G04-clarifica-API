package construction.coatings.entity_external;

import construction.coatings.Coatings;
import construction.components.team_present.MemberStatus;
import construction.components.team_present.TeamMemberDTO;
import construction.components.team_present.TeamMemberDetails;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class CoatingsTeamMemberService {
    
    @Inject
    CoatingsTeamMemberRepository teamMemberRepository;

    private CoatingsTeamMember mapToEntity(TeamMemberDTO dto, String phaseId, Coatings coatings) {
        CoatingsTeamMember entity = new CoatingsTeamMember();
        
        entity.setId(UUID.randomUUID().toString());
        entity.setPhaseId(phaseId);
        entity.setCoatings(coatings);
        
        if (entity.getDetails() == null) {
            entity.setDetails(new TeamMemberDetails());
        }

        String name = dto.getName();
        if (name == null || name.trim().isEmpty()) {
            name = "Membro sem identificação";
        }
        entity.getDetails().setName(name.trim());
        entity.getDetails().setRole(dto.getRole() != null ? dto.getRole().trim() : "Não informado");
        entity.getDetails().setCpf(dto.getCpf());

        entity.setHoursWorked(dto.getHoursWorked());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : MemberStatus.PRESENT);
        entity.setNotes(dto.getNotes());
        
        return entity;
    }

    @Transactional
    public void saveAll(List<TeamMemberDTO> dtos, String phaseId, Coatings coatings) {
        if (dtos == null || dtos.isEmpty()) return;

        List<CoatingsTeamMember> entities = dtos.stream()
            .map(dto -> mapToEntity(dto, phaseId, coatings))
            .collect(Collectors.toList());

        CoatingsTeamMember.persist(entities);
    }
}
