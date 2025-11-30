package construction.user;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User, String> {

    public List<User> findAllCustomers() {
        return find("role", UserRole.CLIENT).list();
    }
}