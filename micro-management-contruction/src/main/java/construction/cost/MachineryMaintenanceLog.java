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
@Table(name = "machinery_maintenance_logs")
public class MachineryMaintenanceLog extends PanacheEntityBase {

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

    @Column(name = "maintenance_date", nullable = false)
    private LocalDate maintenanceDate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private double cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by", nullable = false)
    @JsonIgnore
    private User performedBy;

    @Column(name = "performed_by", insertable = false, updatable = false)
    private String performedById;

    /** Nome de quem executou, preenchido na resposta (não persiste). */
    @Transient
    private String performedByName;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public MachineryMaintenanceLog() {
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

    public LocalDate getMaintenanceDate() { return maintenanceDate; }
    public void setMaintenanceDate(LocalDate maintenanceDate) { this.maintenanceDate = maintenanceDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    public User getPerformedBy() { return performedBy; }
    public void setPerformedBy(User performedBy) { this.performedBy = performedBy; }

    public String getPerformedById() { return performedById; }
    public void setPerformedById(String performedById) { this.performedById = performedById; }

    public String getPerformedByName() { return performedByName; }
    public void setPerformedByName(String performedByName) { this.performedByName = performedByName; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
