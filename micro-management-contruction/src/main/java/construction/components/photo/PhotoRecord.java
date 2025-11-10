package construction.photo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "photo_records")
public class PhotoRecord extends PanacheEntityBase {

    @Id
    private String id;

    @Column(name = "phase_id", nullable = false)
    private String phaseId;

    @NotBlank(message = "File path or URL is required")
    @Column(name = "file_path", nullable = false, length = 1000)
    private String filePath;

    @Column(length = 500)
    private String caption;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PhotoCategory category;

    @Column(name = "uploaded_at", nullable = false)
    private java.time.LocalDateTime uploadedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(String phaseId) {
        this.phaseId = phaseId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public PhotoCategory getCategory() {
        return category;
    }

    public void setCategory(PhotoCategory category) {
        this.category = category;
    }

    public java.time.LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(java.time.LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}