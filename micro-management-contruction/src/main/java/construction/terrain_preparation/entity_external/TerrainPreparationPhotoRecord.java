package construction.terrain_preparation.entity_external;

import com.fasterxml.jackson.annotation.JsonIgnore;

import construction.components.photo.PhotoCategory;
import construction.structure.Structure;
import construction.terrain_preparation.TerrainPreparation;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "terrain_preparation_photo_records")
public class TerrainPreparationPhotoRecord extends PanacheEntityBase {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terrainPreparation_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private TerrainPreparation terrainPreparation;
    
    @Transient
    private String phaseId;

    @Column(name = "file_path", nullable = false, length = 1000)
    private String filePath;

    @Column(length = 500)
    private String caption;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PhotoCategory category;

    @Column(name = "uploaded_at", nullable = false)
    private java.time.LocalDateTime uploadedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public TerrainPreparation getTerrainPreparation() { return terrainPreparation; }
    public void setTerrainPreparation(TerrainPreparation terrainPreparation) { this.terrainPreparation = terrainPreparation; }

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