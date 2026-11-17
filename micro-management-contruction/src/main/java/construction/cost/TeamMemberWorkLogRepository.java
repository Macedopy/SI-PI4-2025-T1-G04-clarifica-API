package construction.cost;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class TeamMemberWorkLogRepository implements PanacheRepositoryBase<TeamMemberWorkLog, String> {

    public List<TeamMemberWorkLog> findByTeamMember(String teamMemberId) {
        return find("teamMemberId = ?1 ORDER BY workDate DESC, createdAt DESC", teamMemberId).list();
    }

    public List<TeamMemberWorkLog> findByPhase(String phaseId) {
        return find("phaseId = ?1 ORDER BY workDate DESC, createdAt DESC", phaseId).list();
    }

    public List<TeamMemberWorkLog> findByDateRange(String phaseId, LocalDate start, LocalDate end) {
        return find("phaseId = ?1 AND workDate BETWEEN ?2 AND ?3 ORDER BY workDate DESC",
                phaseId, start, end).list();
    }

    public List<TeamMemberWorkLog> findByAddedBy(String userId) {
        return find("addedById = ?1 ORDER BY workDate DESC", userId).list();
    }
}
