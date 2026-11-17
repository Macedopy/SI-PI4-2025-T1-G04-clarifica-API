package construction.cost;

import construction.core.Phase;
import construction.user.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tool_purchases")
public class ToolPurchase extends PanacheEntityBase {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id", nullable = false)
    @JsonIgnore
    private Phase phase;

    @Column(name = "phase_id", insertable = false, updatable = false)
    private String phaseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tool_id")
    @JsonIgnore
    private construction.components.tools.Tool tool;

    @Column(name = "tool_id", insertable = false, updatable = false)
    private String toolId;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false)
    private double unitPrice;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchased_by", nullable = false)
    @JsonIgnore
    private User purchasedBy;

    @Column(name = "purchased_by", insertable = false, updatable = false)
    private String purchasedById;

    /** Nome de quem comprou, preenchido na resposta (não persiste). */
    @Transient
    private String purchasedByName;

    @Column(length = 255)
    private String supplier;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public ToolPurchase() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Phase getPhase() { return phase; }
    public void setPhase(Phase phase) { this.phase = phase; }

    public String getPhaseId() { return phaseId; }
    public void setPhaseId(String phaseId) { this.phaseId = phaseId; }

    public construction.components.tools.Tool getTool() { return tool; }
    public void setTool(construction.components.tools.Tool tool) { this.tool = tool; }

    public String getToolId() { return toolId; }
    public void setToolId(String toolId) { this.toolId = toolId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

    public User getPurchasedBy() { return purchasedBy; }
    public void setPurchasedBy(User purchasedBy) { this.purchasedBy = purchasedBy; }

    public String getPurchasedById() { return purchasedById; }
    public void setPurchasedById(String purchasedById) { this.purchasedById = purchasedById; }

    public String getPurchasedByName() { return purchasedByName; }
    public void setPurchasedByName(String purchasedByName) { this.purchasedByName = purchasedByName; }

    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
