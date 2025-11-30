package construction.structure.entity_external;

import construction.components.team_present.MemberStatus;
import construction.components.team_present.TeamMemberDTO;
import construction.components.team_present.TeamMemberDetails;
import construction.structure.Structure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class StructureTeamMemberService {
    
    @Inject
    StructureTeamMemberRepository teamMemberRepository;

    private StructureTeamMember mapToEntity(TeamMemberDTO dto, String phaseId, Structure structure) {
        StructureTeamMember entity = new StructureTeamMember();
        
        // ✅ Gera novo ID sempre
        entity.setId(UUID.randomUUID().toString());
        
        entity.setPhaseId(phaseId);
        entity.setStructure(structure);
        
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
    public void saveAll(List<TeamMemberDTO> dtos, String phaseId, Structure structure) {
        if (dtos == null || dtos.isEmpty()) return;

        List<StructureTeamMember> entities = dtos.stream()
            .map(dto -> mapToEntity(dto, phaseId, structure))
            .collect(Collectors.toList());

        StructureTeamMember.persist(entities);
    }
}
