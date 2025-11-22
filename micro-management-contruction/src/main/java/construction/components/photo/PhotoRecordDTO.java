package construction.components.photo;

public class PhotoRecordDTO {

    private String id;
    private String filePath;
    private String caption;
    private String category;
    private String uploadedAt;

    public PhotoRecordDTO() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(String uploadedAt) { this.uploadedAt = uploadedAt; }
}
