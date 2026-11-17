package construction.cost;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CostCategoryRepository implements PanacheRepositoryBase<CostCategory, String> {

    public List<CostCategory> findAllOrdered() {
        return findAll().list();
    }
}
