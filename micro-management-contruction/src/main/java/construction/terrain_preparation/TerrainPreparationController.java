package construction.terrain_preparation;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/terrain-preparation")
// REMOVIDO @Produces e @Consumes DAQUI PARA EVITAR ERROS
public class TerrainPreparationController {
    
    @Inject
    TerrainPreparationService terrainPreparationService;

    // ============== CREATE (POST) ==============
    
    @POST
    @Produces(MediaType.APPLICATION_JSON) // <--- Coloquei aqui
    @Consumes(MediaType.APPLICATION_JSON) // <--- Coloquei aqui
    @Transactional
    public Response createTerrainPreparationAndDetails(TerrainPreparationDTO detailsDTO) {
        // ... (o código de dentro pode manter igual, só copiei a assinatura para referência)
        String phaseId = UUID.randomUUID().toString();
        try {
            System.out.println("========== INICIANDO SALVAMENTO TERRAIN PREPARATION ==========");
            TerrainPreparation terrainPreparation = new TerrainPreparation();
            terrainPreparation.setId(phaseId);
            terrainPreparation.setName(detailsDTO.getPhaseName());
            terrainPreparation.setContractor(detailsDTO.getContractor());
            
            terrainPreparationService.saveTerrainPreparation(terrainPreparation);
            terrainPreparationService.saveAllPhaseDetails(phaseId, detailsDTO);
            
            return Response.status(Response.Status.CREATED)
                           .entity(new ResponseDTO("Fase Terrain Preparation criada com sucesso", phaseId))
                           .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ErrorDTO("Erro ao salvar", e.getMessage()))
                           .build();
        }
    }

    // ============== FULL UPDATE (PUT) ==============
    
    @PUT
    @Path("/{customerId}")
    @Produces(MediaType.APPLICATION_JSON) // <--- Reforçando
    @Consumes(MediaType.APPLICATION_JSON) // <--- Reforçando
    @Transactional
    public Response updateTerrainPreparation(@PathParam("customerId") String customerId, TerrainPreparationDTO detailsDTO) {
         // ... (código igual)
         try {
            String terrainPreparationId = terrainPreparationService.updateTerrainPreparation(customerId, detailsDTO);
            terrainPreparationService.saveAllPhaseDetails(terrainPreparationId, detailsDTO);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Erro: " + e.getMessage()).build();
        }
    }

    // ============== DETAILS UPDATE (PUT) ==============
    
    @PUT
    @Path("/{id}/details")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateTerrainPreparationDetails(@PathParam("id") String phaseId, TerrainPreparationDTO detailsDTO) {
        // ... (código igual)
         try {
            terrainPreparationService.deleteAllPhaseDetails(phaseId);
            terrainPreparationService.saveAllPhaseDetails(phaseId, detailsDTO);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    // ============== READ (GET) - O Importante ==============
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON) // Diz que DEVOLVE Json
    // IMPORTANTE: SEM @Consumes AQUI! Nem wildcard, nem nada.
    public Response getTerrainPreparation(@PathParam("id") String id) {
        
        System.out.println(">>> AGORA VAI! O GET CHEGOU AQUI! ID: " + id);

        Optional<TerrainPreparation> terrainPreparation = terrainPreparationService.getTerrainPreparationByCustomerId(id);

        if (terrainPreparation.isPresent()) {
            return Response.ok(terrainPreparation.get()).build();
        }
        
        System.out.println(">>> NADA ENCONTRADO NO BANCO PARA O ID: " + id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    // ... (DTOs iguais) ...
    public static class ResponseDTO {
        public String message;
        public String phaseId;
        public ResponseDTO(String m, String p) { this.message = m; this.phaseId = p; }
    }
    public static class ErrorDTO {
        public String error;
        public String message;
        public ErrorDTO(String e, String m) { this.error = e; this.message = m; }
    }
}