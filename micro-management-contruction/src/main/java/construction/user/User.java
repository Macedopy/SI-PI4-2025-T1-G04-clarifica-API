package construction.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import construction.core.Construction;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends PanacheEntityBase {

    @Id
    private String id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF must be in the format XXX.XXX.XXX-XX")
    @Column(nullable = false, unique = true)
    private String cpf;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Construction> ownedConstructions = new ArrayList<>();

    @ManyToMany(mappedBy = "clients")
    private List<Construction> clientConstructions = new ArrayList<>();

    @ManyToMany(mappedBy = "masterBuilders")
    private List<Construction> managedConstructions = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public List<Construction> getOwnedConstructions() { return ownedConstructions; }
    public void setOwnedConstructions(List<Construction> ownedConstructions) { this.ownedConstructions = ownedConstructions; }

    public List<Construction> getClientConstructions() { return clientConstructions; }
    public void setClientConstructions(List<Construction> clientConstructions) { this.clientConstructions = clientConstructions; }

    public List<Construction> getManagedConstructions() { return managedConstructions; }
    public void setManagedConstructions(List<Construction> managedConstructions) { this.managedConstructions = managedConstructions; }

    public String fullName() {
        return firstName + " " + lastName;
    }
}
