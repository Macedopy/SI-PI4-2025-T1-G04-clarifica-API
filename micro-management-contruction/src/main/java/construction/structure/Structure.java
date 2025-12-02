package construction.structure;

import construction.structure.entity_external.StructureExecutedService; import construction.structure.entity_external.StructureMachinery; import construction.structure.entity_external.StructureMaterial; import construction.structure.entity_external.StructurePhotoRecord; import construction.structure.entity_external.StructureTeamMember; import construction.structure.entity_external.StructureTool;
import construction.user.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase; import jakarta.persistence.*; import java.util.ArrayList; import java.util.List; import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity @Table(name = "structures") public class Structure extends PanacheEntityBase {

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

    @OneToMany(mappedBy = "structure", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StructureMaterial> materials = new ArrayList<>();

    @OneToMany(mappedBy = "structure", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StructureTool> tools = new ArrayList<>();

    @OneToMany(mappedBy = "structure", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StructureMachinery> machinery = new ArrayList<>();

    @OneToMany(mappedBy = "structure", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StructureTeamMember> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "structure", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StructureExecutedService> services = new ArrayList<>();

    @OneToMany(mappedBy = "structure", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StructurePhotoRecord> photoRecords = new ArrayList<>();

    public Structure() {
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

    public List<StructureMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(List<StructureMaterial> materials) {
        this.materials = materials;
    }

    public List<StructureTool> getTools() {
        return tools;
    }

    public void setTools(List<StructureTool> tools) {
        this.tools = tools;
    }

    public List<StructureMachinery> getMachinery() {
        return machinery;
    }

    public void setMachinery(List<StructureMachinery> machinery) {
        this.machinery = machinery;
    }

    public List<StructureTeamMember> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<StructureTeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public List<StructureExecutedService> getServices() {
        return services;
    }

    public void setServices(List<StructureExecutedService> services) {
        this.services = services;
    }

    public List<StructurePhotoRecord> getPhotoRecords() {
        return photoRecords;
    }

    public void setPhotoRecords(List<StructurePhotoRecord> photoRecords) {
        this.photoRecords = photoRecords;
    }
}