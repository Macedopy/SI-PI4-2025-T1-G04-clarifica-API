package construction.terrain_preparation;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TerrainPreparationRepository implements PanacheRepositoryBase<TerrainPreparation, String> {
}
