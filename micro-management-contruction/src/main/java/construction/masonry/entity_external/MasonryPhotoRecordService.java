package construction.masonry.entity_external;

import construction.components.photo.PhotoCategory;
import construction.components.photo.PhotoRecordDTO;
import construction.masonry.Masonry;
import construction.masonry.MasonryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class MasonryPhotoRecordService {

    @Inject
    MasonryPhotoRecordRepository repository;

    @Inject
    MasonryRepository masonryRepository;
    
    @Transactional
    public void saveAll(List<PhotoRecordDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }

        Masonry masonry = masonryRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Masonry não encontrada com ID: " + phaseId));
        
        for (PhotoRecordDTO dto : dtos) {
            MasonryPhotoRecord entity = mapToEntity(dto);
            
            if (dto.getId() == null || dto.getId().isBlank()) {
                entity.setId(UUID.randomUUID().toString());
            } else {
                entity.setId(dto.getId());
            }
            
            entity.setPhaseId(phaseId);
            entity.setMasonry(masonry); 
            
            repository.persist(entity);
        }
    }

    protected MasonryPhotoRecord mapToEntity(PhotoRecordDTO dto) {
        MasonryPhotoRecord entity = new MasonryPhotoRecord();
        
        entity.setFilePath(dto.getFilePath() != null ? dto.getFilePath() : "caminho/padrao/imagem.jpg");
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