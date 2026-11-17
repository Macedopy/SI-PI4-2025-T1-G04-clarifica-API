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
@Table(name = "machinery_fuel_logs")
public class MachineryFuelLog extends PanacheEntityBase {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machinery_id", nullable = false)
    @JsonIgnore
    private construction.components.machinery.Machinery machinery;

    @Column(name = "machinery_id", insertable = false, updatable = false)
    private String machineryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id", nullable = false)
    @JsonIgnore
    private Phase phase;

    @Column(name = "phase_id", insertable = false, updatable = false)
    private String phaseId;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(nullable = false)
    private double liters;

    @Column(name = "price_per_liter", nullable = false)
    private double pricePerLiter;

    @Column(name = "total_cost", nullable = false)
    private double totalCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorded_by", nullable = false)
    @JsonIgnore
    private User recordedBy;

    @Column(name = "recorded_by", insertable = false, updatable = false)
    private String recordedById;

    /** Nome de quem registrou, preenchido na resposta (não persiste). */
    @Transient
    private String recordedByName;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public MachineryFuelLog() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public construction.components.machinery.Machinery getMachinery() { return machinery; }
    public void setMachinery(construction.components.machinery.Machinery machinery) { this.machinery = machinery; }

    public String getMachineryId() { return machineryId; }
    public void setMachineryId(String machineryId) { this.machineryId = machineryId; }

    public Phase getPhase() { return phase; }
    public void setPhase(Phase phase) { this.phase = phase; }

    public String getPhaseId() { return phaseId; }
    public void setPhaseId(String phaseId) { this.phaseId = phaseId; }

    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }

    public double getLiters() { return liters; }
    public void setLiters(double liters) { this.liters = liters; }

    public double getPricePerLiter() { return pricePerLiter; }
    public void setPricePerLiter(double pricePerLiter) { this.pricePerLiter = pricePerLiter; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public User getRecordedBy() { return recordedBy; }
    public void setRecordedBy(User recordedBy) { this.recordedBy = recordedBy; }

    public String getRecordedById() { return recordedById; }
    public void setRecordedById(String recordedById) { this.recordedById = recordedById; }

    public String getRecordedByName() { return recordedByName; }
    public void setRecordedByName(String recordedByName) { this.recordedByName = recordedByName; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
