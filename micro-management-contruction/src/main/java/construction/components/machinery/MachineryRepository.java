package construction.components.machinery;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MachineryRepository implements PanacheRepository<Machinery> {
}
