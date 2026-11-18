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
import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Column(name = "template_phase_id", length = 50)
    private String templatePhaseId;

    @Column(name = "status")
    private String status = "not_started";

    @Column(name = "planned_start_date")
    private java.time.LocalDate plannedStartDate;

    @Column(name = "planned_end_date")
    private java.time.LocalDate plannedEndDate;

    @Column(name = "actual_start_date")
    private java.time.LocalDate actualStartDate;

    @Column(name = "actual_end_date")
    private java.time.LocalDate actualEndDate;

    @Column(name = "budget_amount")
    private java.math.BigDecimal budgetAmount = java.math.BigDecimal.ZERO;

    @Column(name = "currency_code", length = 3)
    private String currencyCode;

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

    public String getTemplatePhaseId() { return templatePhaseId; }
    public void setTemplatePhaseId(String templatePhaseId) { this.templatePhaseId = templatePhaseId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public java.time.LocalDate getPlannedStartDate() { return plannedStartDate; }
    public void setPlannedStartDate(java.time.LocalDate plannedStartDate) { this.plannedStartDate = plannedStartDate; }

    public java.time.LocalDate getPlannedEndDate() { return plannedEndDate; }
    public void setPlannedEndDate(java.time.LocalDate plannedEndDate) { this.plannedEndDate = plannedEndDate; }

    public java.time.LocalDate getActualStartDate() { return actualStartDate; }
    public void setActualStartDate(java.time.LocalDate actualStartDate) { this.actualStartDate = actualStartDate; }

    public java.time.LocalDate getActualEndDate() { return actualEndDate; }
    public void setActualEndDate(java.time.LocalDate actualEndDate) { this.actualEndDate = actualEndDate; }

    public java.math.BigDecimal getBudgetAmount() { return budgetAmount; }
    public void setBudgetAmount(java.math.BigDecimal budgetAmount) { this.budgetAmount = budgetAmount; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

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
