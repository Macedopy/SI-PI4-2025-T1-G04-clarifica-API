package construction.hydraulic.entity_external;

import construction.components.photo.PhotoCategory;
import construction.components.photo.PhotoRecordDTO;
import construction.hydraulic.Hydraulic;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class HydraulicPhotoRecordService {

    @Inject
    HydraulicPhotoRecordRepository repository;

    private HydraulicPhotoRecord mapToEntity(PhotoRecordDTO dto, String phaseId, Hydraulic hydraulic) {
        HydraulicPhotoRecord entity = new HydraulicPhotoRecord();
        
        entity.setId(UUID.randomUUID().toString());
        entity.setPhaseId(phaseId);
        entity.setHydraulic(hydraulic);
        
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
    public void saveAll(List<PhotoRecordDTO> dtos, String phaseId, Hydraulic hydraulic) {
        if (dtos == null || dtos.isEmpty()) return;

        List<HydraulicPhotoRecord> entities = dtos.stream()
            .map(dto -> mapToEntity(dto, phaseId, hydraulic))
            .collect(Collectors.toList());

        HydraulicPhotoRecord.persist(entities);
    }
}
