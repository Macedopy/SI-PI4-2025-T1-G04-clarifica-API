package construction.cost;

import construction.core.Phase;
import construction.user.User;
import construction.user.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class ToolPurchaseService {

    @Inject
    ToolPurchaseRepository repository;

    @Inject
    UserRepository userRepository;

    @Transactional
    public ToolPurchase create(String phaseId, String toolId, int quantity,
                               double unitPrice, String purchasedById, String supplier, String notes) {
        Phase phase = Phase.findById(phaseId);
        if (phase == null) {
            throw new NotFoundException("Phase not found: " + phaseId);
        }

        User purchasedBy = userRepository.findById(purchasedById);
        if (purchasedBy == null) {
            throw new NotFoundException("User not found: " + purchasedById);
        }

        ToolPurchase purchase = new ToolPurchase();
        purchase.setPhase(phase);
        purchase.setPhaseId(phaseId);
        purchase.setToolId(toolId);
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

    public List<ToolPurchase> listByPhase(String phaseId) {
        return repository.findByPhase(phaseId);
    }

    public double getTotalSpentByPhase(String phaseId) {
        List<ToolPurchase> purchases = repository.findByPhase(phaseId);
        return purchases.stream().mapToDouble(ToolPurchase::getTotalPrice).sum();
    }
}
