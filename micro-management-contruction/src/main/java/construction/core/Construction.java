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

    @Column(name = "country_code")
    private String countryCode = "BR";

    @Column(name = "currency_code")
    private String currencyCode = "BRL";

    @Column(name = "measurement_system")
    private String measurementSystem = "metric";

    @Column(name = "state_province_code")
    private String stateProvinceCode;

    @Column(name = "municipality")
    private String municipality;

    @Column(name = "construction_method")
    private String constructionMethod;

    @Column(name = "template_id")
    private String templateId;

    @Column(name = "language")
    private String language = "pt";

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

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public String getMeasurementSystem() { return measurementSystem; }
    public void setMeasurementSystem(String measurementSystem) { this.measurementSystem = measurementSystem; }

    public String getStateProvinceCode() { return stateProvinceCode; }
    public void setStateProvinceCode(String stateProvinceCode) { this.stateProvinceCode = stateProvinceCode; }

    public String getMunicipality() { return municipality; }
    public void setMunicipality(String municipality) { this.municipality = municipality; }

    public String getConstructionMethod() { return constructionMethod; }
    public void setConstructionMethod(String constructionMethod) { this.constructionMethod = constructionMethod; }

    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}
