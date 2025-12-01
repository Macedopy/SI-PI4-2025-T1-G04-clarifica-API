package construction.structure;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/structure")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StructureController {

    @Inject
    StructureService structureService;

    // ============== CREATE (POST) ==============
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createStructureAndDetails(StructureDTO detailsDTO) {
        String phaseId = UUID.randomUUID().toString();
        
        try {
            System.out.println("========== INICIANDO SALVAMENTO STRUCTURE ==========");
            
            Structure structure = new Structure();
            structure.setId(phaseId);
            structure.setName(detailsDTO.getPhaseName());
            structure.setContractor(detailsDTO.getContractor());

            structureService.saveStructure(structure);
            structureService.saveAllPhaseDetails(phaseId, detailsDTO);
            
            System.out.println("========== STRUCTURE SALVA COM SUCESSO ==========\n");

            return Response.status(Response.Status.CREATED)
                    .entity(new ResponseDTO("Fase Structure criada com sucesso", phaseId))
                    .build();

        } catch (Exception e) {
            System.err.println("========== ERRO NO SALVAMENTO STRUCTURE ==========");
            e.printStackTrace();

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorDTO("Erro ao salvar Structure", e.getMessage()))
                    .build();
        }
    }

    // ============== FULL UPDATE (PUT /{id}) ==============
    
    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateStructure(@PathParam("id") String id, StructureDTO detailsDTO) {
        try {
            System.out.println("========== INICIANDO UPDATE COMPLETO STRUCTURE ==========");
            
            // 1. Atualiza campos principais e deleta detalhes antigos
            String structureId = structureService.updateStructure(id, detailsDTO);

            // 2. Recria todos os detalhes com os novos dados
            structureService.saveAllPhaseDetails(structureId, detailsDTO);
            
            System.out.println("========== UPDATE CONCLUÍDO ==========");

            return Response.status(Response.Status.NO_CONTENT).build();

        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao atualizar: " + e.getMessage())
                    .build();
        }
    }

    // ============== DETAILS ONLY UPDATE (PUT /{id}/details) ==============
    
    @PUT
    @Path("/{id}/details")
    @Transactional
    public Response updateStructureDetails(@PathParam("id") String phaseId, StructureDTO detailsDTO) {
        try {
            // 1. Deleta os detalhes existentes
            structureService.deleteAllPhaseDetails(phaseId);
            
            // 2. Salva/Recria os novos detalhes
            structureService.saveAllPhaseDetails(phaseId, detailsDTO);
            
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao atualizar detalhes: " + e.getMessage())
                    .build();
        }
    }

    // ============== READ (GET) ==============
    
    @GET
    @Path("/{id}")
    public Response getStructure(@PathParam("id") String id) {
        Optional<Structure> structure = structureService.getTerrainStructureByCustomerId(id);
        
        if (structure.isPresent()) {
            return Response.ok(structure.get()).build();
        }
        
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // ============== DTOs de Resposta ==============
    
    public static class ResponseDTO {
        public String message;
        public String phaseId;

        public ResponseDTO(String message, String phaseId) {
            this.message = message;
            this.phaseId = phaseId;
        }
    }

    public static class ErrorDTO {
        public String error;
        public String message;

        public ErrorDTO(String error, String message) {
            this.error = error;
            this.message = message;
        }
    }
}
