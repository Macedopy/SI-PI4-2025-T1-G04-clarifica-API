package construction.core;

import java.util.List;

public class ConstructionDTO {
    private String id;
    private String name;
    private String status;
    private String ownerId;
    private String ownerName;
    private List<UserInfoDTO> clients;
    private List<UserInfoDTO> masterBuilders;
    private int overallProgress;

    public ConstructionDTO() {}

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public List<UserInfoDTO> getClients() { return clients; }
    public void setClients(List<UserInfoDTO> clients) { this.clients = clients; }

    public List<UserInfoDTO> getMasterBuilders() { return masterBuilders; }
    public void setMasterBuilders(List<UserInfoDTO> masterBuilders) { this.masterBuilders = masterBuilders; }

    public int getOverallProgress() { return overallProgress; }
    public void setOverallProgress(int overallProgress) { this.overallProgress = overallProgress; }

    public static class UserInfoDTO {
        public String id;
        public String name;
        public String email;

        public UserInfoDTO(String id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
    }
}
