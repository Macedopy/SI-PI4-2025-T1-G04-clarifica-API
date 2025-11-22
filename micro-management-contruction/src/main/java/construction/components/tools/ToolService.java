package construction.components.tools;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ToolService {

    @Inject
    ToolRepository toolRepository;

    @Transactional
    public void saveTools(List<Tool> tools) {
        toolRepository.persist(tools);
    }
    
    public List<Tool> findByPhaseId(String phaseId) {
        return toolRepository.list("phaseId", phaseId);
    }

    @Transactional
    public Tool updateTool(Tool tool) {
        toolRepository.persist(tool);
        return tool;
    }
}
