package construction.terrain_preparation.entity_external;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TerrainPreparationExecutedRepository implements PanacheRepository<TerrainPreparationExecutedService> {
}