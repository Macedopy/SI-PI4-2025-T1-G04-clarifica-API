//Responsável: Bruno Martins (Tudo do masonry)

package construction.masonry;

import construction.foundation.entity_external.FoundationExecutedService;
import construction.foundation.entity_external.FoundationMachinery;
import construction.foundation.entity_external.FoundationMaterial;
import construction.foundation.entity_external.FoundationPhotoRecord;
import construction.foundation.entity_external.FoundationTeamMember;
import construction.foundation.entity_external.FoundationTool;
import construction.masonry.entity_external.MasonryExecutedService;
import construction.masonry.entity_external.MasonryMachinery;
import construction.masonry.entity_external.MasonryMaterial;
import construction.masonry.entity_external.MasonryPhotoRecord;
import construction.masonry.entity_external.MasonryTeamMember;
import construction.masonry.entity_external.MasonryTool;
import construction.user.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "masonry")
public class Masonry extends PanacheEntityBase {
    
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contractor;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "masonry", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MasonryExecutedService> executedServices;

    @OneToMany(mappedBy = "masonry", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MasonryMaterial> materials = new ArrayList<>();

    @OneToMany(mappedBy = "masonry", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MasonryTool> tools = new ArrayList<>();

    @OneToMany(mappedBy = "masonry", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MasonryMachinery> machinery = new ArrayList<>();

    @OneToMany(mappedBy = "masonry", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MasonryTeamMember> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "masonry", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MasonryPhotoRecord> photoRecords = new ArrayList<>();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContractor() { return contractor; }
    public void setContractor(String contractor) { this.contractor = contractor; }

    public List<MasonryExecutedService> getExecutedServices() { return executedServices; }
    public void setExecutedServices(List<MasonryExecutedService> executedServices) { this.executedServices = executedServices; }
}