package construction.masonry.entity_external;

import construction.components.photo.PhotoCategory;
import construction.components.photo.PhotoRecordDTO;
import construction.masonry.Masonry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class MasonryPhotoRecordService {

    @Inject
    MasonryPhotoRecordRepository repository;

    private MasonryPhotoRecord mapToEntity(PhotoRecordDTO dto, String phaseId, Masonry masonry) {
        MasonryPhotoRecord entity = new MasonryPhotoRecord();
        
        // ✅ Gera novo ID sempre
        entity.setId(UUID.randomUUID().toString());
        
        entity.setPhaseId(phaseId);
        entity.setMasonry(masonry);
        
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

    @Transactional
    public void saveAll(List<PhotoRecordDTO> dtos, String phaseId, Masonry masonry) {
        if (dtos == null || dtos.isEmpty()) return;

        List<MasonryPhotoRecord> entities = dtos.stream()
            .map(dto -> mapToEntity(dto, phaseId, masonry))
            .collect(Collectors.toList());

        MasonryPhotoRecord.persist(entities);
    }
}
