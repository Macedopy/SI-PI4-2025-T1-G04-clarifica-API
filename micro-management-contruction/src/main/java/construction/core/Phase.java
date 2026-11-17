package construction.core;

import construction.components.executed_services.ExecutedService;
import construction.components.machinery.Machinery;
import construction.components.photo.PhotoRecord;
import construction.components.team_present.TeamMember;
import construction.components.tools.Tool;
import construction.components.used_material.Material;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "phases")
public class Phase extends PanacheEntityBase {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "contractor")
    private String contractor;

    @Column(name = "icon", length = 50)
    private String icon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "construction_id", nullable = false)
    @JsonIgnore
    private Construction construction;

    @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Material> materials = new ArrayList<>();

    @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tool> tools = new ArrayList<>();

    @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Machinery> machinery = new ArrayList<>();

    @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMember> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExecutedService> services = new ArrayList<>();

    @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhotoRecord> photoRecords = new ArrayList<>();

    public Phase() {
        this.id = UUID.randomUUID().toString();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContractor() { return contractor; }
    public void setContractor(String contractor) { this.contractor = contractor; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public Construction getConstruction() { return construction; }
    public void setConstruction(Construction construction) { this.construction = construction; }

    public List<Material> getMaterials() { return materials; }
    public void setMaterials(List<Material> materials) { this.materials = materials; }

    public List<Tool> getTools() { return tools; }
    public void setTools(List<Tool> tools) { this.tools = tools; }

    public List<Machinery> getMachinery() { return machinery; }
    public void setMachinery(List<Machinery> machinery) { this.machinery = machinery; }

    public List<TeamMember> getTeamMembers() { return teamMembers; }
    public void setTeamMembers(List<TeamMember> teamMembers) { this.teamMembers = teamMembers; }

    public List<ExecutedService> getServices() { return services; }
    public void setServices(List<ExecutedService> services) { this.services = services; }

    public List<PhotoRecord> getPhotoRecords() { return photoRecords; }
    public void setPhotoRecords(List<PhotoRecord> photoRecords) { this.photoRecords = photoRecords; }
}
