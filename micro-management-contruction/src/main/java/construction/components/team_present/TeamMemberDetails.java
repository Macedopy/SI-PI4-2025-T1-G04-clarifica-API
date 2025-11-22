package construction.components.team_present;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;

@Embeddable 
public class TeamMemberDetails {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role;

    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF must be in format XXX.XXX.XXX-XX")
    @Column(length = 14)
    private String cpf;

    public TeamMemberDetails() {}
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
}
