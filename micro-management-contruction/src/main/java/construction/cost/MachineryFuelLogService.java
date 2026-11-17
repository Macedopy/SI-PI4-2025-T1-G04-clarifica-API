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
public class MachineryFuelLogService {

    @Inject
    MachineryFuelLogRepository repository;

    @Inject
    UserRepository userRepository;

    @Transactional
    public MachineryFuelLog create(String machineryId, String phaseId, LocalDate logDate,
                                   double liters, double pricePerLiter, String recordedById, String notes) {
        Phase phase = Phase.findById(phaseId);
        if (phase == null) {
            throw new NotFoundException("Phase not found: " + phaseId);
        }

        User recordedBy = userRepository.findById(recordedById);
        if (recordedBy == null) {
            throw new NotFoundException("User not found: " + recordedById);
        }

        MachineryFuelLog log = new MachineryFuelLog();
        log.setMachineryId(machineryId);
        log.setPhase(phase);
        log.setPhaseId(phaseId);
        log.setLogDate(logDate);
        log.setLiters(liters);
        log.setPricePerLiter(pricePerLiter);
        log.setTotalCost(liters * pricePerLiter);
        log.setRecordedBy(recordedBy);
        log.setRecordedById(recordedById);
        log.setNotes(notes);

        repository.persist(log);
        return log;
    }

    public List<MachineryFuelLog> listByPhase(String phaseId) {
        return repository.findByPhase(phaseId);
    }

    public double getTotalFuelCostByPhase(String phaseId) {
        List<MachineryFuelLog> logs = repository.findByPhase(phaseId);
        return logs.stream().mapToDouble(MachineryFuelLog::getTotalCost).sum();
    }
}
