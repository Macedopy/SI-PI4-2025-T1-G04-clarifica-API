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
public class MaterialPurchaseService {

    @Inject
    MaterialPurchaseRepository repository;

    @Inject
    UserRepository userRepository;

    @Transactional
    public MaterialPurchase create(String phaseId, String materialId, double quantity,
                                   double unitPrice, String purchasedById, String supplier, String notes) {
        Phase phase = Phase.findById(phaseId);
        if (phase == null) {
            throw new NotFoundException("Phase not found: " + phaseId);
        }

        User purchasedBy = userRepository.findById(purchasedById);
        if (purchasedBy == null) {
            throw new NotFoundException("User not found: " + purchasedById);
        }

        MaterialPurchase purchase = new MaterialPurchase();
        purchase.setPhase(phase);
        purchase.setPhaseId(phaseId);
        purchase.setMaterialId(materialId);
        purchase.setQuantity(quantity);
        purchase.setUnitPrice(unitPrice);
        purchase.setTotalPrice(quantity * unitPrice);
        purchase.setPurchasedBy(purchasedBy);
        purchase.setPurchasedById(purchasedById);
        purchase.setSupplier(supplier);
        purchase.setNotes(notes);

        repository.persist(purchase);
        return purchase;
    }

    public List<MaterialPurchase> listByPhase(String phaseId) {
        return repository.findByPhase(phaseId);
    }

    public double getTotalSpentByPhase(String phaseId) {
        List<MaterialPurchase> purchases = repository.findByPhase(phaseId);
        return purchases.stream().mapToDouble(MaterialPurchase::getTotalPrice).sum();
    }
}
