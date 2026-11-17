package construction.cost;

import construction.core.Phase;
import construction.user.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "team_member_work_logs")
public class TeamMemberWorkLog extends PanacheEntityBase {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_member_id", nullable = false)
    @JsonIgnore
    private construction.components.team_present.TeamMember teamMember;

    @Column(name = "team_member_id", insertable = false, updatable = false)
    private String teamMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id", nullable = false)
    @JsonIgnore
    private Phase phase;

    @Column(name = "phase_id", insertable = false, updatable = false)
    private String phaseId;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @Column(name = "hours_worked", nullable = false)
    private double hoursWorked;

    @Column(name = "hourly_rate", nullable = false)
    private double hourlyRate;

    @Column(name = "total_cost", nullable = false)
    private double totalCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by", nullable = false)
    @JsonIgnore
    private User addedBy;

    @Column(name = "added_by", insertable = false, updatable = false)
    private String addedById;

    /** Nome de quem registrou o apontamento, preenchido na resposta (não persiste). */
    @Transient
    private String addedByName;

    /** Nome do colaborador, preenchido na resposta (não persiste). */
    @Transient
    private String teamMemberName;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public TeamMemberWorkLog() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public construction.components.team_present.TeamMember getTeamMember() { return teamMember; }
    public void setTeamMember(construction.components.team_present.TeamMember teamMember) { this.teamMember = teamMember; }

    public String getTeamMemberId() { return teamMemberId; }
    public void setTeamMemberId(String teamMemberId) { this.teamMemberId = teamMemberId; }

    public Phase getPhase() { return phase; }
    public void setPhase(Phase phase) { this.phase = phase; }

    public String getPhaseId() { return phaseId; }
    public void setPhaseId(String phaseId) { this.phaseId = phaseId; }

    public LocalDate getWorkDate() { return workDate; }
    public void setWorkDate(LocalDate workDate) { this.workDate = workDate; }

    public double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(double hoursWorked) { this.hoursWorked = hoursWorked; }

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public User getAddedBy() { return addedBy; }
    public void setAddedBy(User addedBy) { this.addedBy = addedBy; }

    public String getAddedById() { return addedById; }
    public void setAddedById(String addedById) { this.addedById = addedById; }

    public String getAddedByName() { return addedByName; }
    public void setAddedByName(String addedByName) { this.addedByName = addedByName; }

    public String getTeamMemberName() { return teamMemberName; }
    public void setTeamMemberName(String teamMemberName) { this.teamMemberName = teamMemberName; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
