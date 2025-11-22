package construction.components.executed_services;

public class ExecutedServiceDTO {

    private String id;
    private String name;
    private String team;
    private double plannedHours;
    private double executedHours;
    private String status;
    private String notes;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }
    public double getPlannedHours() { return plannedHours; }
    public void setPlannedHours(double plannedHours) { this.plannedHours = plannedHours; }
    public double getExecutedHours() { return executedHours; }
    public void setExecutedHours(double executedHours) { this.executedHours = executedHours; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
