package construction.cost;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class MaterialPurchaseRepository implements PanacheRepositoryBase<MaterialPurchase, String> {

    public List<MaterialPurchase> findByPhase(String phaseId) {
        return find("phaseId = ?1 ORDER BY purchaseDate DESC, createdAt DESC", phaseId).list();
    }

    public List<MaterialPurchase> findByDateRange(String phaseId, LocalDate start, LocalDate end) {
        return find("phaseId = ?1 AND purchaseDate BETWEEN ?2 AND ?3 ORDER BY purchaseDate DESC",
                phaseId, start, end).list();
    }

    public List<MaterialPurchase> findByPurchasedBy(String userId) {
        return find("purchasedById = ?1 ORDER BY purchaseDate DESC", userId).list();
    }
}
