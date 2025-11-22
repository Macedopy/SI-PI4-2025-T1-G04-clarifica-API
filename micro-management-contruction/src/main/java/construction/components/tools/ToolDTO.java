package construction.components.tools;

public class ToolDTO {

    private String id;
    private String name;
    private String category;
    private int totalQuantity;
    private int inUse;
    private int inMaintenance;
    private String condition;
    private String notes;

    public ToolDTO() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }
    public int getInUse() { return inUse; }
    public void setInUse(int inUse) { this.inUse = inUse; }
    public int getInMaintenance() { return inMaintenance; }
    public void setInMaintenance(int inMaintenance) { this.inMaintenance = inMaintenance; }
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
