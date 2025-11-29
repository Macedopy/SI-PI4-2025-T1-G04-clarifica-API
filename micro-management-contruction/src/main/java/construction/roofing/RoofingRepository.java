package construction.roofing;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoofingRepository implements PanacheRepositoryBase<Roofing, String> {
}