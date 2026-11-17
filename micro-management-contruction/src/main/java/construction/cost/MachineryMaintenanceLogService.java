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
public class MachineryMaintenanceLogService {

    @Inject
    MachineryMaintenanceLogRepository repository;

    @Inject
    UserRepository userRepository;

    @Transactional
    public MachineryMaintenanceLog create(String machineryId, String phaseId, LocalDate maintenanceDate,
                                          String description, double cost, String performedById, String notes) {
        Phase phase = Phase.findById(phaseId);
        if (phase == null) {
            throw new NotFoundException("Phase not found: " + phaseId);
        }

        User performedBy = userRepository.findById(performedById);
        if (performedBy == null) {
            throw new NotFoundException("User not found: " + performedById);
        }

        MachineryMaintenanceLog log = new MachineryMaintenanceLog();
        log.setMachineryId(machineryId);
        log.setPhase(phase);
        log.setPhaseId(phaseId);
        log.setMaintenanceDate(maintenanceDate);
        log.setDescription(description);
        log.setCost(cost);
        log.setPerformedBy(performedBy);
        log.setPerformedById(performedById);
        log.setNotes(notes);

        repository.persist(log);
        return log;
    }

    public List<MachineryMaintenanceLog> listByPhase(String phaseId) {
        return repository.findByPhase(phaseId);
    }

    public double getTotalMaintenanceCostByPhase(String phaseId) {
        List<MachineryMaintenanceLog> logs = repository.findByPhase(phaseId);
        return logs.stream().mapToDouble(MachineryMaintenanceLog::getCost).sum();
    }
}
