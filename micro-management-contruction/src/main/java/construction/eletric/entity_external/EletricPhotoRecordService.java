package construction.eletric.entity_external;

import construction.components.photo.PhotoCategory;
import construction.components.photo.PhotoRecordDTO;
import construction.eletric.Eletric;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class EletricPhotoRecordService {

    @Inject
    EletricPhotoRecordRepository repository;

    private EletricPhotoRecord mapToEntity(PhotoRecordDTO dto, String phaseId, Eletric eletric) {
        EletricPhotoRecord entity = new EletricPhotoRecord();
        
        entity.setId(UUID.randomUUID().toString());
        entity.setPhaseId(phaseId);
        entity.setEletric(eletric);
        
        entity.setFilePath(
            dto.getFilePath() != null && !dto.getFilePath().isBlank() 
            ? dto.getFilePath() 
            : "caminho/padrao/imagem_eletrica.jpg"
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
    public void saveAll(List<PhotoRecordDTO> dtos, String phaseId, Eletric eletric) {
        if (dtos == null || dtos.isEmpty()) return;

        List<EletricPhotoRecord> entities = dtos.stream()
            .map(dto -> mapToEntity(dto, phaseId, eletric))
            .collect(Collectors.toList());

        EletricPhotoRecord.persist(entities);
    }
}
