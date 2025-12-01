package construction.eletric.entity_external;

import construction.components.team_present.MemberStatus;
import construction.components.team_present.TeamMemberDTO;
import construction.components.team_present.TeamMemberDetails;
import construction.eletric.Eletric;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class EletricTeamMemberService {
    
    @Inject
    EletricTeamMemberRepository teamMemberRepository;

    private EletricTeamMember mapToEntity(TeamMemberDTO dto, String phaseId, Eletric eletric) {
        EletricTeamMember entity = new EletricTeamMember();
        
        entity.setId(UUID.randomUUID().toString());
        entity.setPhaseId(phaseId);
        entity.setEletric(eletric);
        
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
    public void saveAll(List<TeamMemberDTO> dtos, String phaseId, Eletric eletric) {
        if (dtos == null || dtos.isEmpty()) return;

        List<EletricTeamMember> entities = dtos.stream()
            .map(dto -> mapToEntity(dto, phaseId, eletric))
            .collect(Collectors.toList());

        EletricTeamMember.persist(entities);
    }
}
