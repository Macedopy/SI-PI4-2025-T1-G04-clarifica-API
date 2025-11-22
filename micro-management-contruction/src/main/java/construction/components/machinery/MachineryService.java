package construction.components.machinery;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class MachineryService {

    @Inject
    MachineryRepository machineryRepository;

    @Transactional
    public void saveMachinery(List<Machinery> machineryList) {
        machineryRepository.persist(machineryList);
    }

    public List<Machinery> findByPhaseId(String phaseId) {
        return machineryRepository.list("phaseId", phaseId);
    }
}
