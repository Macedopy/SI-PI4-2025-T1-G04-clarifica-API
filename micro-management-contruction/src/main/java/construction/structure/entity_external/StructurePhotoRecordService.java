package construction.structure.entity_external;

import construction.components.photo.PhotoCategory;
import construction.components.photo.PhotoRecordDTO;
import construction.structure.Structure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class StructurePhotoRecordService {

    @Inject
    StructurePhotoRecordRepository repository;

    private StructurePhotoRecord mapToEntity(PhotoRecordDTO dto, String phaseId, Structure structure) {
        StructurePhotoRecord entity = new StructurePhotoRecord();
        
        // ✅ Gera novo ID sempre
        entity.setId(UUID.randomUUID().toString());
        
        entity.setPhaseId(phaseId);
        entity.setStructure(structure);
        
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
    public void saveAll(List<PhotoRecordDTO> dtos, String phaseId, Structure structure) {
        if (dtos == null || dtos.isEmpty()) return;

        List<StructurePhotoRecord> entities = dtos.stream()
            .map(dto -> mapToEntity(dto, phaseId, structure))
            .collect(Collectors.toList());

        StructurePhotoRecord.persist(entities);
    }
}
