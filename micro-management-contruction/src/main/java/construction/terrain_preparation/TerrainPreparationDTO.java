package construction.terrain_preparation;

import construction.components.executed_services.ExecutedServiceDTO;
import construction.components.machinery.MachineryDTO;
import construction.components.photo.PhotoRecordDTO;
import construction.components.team_present.TeamMemberDTO;
import construction.components.used_material.MaterialDTO;

import java.time.LocalDate;
import java.util.List;

public class TerrainPreparationDTO {
    
    private String phaseName;
    private String contractor;
    private GeneralInfoDTO geral;
    
    private List<TeamMemberDTO> equipe;
    private List<ExecutedServiceDTO> servicos;
    private List<MachineryDTO> maquinarios;
    private List<MaterialDTO> materiais;
    private List<PhotoRecordDTO> fotos;

    // Getters e Setters
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

    public GeneralInfoDTO getGeral() {
        return geral;
    }

    public void setGeral(GeneralInfoDTO geral) {
        this.geral = geral;
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

    public List<PhotoRecordDTO> getFotos() {
        return fotos;
    }

    public void setFotos(List<PhotoRecordDTO> fotos) {
        this.fotos = fotos;
    }

    // DTO interno para informações gerais
    public static class GeneralInfoDTO {
        private String endereco;
        private Double areaTerreno;
        private String topografia; // 'plana', 'inclinada', 'acidentada', 'com desnível'
        private LocalDate dataInicio;
        private String responsavel;
        private String observacao;

        // Getters e Setters
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
}
