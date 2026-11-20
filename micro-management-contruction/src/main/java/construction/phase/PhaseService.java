package construction.phase;

import construction.core.Construction;
import construction.core.Phase;
import construction.core.TemplatePhase;
import construction.user.User;
import construction.user.UserRole;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.text.Normalizer;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PhaseService {

    @Transactional
    public Phase createPhase(String constructionId, String name, String contractor, String icon) {
        // A UI envia o id do usuário; aceitamos tanto constructionId quanto userId.
        Construction construction = Construction.findById(resolveConstructionId(constructionId));
        if (construction == null) {
            throw new NotFoundException("Construction not found with ID: " + constructionId);
        }

        // Validar que não existe fase com o mesmo nome nesta construção
        boolean exists = Phase.find("construction.id = ?1 AND name = ?2", construction.getId(), name).firstResult() != null;
        if (exists) {
            throw new IllegalArgumentException(
                    "A phase with name '" + name + "' already exists for this construction."
            );
        }

        Phase phase = new Phase();
        phase.setName(name);
        phase.setContractor(contractor);
        phase.setIcon(icon);
        phase.setConstruction(construction);

        // Associar ao template_phase se a construção tem template
        if (construction.getTemplateId() != null) {
            String normalizedName = normalize(name);
            TemplatePhase tp = TemplatePhase.find(
                    "template_id = ?1 AND phase_id = ?2",
                    construction.getTemplateId(), normalizedName
            ).firstResult();
            if (tp != null) {
                phase.setTemplatePhaseId(tp.getId());
            }
        }

        phase.persist();
        return phase;
    }

    private String normalize(String s) {
        if (s == null) return null;
        String n = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "");
        return n;
    }

    public String resolveConstructionId(String idOrUserId) {
        Construction construction = Construction.findById(idOrUserId);
        if (construction == null) {
            construction = firstConstructionOf(User.findById(idOrUserId));
        }
        if (construction == null) {
            throw new NotFoundException("Construction not found with ID: " + idOrUserId);
        }
        return construction.getId();
    }

    private Construction firstConstructionOf(User user) {
        if (user == null) {
            return null;
        }
        if (user.getRole() == UserRole.OWNER) {
            return Construction.find("owner", user).firstResult();
        }
        if (user.getRole() == UserRole.CLIENT) {
            return Construction.find(
                    "select c from Construction c join c.clients cl where cl.id = ?1", user.getId()).firstResult();
        }
        return Construction.find(
                "select c from Construction c join c.masterBuilders mb where mb.id = ?1", user.getId()).firstResult();
    }

    @Transactional
    public List<Phase> createPhasesBatch(String constructionId, List<PhaseCreationRequest> requests) {
        // Validar construção uma única vez
        Construction construction = Construction.findById(constructionId);
        if (construction == null) {
            throw new NotFoundException("Construction not found with ID: " + constructionId);
        }

        // Validar nomes duplicados dentro do lote e contra o banco
        List<String> names = requests.stream()
                .map(PhaseCreationRequest::getName)
                .collect(Collectors.toList());

        long duplicateInBatch = names.stream()
                .filter(name -> java.util.Collections.frequency(names, name) > 1)
                .count();
        if (duplicateInBatch > 0) {
            throw new IllegalArgumentException("Duplicate phase names within the same batch are not allowed.");
        }

        for (PhaseCreationRequest req : requests) {
            boolean exists = Phase.find("construction.id = ?1 AND name = ?2", constructionId, req.getName()).firstResult() != null;
            if (exists) {
                throw new IllegalArgumentException(
                        "A phase with name '" + req.getName() + "' already exists for this construction."
                );
            }
        }

        // Criar todas as fases
        List<Phase> phases = requests.stream().map(req -> {
            Phase phase = new Phase();
            phase.setName(req.getName());
            phase.setContractor(req.getContractor());
            phase.setConstruction(construction);
            return phase;
        }).collect(Collectors.toList());

        Phase.persist(phases);
        return phases;
    }

    @Transactional
    public void deletePhase(String phaseId) {
        Phase phase = Phase.findById(phaseId);
        if (phase == null) {
            throw new NotFoundException("Phase not found with ID: " + phaseId);
        }
        phase.delete();
    }

    /**
     * DTO interno para criação em lote de fases.
     */
    public static class PhaseCreationRequest {
        private String name;
        private String contractor;

        public PhaseCreationRequest() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getContractor() { return contractor; }
        public void setContractor(String contractor) { this.contractor = contractor; }
    }
}
