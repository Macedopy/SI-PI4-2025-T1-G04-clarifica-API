package construction.components.machinery;

public class MachineryDTO {
    
    private String id; 
    
    private String name;
    private String category;
    private int totalQuantity;
    private String condition;
    private String fuelUnit;
    private String notes;

    private double hoursWorked;
    private double fuelUsed;
    private int inOperation;
    private int inMaintenance;

    public MachineryDTO() {
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    public String getFuelUnit() { return fuelUnit; }
    public void setFuelUnit(String fuelUnit) { this.fuelUnit = fuelUnit; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(double hoursWorked) { this.hoursWorked = hoursWorked; }
    public double getFuelUsed() { return fuelUsed; }
    public void setFuelUsed(double fuelUsed) { this.fuelUsed = fuelUsed; }
    public int getInOperation() { return inOperation; }
    public void setInOperation(int inOperation) { this.inOperation = inOperation; }
    public int getInMaintenance() { return inMaintenance; }
    public void setInMaintenance(int inMaintenance) { this.inMaintenance = inMaintenance; }
}
