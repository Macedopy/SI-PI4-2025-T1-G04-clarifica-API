package construction.components.tools;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "tools")
public class Tool extends PanacheEntityBase {

    @Id
    private String id;

    @Column(name = "phase_id", nullable = false)
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

    @Column(nullable = false)
    private int available = 1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ToolCondition condition;

    @Column(length = 1000)
    private String notes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(String phaseId) {
        this.phaseId = phaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
        updateAvailable();
    }

    public int getInUse() {
        return inUse;
    }

    public void setInUse(int inUse) {
        this.inUse = Math.max(0, Math.min(inUse, totalQuantity - inMaintenance));
        updateAvailable();
    }

    public int getInMaintenance() {
        return inMaintenance;
    }

    public void setInMaintenance(int inMaintenance) {
        this.inMaintenance = Math.max(0, Math.min(inMaintenance, totalQuantity - inUse));
        updateAvailable();
    }

    public int getAvailable() {
        return available;
    }

    public ToolCondition getCondition() {
        return condition;
    }

    public void setCondition(ToolCondition condition) {
        this.condition = condition;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    private void updateAvailable() {
        this.available = totalQuantity - inUse - inMaintenance;
    }

    public void useTool() {
        if (inUse < totalQuantity - inMaintenance) {
            this.inUse++;
            updateAvailable();
        }
    }

    public void returnTool() {
        if (inUse > 0) {
            this.inUse--;
            updateAvailable();
        }
    }

    public void sendToMaintenance() {
        if (inMaintenance < totalQuantity - inUse) {
            this.inMaintenance++;
            updateAvailable();
        }
    }

    public void returnFromMaintenance() {
        if (inMaintenance > 0) {
            this.inMaintenance--;
            updateAvailable();
        }
    }
}