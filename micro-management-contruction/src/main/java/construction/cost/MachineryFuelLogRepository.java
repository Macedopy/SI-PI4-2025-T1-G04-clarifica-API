package construction.cost;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class MachineryFuelLogRepository implements PanacheRepositoryBase<MachineryFuelLog, String> {

    public List<MachineryFuelLog> findByMachinery(String machineryId) {
        return find("machineryId = ?1 ORDER BY logDate DESC, createdAt DESC", machineryId).list();
    }

    public List<MachineryFuelLog> findByPhase(String phaseId) {
        return find("phaseId = ?1 ORDER BY logDate DESC, createdAt DESC", phaseId).list();
    }

    public List<MachineryFuelLog> findByDateRange(LocalDate start, LocalDate end) {
        return find("logDate BETWEEN ?1 AND ?2 ORDER BY logDate DESC", start, end).list();
    }
}
