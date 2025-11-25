package construction.masonry.entity_external;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MasonryMachineryRepository implements PanacheRepository<MasonryMachinery> {
}