package construction.components.photo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PhotoRecordService {

    @Inject
    PhotoRecordRepository photoRecordRepository;

    @Transactional
    public void savePhotos(List<PhotoRecord> photos) {
        photoRecordRepository.persist(photos);
    }

    public List<PhotoRecord> findByPhaseId(String phaseId) {
        return photoRecordRepository.list("phaseId", phaseId);
    }
    
    public Optional<PhotoRecord> findById(String id) {
        return photoRecordRepository.findByIdOptional(id);
    }

    @Transactional
    public boolean deletePhoto(String id) {
        return photoRecordRepository.deleteById(id);
    }
    
    public List<PhotoRecord> findByPhaseIdAndCategory(String phaseId, PhotoCategory category) {
        return photoRecordRepository.list("phaseId = ?1 and category = ?2", phaseId, category);
    }
}
