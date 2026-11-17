package construction.phase;

import construction.core.Phase;
import construction.user.User;
import construction.user.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class PhaseAnnotationService {

    @Inject
    PhaseAnnotationRepository annotationRepository;

    @Inject
    UserRepository userRepository;

    @Transactional
    public PhaseAnnotation createAnnotation(String phaseId, String createdById, String title, String content,
                                            boolean isPublic, String componentType, String componentId) {
        Phase phase = Phase.findById(phaseId);
        if (phase == null) {
            throw new NotFoundException("Phase not found with ID: " + phaseId);
        }

        User createdBy = userRepository.findById(createdById);
        if (createdBy == null) {
            throw new NotFoundException("User not found with ID: " + createdById);
        }

        PhaseAnnotation annotation = new PhaseAnnotation();
        annotation.setPhase(phase);
        annotation.setPhaseId(phaseId);
        annotation.setCreatedBy(createdBy);
        annotation.setCreatedById(createdById);
        annotation.setTitle(title);
        annotation.setContent(content);
        annotation.setPublic(isPublic);
        annotation.setComponentType(componentType);
        annotation.setComponentId(componentId);

        annotationRepository.persist(annotation);
        return annotation;
    }

    @Transactional
    public PhaseAnnotation updateAnnotation(String annotationId, String userId, String title, String content,
                                            boolean isPublic) {
        PhaseAnnotation annotation = annotationRepository.findById(annotationId);
        if (annotation == null) {
            throw new NotFoundException("Annotation not found with ID: " + annotationId);
        }

        // Apenas o criador pode editar
        if (!annotation.getCreatedById().equals(userId)) {
            throw new IllegalArgumentException("Only the author can edit this annotation.");
        }

        annotation.setTitle(title);
        annotation.setContent(content);
        annotation.setPublic(isPublic);

        annotationRepository.persist(annotation);
        return annotation;
    }

    @Transactional
    public void deleteAnnotation(String annotationId, String userId) {
        PhaseAnnotation annotation = annotationRepository.findById(annotationId);
        if (annotation == null) {
            throw new NotFoundException("Annotation not found with ID: " + annotationId);
        }

        // Apenas o criador pode deletar
        if (!annotation.getCreatedById().equals(userId)) {
            throw new IllegalArgumentException("Only the author can delete this annotation.");
        }

        annotationRepository.delete(annotation);
    }

    public List<PhaseAnnotation> listByPhase(String phaseId) {
        return annotationRepository.findByPhase(phaseId);
    }

    public List<PhaseAnnotation> listPublicByPhase(String phaseId) {
        return annotationRepository.findPublicByPhase(phaseId);
    }

    public List<PhaseAnnotation> listByPhaseAndComponent(String phaseId, String componentType, String componentId) {
        return annotationRepository.findByPhaseAndComponent(phaseId, componentType, componentId);
    }

    public List<PhaseAnnotation> listByCreatedBy(String userId) {
        return annotationRepository.findByCreatedBy(userId);
    }
}
