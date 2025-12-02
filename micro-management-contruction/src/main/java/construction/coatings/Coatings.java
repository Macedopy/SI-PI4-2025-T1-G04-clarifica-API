package construction.coatings;

import construction.coatings.entity_external.CoatingsExecutedService;
import construction.coatings.entity_external.CoatingsMachinery;
import construction.coatings.entity_external.CoatingsMaterial;
import construction.coatings.entity_external.CoatingsPhotoRecord;
import construction.coatings.entity_external.CoatingsTeamMember;
import construction.coatings.entity_external.CoatingsTool;
import construction.user.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "coatings")
public class Coatings extends PanacheEntityBase {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "contractor")
    private String contractor;

    @OneToOne
    @JoinColumn(name = "user_id") 
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "coatings", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CoatingsMaterial> materials = new ArrayList<>();

    @OneToMany(mappedBy = "coatings", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CoatingsTool> tools = new ArrayList<>();

    @OneToMany(mappedBy = "coatings", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CoatingsMachinery> machinery = new ArrayList<>();

    @OneToMany(mappedBy = "coatings", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CoatingsTeamMember> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "coatings", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CoatingsExecutedService> services = new ArrayList<>();

    @OneToMany(mappedBy = "coatings", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CoatingsPhotoRecord> photoRecords = new ArrayList<>();

    public Coatings() {
        // Garante que o ID seja gerado se não existir
        this.id = UUID.randomUUID().toString();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public List<CoatingsMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(List<CoatingsMaterial> materials) {
        this.materials = materials;
    }

    public List<CoatingsTool> getTools() {
        return tools;
    }

    public void setTools(List<CoatingsTool> tools) {
        this.tools = tools;
    }

    public List<CoatingsMachinery> getMachinery() {
        return machinery;
    }

    public void setMachinery(List<CoatingsMachinery> machinery) {
        this.machinery = machinery;
    }

    public List<CoatingsTeamMember> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<CoatingsTeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public List<CoatingsExecutedService> getServices() {
        return services;
    }

    public void setServices(List<CoatingsExecutedService> services) {
        this.services = services;
    }

    public List<CoatingsPhotoRecord> getPhotoRecords() {
        return photoRecords;
    }

    public void setPhotoRecords(List<CoatingsPhotoRecord> photoRecords) {
        this.photoRecords = photoRecords;
    }
}