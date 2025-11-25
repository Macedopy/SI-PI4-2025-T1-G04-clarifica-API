package construction.masonry.entity_external;

import construction.components.team_present.MemberStatus;
import construction.components.team_present.TeamMemberDetails;
import construction.masonry.Masonry;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "masonry_team_members")
public class MasonryTeamMember extends PanacheEntityBase {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "masonry_id", referencedColumnName = "id", nullable = false)
    private Masonry masonry;

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

    public MasonryTeamMember() {}
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public Masonry getMasonry() { return masonry; }
    public void setMasonry(Masonry masonry) { this.masonry = masonry; }
    
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