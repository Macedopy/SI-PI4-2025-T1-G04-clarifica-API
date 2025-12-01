package construction.coatings.entity_external;

import construction.coatings.Coatings;
import construction.components.photo.PhotoCategory;
import construction.components.photo.PhotoRecordDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class CoatingsPhotoRecordService {

    @Inject
    CoatingsPhotoRecordRepository repository;

    private CoatingsPhotoRecord mapToEntity(PhotoRecordDTO dto, String phaseId, Coatings coatings) {
        CoatingsPhotoRecord entity = new CoatingsPhotoRecord();
        
        entity.setId(UUID.randomUUID().toString());
        entity.setPhaseId(phaseId);
        entity.setCoatings(coatings);
        
        entity.setFilePath(
            dto.getFilePath() != null && !dto.getFilePath().isBlank() 
            ? dto.getFilePath() 
            : "caminho/padrao/imagem_revestimento.jpg"
        );
        
        entity.setCaption(dto.getCaption());
        
        try {
            entity.setCategory(PhotoCategory.fromString(dto.getCategory()));
        } catch (Exception e) {
            entity.setCategory(PhotoCategory.PROGRESS);
        }

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

    @Transactional
    public void saveAll(List<PhotoRecordDTO> dtos, String phaseId, Coatings coatings) {
        if (dtos == null || dtos.isEmpty()) return;

        List<CoatingsPhotoRecord> entities = dtos.stream()
            .map(dto -> mapToEntity(dto, phaseId, coatings))
            .collect(Collectors.toList());

        CoatingsPhotoRecord.persist(entities);
    }
}
