package construction.roofing.entity_external;

import com.fasterxml.jackson.annotation.JsonIgnore;

import construction.components.tools.ToolCondition;
import construction.roofing.Roofing; // Assumindo existência
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "roofing_tools")
public class RoofingTool extends PanacheEntityBase {
    
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roofing_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Roofing roofing;

    @Transient
    private String phaseId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Min(value = 1, message = "Total quantity must be at least 1")
    @Column(name = "total_quantity", nullable = false)
    private int totalQuantity;

    @Min(value = 0)
    @Column(name = "in_use", nullable = false)
    private int inUse = 0;

    @Min(value = 0)
    @Column(name = "in_maintenance", nullable = false)
    private int inMaintenance = 0;

    @Transient
    private int available = 1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ToolCondition condition;

    @Column(length = 1000)
    private String notes;

    @PostLoad
    @PostPersist
    @PostUpdate
    private void updateAvailable() {
        this.available = totalQuantity - inUse - inMaintenance;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Roofing getRoofing() { return roofing; }
    public void setRoofing(Roofing roofing) { this.roofing = roofing; }

    public String getPhaseId() { return phaseId; }
    public void setPhaseId(String phaseId) { this.phaseId = phaseId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; updateAvailable(); }

    public int getInUse() { return inUse; }
    public void setInUse(int inUse) { this.inUse = inUse; updateAvailable(); }

    public int getInMaintenance() { return inMaintenance; }
    public void setInMaintenance(int inMaintenance) { this.inMaintenance = inMaintenance; updateAvailable(); }

    public int getAvailable() { return available; }

    public ToolCondition getCondition() { return condition; }
    public void setCondition(ToolCondition condition) { this.condition = condition; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}