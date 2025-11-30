package construction.masonry.entity_external;

import construction.components.team_present.MemberStatus;
import construction.components.team_present.TeamMemberDTO;
import construction.components.team_present.TeamMemberDetails;
import construction.masonry.Masonry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class MasonryTeamMemberService {
    
    @Inject
    MasonryTeamMemberRepository teamMemberRepository;

    private MasonryTeamMember mapToEntity(TeamMemberDTO dto, String phaseId, Masonry masonry) {
        MasonryTeamMember entity = new MasonryTeamMember();
        
        // ✅ Gera novo ID sempre
        entity.setId(UUID.randomUUID().toString());
        
        entity.setPhaseId(phaseId);
        entity.setMasonry(masonry);
        
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
    public void saveAll(List<TeamMemberDTO> dtos, String phaseId, Masonry masonry) {
        if (dtos == null || dtos.isEmpty()) return;

        List<MasonryTeamMember> entities = dtos.stream()
            .map(dto -> mapToEntity(dto, phaseId, masonry))
            .collect(Collectors.toList());

        MasonryTeamMember.persist(entities);
    }
}
