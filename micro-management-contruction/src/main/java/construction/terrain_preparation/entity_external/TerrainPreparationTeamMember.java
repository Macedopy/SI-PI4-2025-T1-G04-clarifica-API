package construction.terrain_preparation.entity_external;

import com.fasterxml.jackson.annotation.JsonIgnore;

import construction.components.team_present.MemberStatus;
import construction.components.team_present.TeamMemberDetails;
import construction.structure.Structure;
import construction.terrain_preparation.TerrainPreparation;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "terrain_preparation_team_members")
public class TerrainPreparationTeamMember extends PanacheEntityBase {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terrainPreparation_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private TerrainPreparation terrainPreparation;

    @Transient
    private String phaseId;

    @Embedded
    private TeamMemberDetails details; 

    @Column(name = "hours_worked", nullable = false)
    private double hoursWorked = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status; 

    @Column(length = 500)
    private String notes;

    public TerrainPreparationTeamMember() {}
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public TerrainPreparation getTerrainPreparation() { return terrainPreparation; }
    public void setTerrainPreparation(TerrainPreparation terrainPreparation) { this.terrainPreparation = terrainPreparation; }
    public String getPhaseId() { return phaseId; }
    public void setPhaseId(String phaseId) { this.phaseId = phaseId; }
    public TeamMemberDetails getDetails() { return details; }
    public void setDetails(TeamMemberDetails details) { this.details = details; }
    public double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(double hoursWorked) { this.hoursWorked = hoursWorked; }
    public MemberStatus getStatus() { return status; }
    public void setStatus(MemberStatus status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}