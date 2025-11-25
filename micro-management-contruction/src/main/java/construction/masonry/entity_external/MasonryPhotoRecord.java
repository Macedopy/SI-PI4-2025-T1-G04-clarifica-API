package construction.masonry.entity_external;

import construction.components.photo.PhotoCategory;
import construction.masonry.Masonry;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
// Import @NotBlank removido propositalmente

@Entity
@Table(name = "masonry_photo_records")
public class MasonryPhotoRecord extends PanacheEntityBase {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "masonry_id", referencedColumnName = "id", nullable = false)
    private Masonry masonry;
    
    @Transient
    private String phaseId; 

    // @NotBlank removido para evitar erro de validação Java
    @Column(name = "file_path", nullable = false, length = 1000)
    private String filePath;

    @Column(length = 500)
    private String caption;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PhotoCategory category;

    @Column(name = "uploaded_at", nullable = false)
    private java.time.LocalDateTime uploadedAt;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Masonry getMasonry() { return masonry; }
    public void setMasonry(Masonry masonry) { this.masonry = masonry; }

    public String getPhaseId() { return phaseId; }
    public void setPhaseId(String phaseId) { this.phaseId = phaseId; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public PhotoCategory getCategory() { return category; }
    public void setCategory(PhotoCategory category) { this.category = category; }

    public java.time.LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(java.time.LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}