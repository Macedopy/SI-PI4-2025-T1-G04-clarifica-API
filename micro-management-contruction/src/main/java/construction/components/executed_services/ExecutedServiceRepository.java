package construction.components.executed_services;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExecutedServiceRepository implements PanacheRepository<ExecutedService> {
}
