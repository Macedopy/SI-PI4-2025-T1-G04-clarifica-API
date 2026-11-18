package construction.core;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "change_orders")
public class ChangeOrder extends PanacheEntityBase {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "construction_id", nullable = false)
    private String constructionId;

    @Column(name = "phase_id")
    private String phaseId;

    @Column(nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price_change", nullable = false)
    private BigDecimal priceChange = BigDecimal.ZERO;

    @Column(name = "days_change", nullable = false)
    private Integer daysChange = 0;

    @Column(nullable = false)
    private String status = "pending";

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public ChangeOrder() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getConstructionId() { return constructionId; }
    public void setConstructionId(String constructionId) { this.constructionId = constructionId; }

    public String getPhaseId() { return phaseId; }
    public void setPhaseId(String phaseId) { this.phaseId = phaseId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPriceChange() { return priceChange; }
    public void setPriceChange(BigDecimal priceChange) { this.priceChange = priceChange; }

    public Integer getDaysChange() { return daysChange; }
    public void setDaysChange(Integer daysChange) { this.daysChange = daysChange; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
