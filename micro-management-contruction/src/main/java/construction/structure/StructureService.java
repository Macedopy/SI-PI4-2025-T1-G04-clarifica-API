package construction.structure;

import java.util.Optional;

import construction.structure.entity_external.*;
import construction.terrain_preparation.TerrainPreparation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class StructureService {

    @Inject StructureRepository structureRepository;
    @Inject StructureMaterialService structureMaterialService;
    @Inject StructureToolService structureToolService;
    @Inject StructureMachineryService structureMachineryService;
    @Inject StructureTeamMemberService structureTeamMemberService;
    @Inject StructureExecutedServiceService structureExecutedServiceService;
    @Inject StructurePhotoRecordService structurePhotoRecordService;

    @Transactional
    public void saveStructure(Structure structure){
        if (structure.getName() == null || structure.getName().isBlank()) {
            throw new IllegalArgumentException("Phase name is mandatory.");
        }
        structureRepository.persist(structure);
    }

    @Transactional
    public String updateStructure(String structureId, StructureDTO dto) {
        Structure structure = structureRepository.findByIdOptional(structureId)
            .orElseThrow(() -> new NotFoundException("Structure not found for ID: " + structureId));
        
        if (dto.getPhaseName() != null && !dto.getPhaseName().isBlank()) {
            structure.setName(dto.getPhaseName());
        }
        if (dto.getContractor() != null) {
            structure.setContractor(dto.getContractor());
        }
        
        // Deleta todos os detalhes antigos antes de recriar
        deleteAllPhaseDetails(structure.getId());
        
        return structure.getId();
    }

    @Transactional
    public void deleteAllPhaseDetails(String structureId) {
        StructureTeamMember.delete("structure.id", structureId);
        StructureExecutedService.delete("structure.id", structureId);
        StructureMachinery.delete("structure.id", structureId);
        StructureMaterial.delete("structure.id", structureId);
        StructureTool.delete("structure.id", structureId);
        StructurePhotoRecord.delete("structure.id", structureId);
    }

    @Transactional
    public void saveAllPhaseDetails(String phaseId, StructureDTO detailsDTO){
        Structure structure = Structure.findById(phaseId);
        if (structure == null) {
            throw new NotFoundException("Structure not found for ID: " + phaseId);
        }
        
        try{
            System.out.println("\n--- INICIANDO saveAllPhaseDetails para phaseId: " + phaseId);
            
            if (detailsDTO.getEquipe() != null && !detailsDTO.getEquipe().isEmpty()){
                System.out.println("  → Salvando " + detailsDTO.getEquipe().size() + " membros da equipe...");
                structureTeamMemberService.saveAll(detailsDTO.getEquipe(), phaseId, structure);
                System.out.println("  ✓ Equipe salva!");
            } else {
                System.out.println("  ⊘ Equipe vazia ou null");
            }

            if (detailsDTO.getServicos() != null && !detailsDTO.getServicos().isEmpty()){
                System.out.println("  → Salvando " + detailsDTO.getServicos().size() + " serviços executados...");
                structureExecutedServiceService.saveAll(detailsDTO.getServicos(), phaseId, structure);
                System.out.println("  ✓ Serviços salvos!");
            }

            if (detailsDTO.getMaquinarios() != null && !detailsDTO.getMaquinarios().isEmpty()){
                System.out.println("  → Salvando " + detailsDTO.getMaquinarios().size() + " maquinários...");
                structureMachineryService.saveAll(detailsDTO.getMaquinarios(), phaseId, structure);
                System.out.println("  ✓ Maquinários salvos!");
            }

            if (detailsDTO.getMateriais() != null && !detailsDTO.getMateriais().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMateriais().size() + " materiais...");
                structureMaterialService.saveAll(detailsDTO.getMateriais(), phaseId, structure);
                System.out.println("  ✓ Materiais salvos!");
            }

            if (detailsDTO.getFerramentas() != null && !detailsDTO.getFerramentas().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFerramentas().size() + " ferramentas...");
                structureToolService.saveAll(detailsDTO.getFerramentas(), phaseId, structure);
                System.out.println("  ✓ Ferramentas salvas!");
            }

            if (detailsDTO.getFotos() != null && !detailsDTO.getFotos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFotos().size() + " fotos...");
                structurePhotoRecordService.saveAll(detailsDTO.getFotos(), phaseId, structure);
                System.out.println("  ✓ Fotos salvas!");
            }

            System.out.println("--- FIM saveAllPhaseDetails\n");
        } catch (Exception e){
            System.err.println("ERRO em saveAllPhaseDetails: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }   
    }

    public Optional<Structure> getStructureById(String id){
        return structureRepository.findByIdOptional(id);
    }

    public Optional<Structure> getTerrainStructureByCustomerId(String customerId) {
        return structureRepository.find("userId", customerId).firstResultOptional();
    }
}
