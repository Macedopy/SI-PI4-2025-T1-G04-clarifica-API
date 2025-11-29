package construction.roofing.entity_external;

import construction.components.machinery.Condition;
import construction.components.machinery.FuelUnit;
import construction.roofing.Roofing;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
// @NotBlank removido

@Entity
@Table(name = "roofing_machineries")
public class RoofingMachinery extends PanacheEntityBase {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roofing_id", referencedColumnName = "id", nullable = false)
    private Roofing roofing;
    
    @Transient
    private String phaseId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(name = "hours_worked", nullable = false)
    @Min(0)
    private double hoursWorked = 0.0;

    @Column(name = "fuel_used", nullable = false)
    @Min(0)
    private double fuelUsed = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_unit", nullable = false)
    private FuelUnit fuelUnit;

    @Column(name = "total_quantity", nullable = false)
    private int totalQuantity = 1;

    @Column(name = "in_operation", nullable = false)
    @Min(0)
    private int inOperation = 0;

    @Column(name = "in_maintenance", nullable = false)
    @Min(0)
    private int inMaintenance = 0;

    @Transient
    private int available;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Condition condition;

    @Column(length = 1000)
    private String notes;

    @PostLoad
    @PostPersist
    @PostUpdate
    private void calculateAvailable() {
        this.available = totalQuantity - inOperation - inMaintenance;
    }

    // Getters and Setters
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

    public double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(double hoursWorked) { this.hoursWorked = hoursWorked; }

    public double getFuelUsed() { return fuelUsed; }
    public void setFuelUsed(double fuelUsed) { this.fuelUsed = fuelUsed; }

    public FuelUnit getFuelUnit() { return fuelUnit; }
    public void setFuelUnit(FuelUnit fuelUnit) { this.fuelUnit = fuelUnit; }

    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }

    public int getInOperation() { return inOperation; }
    public void setInOperation(int inOperation) { this.inOperation = inOperation; }

    public int getInMaintenance() { return inMaintenance; }
    public void setInMaintenance(int inMaintenance) { this.inMaintenance = inMaintenance; }

    public int getAvailable() { return available; }

    public Condition getCondition() { return condition; }
    public void setCondition(Condition condition) { this.condition = condition; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}