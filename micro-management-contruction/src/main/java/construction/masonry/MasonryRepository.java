package construction.masonry;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MasonryRepository implements PanacheRepositoryBase<Masonry, String> {
}