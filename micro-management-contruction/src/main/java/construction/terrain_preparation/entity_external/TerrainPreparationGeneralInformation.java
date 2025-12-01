package construction.terrain_preparation.entity_external;

import construction.terrain_preparation.TerrainPreparation;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "terrain_preparation_general_information")
public class TerrainPreparationGeneralInformation extends PanacheEntityBase {
    
    @Id
    @Column(name = "id")
    private String id;
    
    @Column(name = "phase_id")
    private String phaseId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terrainPreparation_id")
    private TerrainPreparation terrainPreparation;
    
    @Column(name = "endereco", length = 500)
    private String endereco;
    
    @Column(name = "area_terreno")
    private Double areaTerreno;
    
    @Column(name = "topografia", length = 50)
    private String topografia; // 'plana', 'inclinada', 'acidentada', 'com desnível'
    
    @Column(name = "data_inicio")
    private LocalDate dataInicio;
    
    @Column(name = "responsavel", length = 255)
    private String responsavel;
    
    @Column(name = "observacao", length = 1000)
    private String observacao;

    // Getters e Setters
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

    public TerrainPreparation getTerrainPreparation() {
        return terrainPreparation;
    }

    public void setTerrainPreparation(TerrainPreparation terrainPreparation) {
        this.terrainPreparation = terrainPreparation;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Double getAreaTerreno() {
        return areaTerreno;
    }

    public void setAreaTerreno(Double areaTerreno) {
        this.areaTerreno = areaTerreno;
    }

    public String getTopografia() {
        return topografia;
    }

    public void setTopografia(String topografia) {
        this.topografia = topografia;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
