package construction.phase;

import java.util.List;

public class PhaseRequestDTO {
    private String constructionId;
    private String requesterId;
    private String name;
    private String contractor;
    private String icon;
    private List<String> components;
    private List<String> phases;

    public PhaseRequestDTO() {}

    public String getConstructionId() { return constructionId; }
    public void setConstructionId(String constructionId) { this.constructionId = constructionId; }

    public String getRequesterId() { return requesterId; }
    public void setRequesterId(String requesterId) { this.requesterId = requesterId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContractor() { return contractor; }
    public void setContractor(String contractor) { this.contractor = contractor; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public List<String> getComponents() { return components; }
    public void setComponents(List<String> components) { this.components = components; }

    public List<String> getPhases() { return phases; }
    public void setPhases(List<String> phases) { this.phases = phases; }
}
