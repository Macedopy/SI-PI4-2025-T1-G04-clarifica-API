package construction.foundation;

import construction.components.team_present.TeamMemberDTO;
import construction.components.executed_services.ExecutedServiceDTO;
import construction.components.machinery.MachineryDTO;
import construction.components.used_material.MaterialDTO;
import construction.components.tools.ToolDTO;
import construction.components.photo.PhotoRecordDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FoundationDTO {
    @JsonProperty("phaseName")
    private String phaseName;
    @JsonProperty("contractor")
    private String contractor;

    @JsonProperty("equipe")
    private List<TeamMemberDTO> equipe;
    @JsonProperty("servicos")
    private List<ExecutedServiceDTO> servicos;
    @JsonProperty("maquinarios")
    private List<MachineryDTO> maquinarios;
    @JsonProperty("materiais")
    private List<MaterialDTO> materiais;
    @JsonProperty("ferramentas")
    private List<ToolDTO> ferramentas;
    @JsonProperty("fotos")
    private List<PhotoRecordDTO> fotos;

    public FoundationDTO() {
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public List<TeamMemberDTO> getEquipe() {
        return equipe;
    }

    public void setEquipe(List<TeamMemberDTO> equipe) {
        this.equipe = equipe;
    }

    public List<ExecutedServiceDTO> getServicos() {
        return servicos;
    }

    public void setServicos(List<ExecutedServiceDTO> servicos) {
        this.servicos = servicos;
    }

    public List<MachineryDTO> getMaquinarios() {
        return maquinarios;
    }

    public void setMaquinarios(List<MachineryDTO> maquinarios) {
        this.maquinarios = maquinarios;
    }

    public List<MaterialDTO> getMateriais() {
        return materiais;
    }

    public void setMateriais(List<MaterialDTO> materiais) {
        this.materiais = materiais;
    }

    public List<ToolDTO> getFerramentas() {
        return ferramentas;
    }

    public void setFerramentas(List<ToolDTO> ferramentas) {
        this.ferramentas = ferramentas;
    }

    public List<PhotoRecordDTO> getFotos() {
        return fotos;
    }

    public void setFotos(List<PhotoRecordDTO> fotos) {
        this.fotos = fotos;
    }
}
