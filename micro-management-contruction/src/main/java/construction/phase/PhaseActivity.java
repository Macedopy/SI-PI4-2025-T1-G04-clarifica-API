package construction.phase;

import construction.core.Phase;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "phase_activities")
public class PhaseActivity extends PanacheEntityBase {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Phase phase;

    @Column(name = "phase_id", insertable = false, updatable = false)
    private String phaseId;

    @Column(name = "activity_id", nullable = false)
    private String activityId;

    @Column(name = "name_key", nullable = false)
    private String nameKey;

    @Column(name = "cost_category_id")
    private String costCategoryId;

    @Column(name = "status", nullable = false)
    private String status = "not_started";

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "is_optional", nullable = false)
    private boolean isOptional = false;

    @Column(name = "budget_amount", nullable = false)
    private java.math.BigDecimal budgetAmount = java.math.BigDecimal.ZERO;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public PhaseActivity() {
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

    public String getActivityId() { return activityId; }
    public void setActivityId(String activityId) { this.activityId = activityId; }

    public String getNameKey() { return nameKey; }
    public void setNameKey(String nameKey) { this.nameKey = nameKey; }

    public String getCostCategoryId() { return costCategoryId; }
    public void setCostCategoryId(String costCategoryId) { this.costCategoryId = costCategoryId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getSequence() { return sequence; }
    public void setSequence(Integer sequence) { this.sequence = sequence; }

    public boolean isOptional() { return isOptional; }
    public void setOptional(boolean isOptional) { this.isOptional = isOptional; }

    public java.math.BigDecimal getBudgetAmount() { return budgetAmount; }
    public void setBudgetAmount(java.math.BigDecimal budgetAmount) { this.budgetAmount = budgetAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
