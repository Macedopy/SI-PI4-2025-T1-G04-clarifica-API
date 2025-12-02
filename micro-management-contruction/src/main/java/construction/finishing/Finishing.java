package construction.finishing;

import construction.finishing.entity_external.FinishingExecutedService;
import construction.finishing.entity_external.FinishingMachinery;
import construction.finishing.entity_external.FinishingMaterial;
import construction.finishing.entity_external.FinishingPhotoRecord;
import construction.finishing.entity_external.FinishingTeamMember;
import construction.finishing.entity_external.FinishingTool;
import construction.user.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "finishings")
public class Finishing extends PanacheEntityBase {

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

    @OneToMany(mappedBy = "finishing", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FinishingMaterial> materials = new ArrayList<>();

    @OneToMany(mappedBy = "finishing", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FinishingTool> tools = new ArrayList<>();

    @OneToMany(mappedBy = "finishing", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FinishingMachinery> machinery = new ArrayList<>();

    @OneToMany(mappedBy = "finishing", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FinishingTeamMember> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "finishing", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FinishingExecutedService> services = new ArrayList<>();

    @OneToMany(mappedBy = "finishing", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FinishingPhotoRecord> photoRecords = new ArrayList<>();

    public Finishing() {
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

    public List<FinishingMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(List<FinishingMaterial> materials) {
        this.materials = materials;
    }

    public List<FinishingTool> getTools() {
        return tools;
    }

    public void setTools(List<FinishingTool> tools) {
        this.tools = tools;
    }

    public List<FinishingMachinery> getMachinery() {
        return machinery;
    }

    public void setMachinery(List<FinishingMachinery> machinery) {
        this.machinery = machinery;
    }

    public List<FinishingTeamMember> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<FinishingTeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public List<FinishingExecutedService> getServices() {
        return services;
    }

    public void setServices(List<FinishingExecutedService> services) {
        this.services = services;
    }

    public List<FinishingPhotoRecord> getPhotoRecords() {
        return photoRecords;
    }

    public void setPhotoRecords(List<FinishingPhotoRecord> photoRecords) {
        this.photoRecords = photoRecords;
    }
}