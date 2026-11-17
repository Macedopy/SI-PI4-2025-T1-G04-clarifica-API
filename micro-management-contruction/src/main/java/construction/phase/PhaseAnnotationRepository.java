package construction.phase;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PhaseAnnotationRepository implements PanacheRepositoryBase<PhaseAnnotation, String> {

    public List<PhaseAnnotation> findByPhase(String phaseId) {
        return find("phaseId = ?1 ORDER BY createdAt DESC", phaseId).list();
    }

    public List<PhaseAnnotation> findByPhaseAndComponent(String phaseId, String componentType, String componentId) {
        return find("phaseId = ?1 AND componentType = ?2 AND componentId = ?3 ORDER BY createdAt DESC",
                phaseId, componentType, componentId).list();
    }

    public List<PhaseAnnotation> findPublicByPhase(String phaseId) {
        return find("phaseId = ?1 AND isPublic = true ORDER BY createdAt DESC", phaseId).list();
    }

    public List<PhaseAnnotation> findByCreatedBy(String userId) {
        return find("createdById = ?1 ORDER BY createdAt DESC", userId).list();
    }
}
