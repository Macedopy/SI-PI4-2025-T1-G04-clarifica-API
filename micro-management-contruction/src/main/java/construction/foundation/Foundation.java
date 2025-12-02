package construction.foundation;

import construction.foundation.entity_external.FoundationExecutedService;
import construction.foundation.entity_external.FoundationMachinery;
import construction.foundation.entity_external.FoundationMaterial;
import construction.foundation.entity_external.FoundationPhotoRecord;
import construction.foundation.entity_external.FoundationTeamMember;
import construction.foundation.entity_external.FoundationTool;
import construction.user.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "foundations")
public class Foundation extends PanacheEntityBase {

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

    @OneToMany(mappedBy = "foundation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FoundationMaterial> materials = new ArrayList<>();

    @OneToMany(mappedBy = "foundation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FoundationTool> tools = new ArrayList<>();

    @OneToMany(mappedBy = "foundation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FoundationMachinery> machinery = new ArrayList<>();

    @OneToMany(mappedBy = "foundation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FoundationTeamMember> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "foundation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FoundationExecutedService> services = new ArrayList<>();

    @OneToMany(mappedBy = "foundation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FoundationPhotoRecord> photoRecords = new ArrayList<>();

    public Foundation() {
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

    public List<FoundationMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(List<FoundationMaterial> materials) {
        this.materials = materials;
    }

    public List<FoundationTool> getTools() {
        return tools;
    }

    public void setTools(List<FoundationTool> tools) {
        this.tools = tools;
    }

    public List<FoundationMachinery> getMachinery() {
        return machinery;
    }

    public void setMachinery(List<FoundationMachinery> machinery) {
        this.machinery = machinery;
    }

    public List<FoundationTeamMember> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<FoundationTeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public List<FoundationExecutedService> getServices() {
        return services;
    }

    public void setServices(List<FoundationExecutedService> services) {
        this.services = services;
    }

    public List<FoundationPhotoRecord> getPhotoRecords() {
        return photoRecords;
    }

    public void setPhotoRecords(List<FoundationPhotoRecord> photoRecords) {
        this.photoRecords = photoRecords;
    }
}