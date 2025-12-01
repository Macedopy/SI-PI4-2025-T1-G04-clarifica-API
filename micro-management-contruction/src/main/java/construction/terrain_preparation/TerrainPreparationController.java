package construction.terrain_preparation;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/terrain-preparation")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TerrainPreparationController {
    
    @Inject
    TerrainPreparationService terrainPreparationService;

    // ============== CREATE (POST) ==============
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createTerrainPreparationAndDetails(TerrainPreparationDTO detailsDTO) {
        String phaseId = UUID.randomUUID().toString();
        
        try {
            System.out.println("========== INICIANDO SALVAMENTO TERRAIN PREPARATION ==========");
            
            TerrainPreparation terrainPreparation = new TerrainPreparation();
            terrainPreparation.setId(phaseId);
            terrainPreparation.setName(detailsDTO.getPhaseName());
            terrainPreparation.setContractor(detailsDTO.getContractor());
            
            terrainPreparationService.saveTerrainPreparation(terrainPreparation);
            terrainPreparationService.saveAllPhaseDetails(phaseId, detailsDTO);
            
            System.out.println("========== TERRAIN PREPARATION SALVO COM SUCESSO ==========\n");
            
            return Response.status(Response.Status.CREATED)
                           .entity(new ResponseDTO("Fase Terrain Preparation criada com sucesso", phaseId))
                           .build();
                             
        } catch (Exception e) {
            System.err.println("========== ERRO NO SALVAMENTO TERRAIN PREPARATION ==========");
            e.printStackTrace();
            
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ErrorDTO("Erro ao salvar Terrain Preparation", e.getMessage()))
                           .build();
        }
    }

    // ============== FULL UPDATE (PUT /{customerId}) ==============
    
    @PUT
    @Path("/{customerId}")
    @Transactional
    public Response updateTerrainPreparation(@PathParam("customerId") String customerId, TerrainPreparationDTO detailsDTO) {
        try {
            System.out.println("========== INICIANDO UPDATE COMPLETO TERRAIN PREPARATION ==========");
            
            String terrainPreparationId = terrainPreparationService.updateTerrainPreparation(customerId, detailsDTO);
            terrainPreparationService.saveAllPhaseDetails(terrainPreparationId, detailsDTO);
            
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
    public Response updateTerrainPreparationDetails(@PathParam("id") String phaseId, TerrainPreparationDTO detailsDTO) {
        try {
            terrainPreparationService.deleteAllPhaseDetails(phaseId);
            terrainPreparationService.saveAllPhaseDetails(phaseId, detailsDTO);
            
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
    public Response getTerrainPreparation(@PathParam("id") String id) {
        Optional<TerrainPreparation> terrainPreparation = terrainPreparationService.getTerrainPreparationByCustomerId(id);

        if (terrainPreparation.isPresent()) {
            return Response.ok(terrainPreparation.get()).build();
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
