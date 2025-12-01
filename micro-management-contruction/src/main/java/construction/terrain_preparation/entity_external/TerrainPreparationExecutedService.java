package construction.terrain_preparation.entity_external;
import com.fasterxml.jackson.annotation.JsonIgnore;

import construction.components.executed_services.ExecutedServiceStatus;
import construction.terrain_preparation.TerrainPreparation;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "terrain_executed_services")
public class TerrainPreparationExecutedService extends PanacheEntityBase {
    
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terrainPreparation_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private TerrainPreparation terrainPreparation;

    @Transient
    private String phaseId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String team;

    @Min(value = 1, message = "Planned hours must be greater than 0")
    @Column(name = "planned_hours", nullable = false)
    private double plannedHours;

    @Min(value = 0, message = "Executed hours cannot be negative")
    @Column(name = "executed_hours", nullable = false)
    private double executedHours = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExecutedServiceStatus status;

    @Min(value = 0)
    @Column(nullable = false)
    private int progress = 0;

    @Column(length = 1000)
    private String notes;

    @PostLoad
    @PostPersist
    @PostUpdate
    private void updateProgress() {
        if (plannedHours > 0) {
            this.progress = (int) Math.round((executedHours / plannedHours) * 100);
            this.progress = Math.min(100, Math.max(0, this.progress));
        } else {
            this.progress = 0;
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public TerrainPreparation getTerrainPreparation() { return terrainPreparation; }
    public void setTerrainPreparation(TerrainPreparation terrainPreparation) { this.terrainPreparation = terrainPreparation; }

    public String getPhaseId() { return phaseId; }
    public void setPhaseId(String phaseId) { this.phaseId = phaseId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }

    public double getPlannedHours() { return plannedHours; }
    public void setPlannedHours(double plannedHours) { this.plannedHours = plannedHours; updateProgress(); }

    public double getExecutedHours() { return executedHours; }
    public void setExecutedHours(double executedHours) { this.executedHours = executedHours; updateProgress(); }

    public ExecutedServiceStatus getStatus() { return status; }
    public void setStatus(ExecutedServiceStatus status) { this.status = status; }

    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
