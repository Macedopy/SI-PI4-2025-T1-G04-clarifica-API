package construction.cost;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class MachineryMaintenanceLogRepository implements PanacheRepositoryBase<MachineryMaintenanceLog, String> {

    public List<MachineryMaintenanceLog> findByMachinery(String machineryId) {
        return find("machineryId = ?1 ORDER BY maintenanceDate DESC, createdAt DESC", machineryId).list();
    }

    public List<MachineryMaintenanceLog> findByPhase(String phaseId) {
        return find("phaseId = ?1 ORDER BY maintenanceDate DESC, createdAt DESC", phaseId).list();
    }

    public List<MachineryMaintenanceLog> findByDateRange(LocalDate start, LocalDate end) {
        return find("maintenanceDate BETWEEN ?1 AND ?2 ORDER BY maintenanceDate DESC", start, end).list();
    }
}
