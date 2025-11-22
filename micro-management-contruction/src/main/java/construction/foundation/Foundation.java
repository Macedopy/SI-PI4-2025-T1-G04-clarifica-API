package construction.foundation;

import construction.foundation.entity_external.FoundationTeamMember;
import construction.foundation.entity_external.FoundationMaterial;
import construction.foundation.entity_external.FoundationTool;
import construction.foundation.entity_external.FoundationMachinery;
import construction.foundation.entity_external.FoundationExecutedService;
import construction.foundation.entity_external.FoundationPhotoRecord;

import jakarta.persistence.*;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.List;

@Entity
@Table(name = "foundations")
public class Foundation extends PanacheEntityBase {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contractor;

    // Relacionamentos - CORRIGIDO: mappedBy aponta para o atributo da entidade relacionada
    @OneToMany(mappedBy = "foundation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FoundationTeamMember> teamMembers;

    // COMENTADO PARA TESTES
    //@OneToMany(mappedBy = "foundation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    //private List<FoundationMaterial> materials;

    //@OneToMany(mappedBy = "foundation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    //private List<FoundationTool> tools;

    //@OneToMany(mappedBy = "foundation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    //private List<FoundationMachinery> machineries;

    //@OneToMany(mappedBy = "foundation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    //private List<FoundationExecutedService> executedServices;

    //@OneToMany(mappedBy = "foundation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    //private List<FoundationPhotoRecord> photoRecords;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContractor() { return contractor; }
    public void setContractor(String contractor) { this.contractor = contractor; }

    public List<FoundationTeamMember> getTeamMembers() { return teamMembers; }
    public void setTeamMembers(List<FoundationTeamMember> teamMembers) { this.teamMembers = teamMembers; }

    // COMENTADO PARA TESTES
    //public List<FoundationMaterial> getMaterials() { return materials; }
    //public void setMaterials(List<FoundationMaterial> materials) { this.materials = materials; }

    //public List<FoundationTool> getTools() { return tools; }
    //public void setTools(List<FoundationTool> tools) { this.tools = tools; }

    //public List<FoundationMachinery> getMachineries() { return machineries; }
    //public void setMachineries(List<FoundationMachinery> machineries) { this.machineries = machineries; }

    //public List<FoundationExecutedService> getExecutedServices() { return executedServices; }
    //public void setExecutedServices(List<FoundationExecutedService> executedServices) { this.executedServices = executedServices; }

    //public List<FoundationPhotoRecord> getPhotoRecords() { return photoRecords; }
    //public void setPhotoRecords(List<FoundationPhotoRecord> photoRecords) { this.photoRecords = photoRecords; }
}
