package construction.core;

import construction.user.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "constructions")
public class Construction extends PanacheEntityBase {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String status = "ACTIVE";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private User owner;

    @ManyToMany
    @JoinTable(
        name = "construction_clients",
        joinColumns = @JoinColumn(name = "construction_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> clients = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "construction_master_builders",
        joinColumns = @JoinColumn(name = "construction_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> masterBuilders = new ArrayList<>();

    @OneToMany(mappedBy = "construction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phase> phases = new ArrayList<>();

    public Construction() {
        this.id = UUID.randomUUID().toString();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public List<User> getClients() { return clients; }
    public void setClients(List<User> clients) { this.clients = clients; }

    public List<User> getMasterBuilders() { return masterBuilders; }
    public void setMasterBuilders(List<User> masterBuilders) { this.masterBuilders = masterBuilders; }

    public List<Phase> getPhases() { return phases; }
    public void setPhases(List<Phase> phases) { this.phases = phases; }
}
