package construction.cost;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class MachineryRecordRepository implements PanacheRepositoryBase<MachineryRecord, String> {

    public List<MachineryRecord> findByPhase(String phaseId) {
        return find("phaseId = ?1 ORDER BY createdAt DESC", phaseId).list();
    }

    public List<MachineryRecord> findByStatus(String status) {
        return find("status = ?1 ORDER BY createdAt DESC", status).list();
    }

    public List<MachineryRecord> findOwned() {
        return find("isOwned = true ORDER BY createdAt DESC").list();
    }

    public List<MachineryRecord> findRented() {
        return find("isOwned = false ORDER BY createdAt DESC").list();
    }
}
