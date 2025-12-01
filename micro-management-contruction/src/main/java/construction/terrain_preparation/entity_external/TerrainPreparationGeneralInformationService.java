package construction.terrain_preparation.entity_external;

import construction.terrain_preparation.TerrainPreparation;
import construction.terrain_preparation.TerrainPreparationDTO.GeneralInfoDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class TerrainPreparationGeneralInformationService {

    @Inject
    TerrainPreparationGeneralInformationRepository repository;

    private TerrainPreparationGeneralInformation mapToEntity(GeneralInfoDTO dto, String phaseId, TerrainPreparation terrainPreparation) {
        TerrainPreparationGeneralInformation entity = new TerrainPreparationGeneralInformation();
        
        entity.setId(UUID.randomUUID().toString());
        entity.setPhaseId(phaseId);
        entity.setTerrainPreparation(terrainPreparation);
        
        entity.setEndereco(dto.getEndereco() != null ? dto.getEndereco() : "");
        entity.setAreaTerreno(dto.getAreaTerreno() != null ? dto.getAreaTerreno() : 0.0);
        entity.setTopografia(dto.getTopografia() != null ? dto.getTopografia() : "plana");
        entity.setDataInicio(dto.getDataInicio());
        entity.setResponsavel(dto.getResponsavel() != null ? dto.getResponsavel() : "");
        entity.setObservacao(dto.getObservacao() != null ? dto.getObservacao() : "");
        
        return entity;
    }

    @Transactional
    public void save(GeneralInfoDTO dto, String phaseId, TerrainPreparation terrainPreparation) {
        if (dto == null) return;

        TerrainPreparationGeneralInformation entity = mapToEntity(dto, phaseId, terrainPreparation);
        TerrainPreparationGeneralInformation.persist(entity);
    }
}
