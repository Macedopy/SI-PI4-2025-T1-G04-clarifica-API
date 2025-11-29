package construction.roofing.entity_external;

import construction.components.photo.PhotoCategory;
import construction.components.photo.PhotoRecordDTO;
import construction.roofing.Roofing; // Assumindo existência
import construction.roofing.RoofingRepository; // Assumindo existência
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RoofingPhotoRecordService {

    @Inject
    RoofingPhotoRecordRepository repository;

    @Inject
    RoofingRepository roofingRepository;
    
    @Transactional
    public void saveAll(List<PhotoRecordDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }

        // Busca a entidade pai (Roofing) usando o phaseId (assumindo que phaseId é o ID do Roofing)
        Roofing roofing = roofingRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Roofing não encontrado com ID: " + phaseId));
        
        for (PhotoRecordDTO dto : dtos) {
            RoofingPhotoRecord entity = mapToEntity(dto);
            
            if (dto.getId() == null || dto.getId().isBlank()) {
                entity.setId(UUID.randomUUID().toString());
            } else {
                entity.setId(dto.getId());
            }
            
            entity.setPhaseId(phaseId);
            entity.setRoofing(roofing); 
            
            repository.persist(entity);
        }
    }

    protected RoofingPhotoRecord mapToEntity(PhotoRecordDTO dto) {
        RoofingPhotoRecord entity = new RoofingPhotoRecord();
        
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