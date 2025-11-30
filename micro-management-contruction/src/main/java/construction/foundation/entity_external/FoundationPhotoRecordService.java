package construction.foundation.entity_external;

import construction.components.photo.PhotoCategory;
import construction.components.photo.PhotoRecordDTO;
import construction.foundation.Foundation;
import construction.foundation.FoundationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FoundationPhotoRecordService {

    @Inject
    FoundationPhotoRecordRepository repository;

    @Inject
    FoundationRepository foundationRepository;
    
    @Transactional
    public void saveAll(List<PhotoRecordDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }

        Foundation foundation = foundationRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Foundation não encontrada com ID: " + phaseId));
        
        for (PhotoRecordDTO dto : dtos) {
            FoundationPhotoRecord entity = mapToEntity(dto);
            entity.setId(java.util.UUID.randomUUID().toString());
            
            entity.setPhaseId(phaseId);
            entity.setFoundation(foundation); 
            
            repository.persist(entity);
        }
    }

    protected FoundationPhotoRecord mapToEntity(PhotoRecordDTO dto) {
        FoundationPhotoRecord entity = new FoundationPhotoRecord();
        
        entity.setFilePath(dto.getFilePath());
        entity.setCaption(dto.getCaption());
        
        entity.setCategory(PhotoCategory.fromString(dto.getCategory()));

        if (dto.getUploadedAt() != null) {
            try {
                entity.setUploadedAt(LocalDateTime.parse(dto.getUploadedAt()));
            } catch (Exception e) {
                entity.setUploadedAt(LocalDateTime.now());
            }
        } else {
            entity.setUploadedAt(LocalDateTime.now());
        }
        
        return entity;
    }
}