package construction.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import construction.coatings.Coatings;
import construction.eletric.Eletric;
import construction.finishing.Finishing;
import construction.foundation.Foundation;
import construction.hydraulic.Hydraulic;
import construction.masonry.Masonry;
import construction.roofing.Roofing;
import construction.structure.Structure;
import construction.terrain_preparation.TerrainPreparation;

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

    // Relacionamentos com as fases
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Foundation foundation;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Structure structure;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Masonry masonry;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Eletric electrical;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Hydraulic hydraulic;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Coatings coatings;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Finishing finishing;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Roofing roofing;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Eletric eletric;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TerrainPreparation terrainPreparation;

    // Getters e Setters básicos

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

    // ADICIONAR Getters e Setters das fases

    public Foundation getFoundation() { 
        return foundation; 
    }
    
    public void setFoundation(Foundation foundation) { 
        this.foundation = foundation; 
    }

    public TerrainPreparation getTerrainPreparation() { 
        return terrainPreparation; 
    }
    
    public void setTerrainPreparation(TerrainPreparation terrainPreparation) { 
        this.terrainPreparation = terrainPreparation; 
    }

    public Hydraulic getHydraulic() { 
        return hydraulic; 
    }
    
    public void setHydraulic(Hydraulic hydraulic) { 
        this.hydraulic = hydraulic; 
    }

    public Structure getStructure() { 
        return structure; 
    }
    
    public void setStructure(Structure structure) { 
        this.structure = structure; 
    }

    public Finishing getFinishing() { 
        return finishing; 
    }
    
    public void setFinishing(Finishing finishing) { 
        this.finishing = finishing; 
    }

    public Masonry getMasonry() { 
        return masonry; 
    }
    
    public void setMasonry(Masonry masonry) { 
        this.masonry = masonry; 
    }

    public Eletric getElectrical() { 
        return electrical; 
    }
    
    public void setElectrical(Eletric electrical) { 
        this.electrical = electrical; 
    }

    public Coatings getCoatings() { 
        return coatings; 
    }
    
    public void setCoatings(Coatings coatings) { 
        this.coatings = coatings; 
    }

    public Roofing getRoofing() { 
        return roofing; 
    }
    
    public void setRoofing(Roofing roofing) { 
        this.roofing = roofing; 
    }

    public String fullName() {
        return firstName + " " + lastName;
    }
}
