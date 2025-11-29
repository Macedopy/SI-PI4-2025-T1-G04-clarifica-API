package construction.roofing.entity_external;

import construction.components.team_present.MemberStatus;
import construction.components.team_present.TeamMemberDTO;
import construction.components.team_present.TeamMemberDetails;
import construction.roofing.Roofing; // Assumindo existência
import construction.roofing.RoofingRepository; // Assumindo existência
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RoofingTeamMemberService {
    
    @Inject
    RoofingTeamMemberRepository teamMemberRepository;

    @Inject
    RoofingRepository roofingRepository;

    @Transactional
    public void saveAll(List<TeamMemberDTO> memberDTOs, String phaseId) {
        
        if (memberDTOs == null || memberDTOs.isEmpty()) {
            return;
        }

        // Busca a entidade pai (Roofing)
        Roofing roofing = roofingRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Roofing não encontrado com ID: " + phaseId));

        for (TeamMemberDTO dto : memberDTOs) {
            RoofingTeamMember entity = mapDtoToEntity(dto);
            entity.setId(UUID.randomUUID().toString()); 
            entity.setPhaseId(phaseId);
            entity.setRoofing(roofing);
            
            System.out.println("Persistindo membro (Roofing): " + entity.getId() + " - " + entity.getDetails().getName());
            teamMemberRepository.persist(entity);
        }
    }

    private RoofingTeamMember mapDtoToEntity(TeamMemberDTO dto) {
        RoofingTeamMember entity = new RoofingTeamMember();
        TeamMemberDetails details = new TeamMemberDetails();
        
        // Lógica manual de validação/padrão idêntica ao Structure
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