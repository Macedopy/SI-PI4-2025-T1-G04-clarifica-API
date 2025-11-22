package construction.components.team_present;

import jakarta.validation.constraints.NotBlank;

public class TeamMemberDTO {

    private String id;

    @NotBlank(message = "O nome do membro é obrigatório")
    private String name;

    private String role;
    private String cpf;
    private double hoursWorked;
    private MemberStatus status;
    private String notes;

    public TeamMemberDTO() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(double hoursWorked) { this.hoursWorked = hoursWorked; }
    public MemberStatus getStatus() { return status; }
    public void setStatus(MemberStatus status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}