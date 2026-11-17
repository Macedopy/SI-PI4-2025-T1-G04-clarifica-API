package construction.cost;

import construction.core.Phase;
import construction.user.User;
import construction.user.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class MachineryRecordService {

    @Inject
    MachineryRecordRepository repository;

    @Inject
    UserRepository userRepository;

    @Transactional
    public MachineryRecord create(String phaseId, String machineryId, boolean isOwned,
                                  String rentalType, double rentalPrice, double currentValue,
                                  String createdById) {
        Phase phase = Phase.findById(phaseId);
        if (phase == null) {
            throw new NotFoundException("Phase not found: " + phaseId);
        }

        MachineryRecord record = new MachineryRecord();
        record.setPhase(phase);
        record.setPhaseId(phaseId);
        record.setMachineryId(machineryId);
        record.setOwned(isOwned);
        record.setRentalType(rentalType);
        record.setRentalPrice(rentalPrice);
        record.setCurrentValue(currentValue);
        record.setStatus("active");

        repository.persist(record);
        return record;
    }

    @Transactional
    public void stopMachinery(String recordId, String stoppedById, String stopReason) {
        MachineryRecord record = repository.findById(recordId);
        if (record == null) {
            throw new NotFoundException("Machinery record not found: " + recordId);
        }

        User stoppedBy = userRepository.findById(stoppedById);
        if (stoppedBy == null) {
            throw new NotFoundException("User not found: " + stoppedById);
        }

        record.setStatus("stopped");
        record.setStopReason(stopReason);
        record.setStoppedBy(stoppedBy);
        record.setStoppedById(stoppedById);
        record.setStoppedAt(LocalDateTime.now());

        repository.persist(record);
    }

    @Transactional
    public void resumeMachinery(String recordId) {
        MachineryRecord record = repository.findById(recordId);
        if (record == null) {
            throw new NotFoundException("Machinery record not found: " + recordId);
        }

        record.setStatus("active");
        record.setStopReason(null);
        record.setStoppedBy(null);
        record.setStoppedById(null);
        record.setStoppedAt(null);

        repository.persist(record);
    }

    public List<MachineryRecord> listByPhase(String phaseId) {
        return repository.findByPhase(phaseId);
    }

    public double getTotalRentalCostByPhase(String phaseId) {
        List<MachineryRecord> records = repository.findByPhase(phaseId);
        return records.stream()
                .filter(r -> !r.isOwned())
                .mapToDouble(MachineryRecord::getRentalPrice)
                .sum();
    }
}
