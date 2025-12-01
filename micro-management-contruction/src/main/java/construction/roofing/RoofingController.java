package construction.roofing;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/roofing")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoofingController {
    
    @Inject
    RoofingService roofingService;

    // ============== CREATE (POST) ==============
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createRoofingAndDetails(RoofingDTO detailsDTO) {
        String phaseId = UUID.randomUUID().toString();
        
        try {
            System.out.println("========== INICIANDO SALVAMENTO ROOFING ==========");
            
            Roofing roofing = new Roofing();
            roofing.setId(phaseId);
            roofing.setName(detailsDTO.getPhaseName());
            roofing.setContractor(detailsDTO.getContractor());
            
            roofingService.saveRoofing(roofing);
            roofingService.saveAllPhaseDetails(phaseId, detailsDTO);
            
            System.out.println("========== ROOFING SALVO COM SUCESSO ==========\n");
            
            return Response.status(Response.Status.CREATED)
                           .entity(new ResponseDTO("Fase Roofing criada com sucesso", phaseId))
                           .build();
                             
        } catch (Exception e) {
            System.err.println("========== ERRO NO SALVAMENTO ROOFING ==========");
            e.printStackTrace();
            
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ErrorDTO("Erro ao salvar Roofing", e.getMessage()))
                           .build();
        }
    }

    // ============== FULL UPDATE (PUT /{customerId}) ==============
    
    @PUT
    @Path("/{customerId}")
    @Transactional
    public Response updateRoofing(@PathParam("customerId") String customerId, RoofingDTO detailsDTO) {
        try {
            System.out.println("========== INICIANDO UPDATE COMPLETO ROOFING ==========");
            
            String roofingId = roofingService.updateRoofing(customerId, detailsDTO);
            roofingService.saveAllPhaseDetails(roofingId, detailsDTO);
            
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
    public Response updateRoofingDetails(@PathParam("id") String phaseId, RoofingDTO detailsDTO) {
        try {
            roofingService.deleteAllPhaseDetails(phaseId);
            roofingService.saveAllPhaseDetails(phaseId, detailsDTO);
            
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
    public Response getRoofing(@PathParam("id") String id) {
        Optional<Roofing> roofing = roofingService.getRoofingByCustomerId(id);
        
        if (roofing.isPresent()) {
            return Response.ok(roofing.get()).build();
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
