package construction.eletric;

import construction.eletric.entity_external.EletricExecutedService;
import construction.eletric.entity_external.EletricMachinery;
import construction.eletric.entity_external.EletricMaterial;
import construction.eletric.entity_external.EletricPhotoRecord;
import construction.eletric.entity_external.EletricTeamMember;
import construction.eletric.entity_external.EletricTool;
import construction.user.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "eletric")
public class Eletric extends PanacheEntityBase {

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

    @OneToMany(mappedBy = "eletric", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EletricMaterial> materials = new ArrayList<>();

    @OneToMany(mappedBy = "eletric", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EletricTool> tools = new ArrayList<>();

    @OneToMany(mappedBy = "eletric", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EletricMachinery> machinery = new ArrayList<>();

    @OneToMany(mappedBy = "eletric", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EletricTeamMember> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "eletric", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EletricExecutedService> services = new ArrayList<>();

    @OneToMany(mappedBy = "eletric", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EletricPhotoRecord> photoRecords = new ArrayList<>();

    public Eletric() {
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

    public List<EletricMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(List<EletricMaterial> materials) {
        this.materials = materials;
    }

    public List<EletricTool> getTools() {
        return tools;
    }

    public void setTools(List<EletricTool> tools) {
        this.tools = tools;
    }

    public List<EletricMachinery> getMachinery() {
        return machinery;
    }

    public void setMachinery(List<EletricMachinery> machinery) {
        this.machinery = machinery;
    }

    public List<EletricTeamMember> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<EletricTeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public List<EletricExecutedService> getServices() {
        return services;
    }

    public void setServices(List<EletricExecutedService> services) {
        this.services = services;
    }

    public List<EletricPhotoRecord> getPhotoRecords() {
        return photoRecords;
    }

    public void setPhotoRecords(List<EletricPhotoRecord> photoRecords) {
        this.photoRecords = photoRecords;
    }
}