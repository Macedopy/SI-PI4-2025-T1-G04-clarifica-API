package construction.terrain_preparation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import construction.structure.entity_external.StructureExecutedService;
import construction.structure.entity_external.StructureMaterial;
import construction.structure.entity_external.StructurePhotoRecord;
import construction.structure.entity_external.StructureTeamMember;
import construction.structure.entity_external.StructureTool;
import construction.terrain_preparation.entity_external.TerrainPreparationExecutedService;
import construction.terrain_preparation.entity_external.TerrainPreparationGeneralInformation;
import construction.terrain_preparation.entity_external.TerrainPreparationMachinery;
import construction.terrain_preparation.entity_external.TerrainPreparationMaterial;
import construction.terrain_preparation.entity_external.TerrainPreparationPhotoRecord;
import construction.terrain_preparation.entity_external.TerrainPreparationTeamMember;
import construction.terrain_preparation.entity_external.TerrainPreparationTool;
import construction.user.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity @Table(name = "terrainPreparation") public class TerrainPreparation extends PanacheEntityBase {
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

    @OneToMany(mappedBy = "terrainPreparation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TerrainPreparationMaterial> materials = new ArrayList<>();

    @OneToMany(mappedBy = "terrainPreparation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TerrainPreparationTool> tools = new ArrayList<>();

    @OneToMany(mappedBy = "terrainPreparation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TerrainPreparationMachinery> machinery = new ArrayList<>();

    @OneToMany(mappedBy = "terrainPreparation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TerrainPreparationTeamMember> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "terrainPreparation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TerrainPreparationExecutedService> services = new ArrayList<>();

    @OneToMany(mappedBy = "terrainPreparation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TerrainPreparationPhotoRecord> photoRecords = new ArrayList<>();

    @OneToMany(mappedBy = "terrainPreparation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TerrainPreparationGeneralInformation> generalInformations = new ArrayList<>();

    public TerrainPreparation() {
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

    public List<TerrainPreparationMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(List<TerrainPreparationMaterial> materials) {
        this.materials = materials;
    }

    public List<TerrainPreparationTool> getTools() {
        return tools;
    }

    public void setTools(List<TerrainPreparationTool> tools) {
        this.tools = tools;
    }

    public List<TerrainPreparationMachinery> getMachinery() {
        return machinery;
    }

    public void setMachinery(List<TerrainPreparationMachinery> machinery) {
        this.machinery = machinery;
    }

    public List<TerrainPreparationTeamMember> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<TerrainPreparationTeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public List<TerrainPreparationExecutedService> getServices() {
        return services;
    }

    public void setServices(List<TerrainPreparationExecutedService> services) {
        this.services = services;
    }

    public List<TerrainPreparationPhotoRecord> getPhotoRecords() {
        return photoRecords;
    }

    public void setPhotoRecords(List<TerrainPreparationPhotoRecord> photoRecords) {
        this.photoRecords = photoRecords;
    }
}
