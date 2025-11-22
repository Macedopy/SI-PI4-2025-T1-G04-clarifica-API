package construction.components.executed_services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ExecutedServiceService {

    @Inject
    ExecutedServiceRepository executedServiceRepository;

    public void saveServices(List<ExecutedService> services) {
        executedServiceRepository.persist(services);
    }

    public List<ExecutedService> findByPhaseId(String phaseId) {
        return executedServiceRepository.list("phaseId", phaseId);
    }
}
