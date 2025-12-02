package construction.roofing;

import construction.roofing.entity_external.RoofingExecutedService;
import construction.roofing.entity_external.RoofingMachinery;
import construction.roofing.entity_external.RoofingMaterial;
import construction.roofing.entity_external.RoofingPhotoRecord;
import construction.roofing.entity_external.RoofingTeamMember;
import construction.roofing.entity_external.RoofingTool;
import construction.user.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "roofing")
public class Roofing extends PanacheEntityBase {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "contractor")
    private String contractor;

    @OneToMany(mappedBy = "roofing", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RoofingMaterial> materials = new ArrayList<>();

    @OneToMany(mappedBy = "roofing", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RoofingTool> tools = new ArrayList<>();

    @OneToMany(mappedBy = "roofing", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RoofingMachinery> machinery = new ArrayList<>();

    @OneToMany(mappedBy = "roofing", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RoofingTeamMember> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "roofing", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RoofingExecutedService> services = new ArrayList<>();

    @OneToMany(mappedBy = "roofing", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RoofingPhotoRecord> photoRecords = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id") 
    @JsonIgnore
    private User user;

    public Roofing() {
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

    public List<RoofingMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(List<RoofingMaterial> materials) {
        this.materials = materials;
    }

    public List<RoofingTool> getTools() {
        return tools;
    }

    public void setTools(List<RoofingTool> tools) {
        this.tools = tools;
    }

    public List<RoofingMachinery> getMachinery() {
        return machinery;
    }

    public void setMachinery(List<RoofingMachinery> machinery) {
        this.machinery = machinery;
    }

    public List<RoofingTeamMember> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<RoofingTeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public List<RoofingExecutedService> getServices() {
        return services;
    }

    public void setServices(List<RoofingExecutedService> services) {
        this.services = services;
    }

    public List<RoofingPhotoRecord> getPhotoRecords() {
        return photoRecords;
    }

    public void setPhotoRecords(List<RoofingPhotoRecord> photoRecords) {
        this.photoRecords = photoRecords;
    }
}