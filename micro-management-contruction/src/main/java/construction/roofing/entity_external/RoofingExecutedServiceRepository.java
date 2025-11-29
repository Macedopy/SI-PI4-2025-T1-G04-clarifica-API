package construction.roofing.entity_external;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoofingExecutedServiceRepository implements PanacheRepository<RoofingExecutedService> {
}