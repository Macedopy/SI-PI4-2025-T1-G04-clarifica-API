package construction.foundation.entity_external;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FoundationToolRepository implements PanacheRepository<FoundationTool> {
}