package construction.masonry.entity_external;

import construction.components.team_present.MemberStatus;
import construction.components.team_present.TeamMemberDTO;
import construction.components.team_present.TeamMemberDetails;
import construction.masonry.Masonry;
import construction.masonry.MasonryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class MasonryTeamMemberService {
    
    @Inject
    MasonryTeamMemberRepository teamMemberRepository;

    @Inject
    MasonryRepository masonryRepository;

    @Transactional
    public void saveAll(List<TeamMemberDTO> memberDTOs, String phaseId) {
        
        if (memberDTOs == null || memberDTOs.isEmpty()) {
            return;
        }

        Masonry masonry = masonryRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Masonry não encontrada com ID: " + phaseId));

        for (TeamMemberDTO dto : memberDTOs) {
            MasonryTeamMember entity = mapDtoToEntity(dto);
            entity.setId(UUID.randomUUID().toString()); 
            entity.setPhaseId(phaseId);
            entity.setMasonry(masonry);
            
            System.out.println("Persistindo membro Masonry: " + entity.getId() + " - " + entity.getDetails().getName());
            teamMemberRepository.persist(entity);
        }
    }

    private MasonryTeamMember mapDtoToEntity(TeamMemberDTO dto) {
        MasonryTeamMember entity = new MasonryTeamMember();
        TeamMemberDetails details = new TeamMemberDetails();
        
        // Tratamento de nome para evitar nulo
        String name = dto.getName();
        if (name == null || name.trim().isEmpty()) {
            name = "Membro sem identificação";
        }
        details.setName(name.trim());
        
        details.setRole(dto.getRole() != null ? dto.getRole().trim() : "Não informado");
        details.setCpf(dto.getCpf());

        entity.setDetails(details); 
        entity.setHoursWorked(dto.getHoursWorked());
        
        // Status padrão se vier nulo
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : MemberStatus.PRESENT);
        entity.setNotes(dto.getNotes());
        
        return entity;
    }
}