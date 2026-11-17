package construction.cost;

import construction.core.Phase;
import construction.user.User;
import construction.user.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class TeamMemberWorkLogService {

    @Inject
    TeamMemberWorkLogRepository repository;

    @Inject
    UserRepository userRepository;

    @Transactional
    public TeamMemberWorkLog create(String teamMemberId, String phaseId, LocalDate workDate,
                                    double hoursWorked, double hourlyRate, String addedById, String notes) {
        Phase phase = Phase.findById(phaseId);
        if (phase == null) {
            throw new NotFoundException("Phase not found: " + phaseId);
        }

        User addedBy = userRepository.findById(addedById);
        if (addedBy == null) {
            throw new NotFoundException("User not found: " + addedById);
        }

        TeamMemberWorkLog log = new TeamMemberWorkLog();
        log.setTeamMemberId(teamMemberId);
        log.setPhase(phase);
        log.setPhaseId(phaseId);
        log.setWorkDate(workDate);
        log.setHoursWorked(hoursWorked);
        log.setHourlyRate(hourlyRate);
        log.setTotalCost(hoursWorked * hourlyRate);
        log.setAddedBy(addedBy);
        log.setAddedById(addedById);
        log.setNotes(notes);

        repository.persist(log);
        return log;
    }

    public List<TeamMemberWorkLog> listByPhase(String phaseId) {
        return repository.findByPhase(phaseId);
    }

    public double getTotalLaborCostByPhase(String phaseId) {
        List<TeamMemberWorkLog> logs = repository.findByPhase(phaseId);
        return logs.stream().mapToDouble(TeamMemberWorkLog::getTotalCost).sum();
    }
}
