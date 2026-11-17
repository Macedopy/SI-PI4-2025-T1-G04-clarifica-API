package construction.cost;

import construction.core.Phase;
import construction.user.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "machinery_records")
public class MachineryRecord extends PanacheEntityBase {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id", nullable = false)
    @JsonIgnore
    private Phase phase;

    @Column(name = "phase_id", insertable = false, updatable = false)
    private String phaseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machinery_id")
    @JsonIgnore
    private construction.components.machinery.Machinery machinery;

    @Column(name = "machinery_id", insertable = false, updatable = false)
    private String machineryId;

    @Column(name = "is_owned", nullable = false)
    private boolean isOwned = true;

    @Column(name = "rental_type", length = 20)
    private String rentalType = "none"; // none, per_hour, per_day, per_month, per_year

    @Column(name = "rental_price")
    private double rentalPrice = 0;

    @Column(nullable = false)
    private String status = "active"; // active, maintenance, stopped

    @Column(name = "stop_reason", columnDefinition = "TEXT")
    private String stopReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stopped_by")
    @JsonIgnore
    private User stoppedBy;

    @Column(name = "stopped_by", insertable = false, updatable = false)
    private String stoppedById;

    /** Nome de quem parou a máquina, preenchido na resposta (não persiste). */
    @Transient
    private String stoppedByName;

    @Column(name = "stopped_at")
    private LocalDateTime stoppedAt;

    @Column(name = "current_value")
    private double currentValue = 0;

    @Column(name = "total_fuel_cost")
    private double totalFuelCost = 0;

    @Column(name = "total_maintenance_cost")
    private double totalMaintenanceCost = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public MachineryRecord() {
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

    public construction.components.machinery.Machinery getMachinery() { return machinery; }
    public void setMachinery(construction.components.machinery.Machinery machinery) { this.machinery = machinery; }

    public String getMachineryId() { return machineryId; }
    public void setMachineryId(String machineryId) { this.machineryId = machineryId; }

    public boolean isOwned() { return isOwned; }
    public void setOwned(boolean owned) { this.isOwned = owned; }

    public String getRentalType() { return rentalType; }
    public void setRentalType(String rentalType) { this.rentalType = rentalType; }

    public double getRentalPrice() { return rentalPrice; }
    public void setRentalPrice(double rentalPrice) { this.rentalPrice = rentalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStopReason() { return stopReason; }
    public void setStopReason(String stopReason) { this.stopReason = stopReason; }

    public User getStoppedBy() { return stoppedBy; }
    public void setStoppedBy(User stoppedBy) { this.stoppedBy = stoppedBy; }

    public String getStoppedById() { return stoppedById; }
    public void setStoppedById(String stoppedById) { this.stoppedById = stoppedById; }

    public String getStoppedByName() { return stoppedByName; }
    public void setStoppedByName(String stoppedByName) { this.stoppedByName = stoppedByName; }

    public LocalDateTime getStoppedAt() { return stoppedAt; }
    public void setStoppedAt(LocalDateTime stoppedAt) { this.stoppedAt = stoppedAt; }

    public double getCurrentValue() { return currentValue; }
    public void setCurrentValue(double currentValue) { this.currentValue = currentValue; }

    public double getTotalFuelCost() { return totalFuelCost; }
    public void setTotalFuelCost(double totalFuelCost) { this.totalFuelCost = totalFuelCost; }

    public double getTotalMaintenanceCost() { return totalMaintenanceCost; }
    public void setTotalMaintenanceCost(double totalMaintenanceCost) { this.totalMaintenanceCost = totalMaintenanceCost; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
