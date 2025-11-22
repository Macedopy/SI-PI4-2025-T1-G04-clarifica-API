package construction.service;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractPhaseComponentService<T, D> {

    protected abstract PanacheRepositoryBase<T, String> getRepository();

    protected abstract T mapToEntity(String phaseId, D dto);

    @Transactional
    public void saveAll(List<D> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }
        List<T> entities = dtos.stream()
            .map(dto -> mapToEntity(phaseId, dto))
            .collect(Collectors.toList());
            
        getRepository().persist(entities);
    }
}
