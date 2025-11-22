package construction.components.tools;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ToolRepository implements PanacheRepository<Tool> {
}
