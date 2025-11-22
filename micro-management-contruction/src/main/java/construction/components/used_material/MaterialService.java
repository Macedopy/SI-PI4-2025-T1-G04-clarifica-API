package construction.components.used_material;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class MaterialService {

    @Inject
    MaterialRepository materialRepository;

    @Transactional
    public void saveMaterials(List<Material> materials) {
        materialRepository.persist(materials);
    }
}
