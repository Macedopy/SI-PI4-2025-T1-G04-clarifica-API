package construction.components.team_present;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class TeamMemberService {

    @Inject
    TeamMemberRepository teamMemberRepository;

    @Transactional
    public void saveMembers(List<TeamMember> members) {
        teamMemberRepository.persist(members);
    }

    public List<TeamMember> findByPhaseId(String phaseId) {
        return teamMemberRepository.list("phaseId", phaseId);
    }
}
