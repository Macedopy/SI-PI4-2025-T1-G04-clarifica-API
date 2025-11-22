package construction.foundation.entity_external;

import construction.components.tools.ToolCondition;
import construction.foundation.Foundation;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "foundation_tools")
public class FoundationTool extends PanacheEntityBase {
    
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foundation_id", referencedColumnName = "id", nullable = false)
    private Foundation foundation;

    @Transient
    private String phaseId;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Category is required")
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

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Foundation getFoundation() { return foundation; }
    public void setFoundation(Foundation foundation) { this.foundation = foundation; }

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
