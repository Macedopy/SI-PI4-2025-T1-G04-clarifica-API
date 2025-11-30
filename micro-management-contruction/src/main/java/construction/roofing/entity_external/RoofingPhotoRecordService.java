package construction.roofing.entity_external;

import construction.components.photo.PhotoCategory;
import construction.components.photo.PhotoRecordDTO;
import construction.roofing.Roofing;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class RoofingPhotoRecordService {

    @Inject
    RoofingPhotoRecordRepository repository;

    private RoofingPhotoRecord mapToEntity(PhotoRecordDTO dto, String phaseId, Roofing roofing) {
        RoofingPhotoRecord entity = new RoofingPhotoRecord();
        
        entity.setId(UUID.randomUUID().toString());
        entity.setPhaseId(phaseId);
        entity.setRoofing(roofing);
        
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
    public void saveAll(List<PhotoRecordDTO> dtos, String phaseId, Roofing roofing) {
        if (dtos == null || dtos.isEmpty()) return;

        List<RoofingPhotoRecord> entities = dtos.stream()
            .map(dto -> mapToEntity(dto, phaseId, roofing))
            .collect(Collectors.toList());

        RoofingPhotoRecord.persist(entities);
    }
}
