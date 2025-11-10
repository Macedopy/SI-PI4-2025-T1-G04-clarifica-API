package construction.foundation;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class FoundationRepository implements PanacheRepository<Foundation> {
    
    public List<Foundation> findAllCustomers() {
        return listAll();
    }
    
    public Foundation findByIdOptional(String id) {
        return find("id", id).firstResult();
    }
}