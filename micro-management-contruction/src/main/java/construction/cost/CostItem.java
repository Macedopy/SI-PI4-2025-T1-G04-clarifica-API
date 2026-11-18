package construction.cost;

import construction.core.Phase;
import construction.phase.PhaseActivity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cost_items")
public class CostItem extends PanacheEntityBase {

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Phase phase;

    @Column(name = "phase_id", insertable = false, updatable = false)
    private String phaseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private PhaseActivity activity;

    @Column(name = "activity_id", insertable = false, updatable = false)
    private String activityId;

    @Column(nullable = false)
    private String description;

    @Column(name = "vendor_id")
    private String vendorId;

    @Column(name = "budget_amount", nullable = false)
    private BigDecimal budgetAmount = BigDecimal.ZERO;

    @Column(name = "committed_amount", nullable = false)
    private BigDecimal committedAmount = BigDecimal.ZERO;

    @Column(name = "invoiced_amount", nullable = false)
    private BigDecimal invoicedAmount = BigDecimal.ZERO;

    @Column(name = "actual_cost_amount", nullable = false)
    private BigDecimal actualCostAmount = BigDecimal.ZERO;

    @Column(name = "paid_amount", nullable = false)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "retained_amount", nullable = false)
    private BigDecimal retainedAmount = BigDecimal.ZERO;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public CostItem() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Phase getPhase() { return phase; }
    public void setPhase(Phase phase) { this.phase = phase; }

    public String getPhaseId() { return phaseId; }
    public void setPhaseId(String phaseId) { this.phaseId = phaseId; }

    public PhaseActivity getActivity() { return activity; }
    public void setActivity(PhaseActivity activity) { this.activity = activity; }

    public String getActivityId() { return activityId; }
    public void setActivityId(String activityId) { this.activityId = activityId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }

    public BigDecimal getBudgetAmount() { return budgetAmount; }
    public void setBudgetAmount(BigDecimal budgetAmount) { this.budgetAmount = budgetAmount; }

    public BigDecimal getCommittedAmount() { return committedAmount; }
    public void setCommittedAmount(BigDecimal committedAmount) { this.committedAmount = committedAmount; }

    public BigDecimal getInvoicedAmount() { return invoicedAmount; }
    public void setInvoicedAmount(BigDecimal invoicedAmount) { this.invoicedAmount = invoicedAmount; }

    public BigDecimal getActualCostAmount() { return actualCostAmount; }
    public void setActualCostAmount(BigDecimal actualCostAmount) { this.actualCostAmount = actualCostAmount; }

    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }

    public BigDecimal getRetainedAmount() { return retainedAmount; }
    public void setRetainedAmount(BigDecimal retainedAmount) { this.retainedAmount = retainedAmount; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
