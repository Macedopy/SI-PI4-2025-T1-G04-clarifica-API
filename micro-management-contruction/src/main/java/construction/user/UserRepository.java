package construction.user;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User, String> { 
    public List<User> findAllCustomers() {
        return listAll(); 
    }
}