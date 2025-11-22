package construction.components.photo;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PhotoRecordRepository implements PanacheRepositoryBase<PhotoRecord, String> {
}
