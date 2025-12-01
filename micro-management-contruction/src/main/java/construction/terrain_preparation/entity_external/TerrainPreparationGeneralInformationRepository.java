package construction.terrain_preparation.entity_external;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TerrainPreparationGeneralInformationRepository implements PanacheRepositoryBase<TerrainPreparationGeneralInformation, String> {
}
