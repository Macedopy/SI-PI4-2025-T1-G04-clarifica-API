package construction.core;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "template_phases")
public class TemplatePhase extends PanacheEntityBase {

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private ProjectTemplate template;

    @Column(name = "template_id", insertable = false, updatable = false)
    private String templateId;

    @Column(name = "phase_id", nullable = false)
    private String phaseId;

    @Column(name = "name_key", nullable = false)
    private String nameKey;

    @Column(name = "icon", length = 50)
    private String icon;

    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    @Column(name = "is_optional", nullable = false)
    private boolean isOptional = false;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "components", columnDefinition = "jsonb")
    private String components;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public TemplatePhase() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public ProjectTemplate getTemplate() { return template; }
    public void setTemplate(ProjectTemplate template) { this.template = template; }

    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }

    public String getPhaseId() { return phaseId; }
    public void setPhaseId(String phaseId) { this.phaseId = phaseId; }

    public String getNameKey() { return nameKey; }
    public void setNameKey(String nameKey) { this.nameKey = nameKey; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public Integer getSequence() { return sequence; }
    public void setSequence(Integer sequence) { this.sequence = sequence; }

    public boolean isOptional() { return isOptional; }
    public void setOptional(boolean isOptional) { this.isOptional = isOptional; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean isActive) { this.isActive = isActive; }

    public String getComponents() { return components; }
    public void setComponents(String components) { this.components = components; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
