package construction.coatings;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/coatings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CoatingsController {
    
    @Inject
    CoatingsService coatingsService;

    // ============== CREATE (POST) ==============
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createCoatingsAndDetails(CoatingsDTO detailsDTO) {
        String phaseId = UUID.randomUUID().toString();
        
        try {
            System.out.println("========== INICIANDO SALVAMENTO COATINGS ==========");
            
            Coatings coatings = new Coatings();
            coatings.setId(phaseId);
            coatings.setName(detailsDTO.getPhaseName()); 
            coatings.setContractor(detailsDTO.getContractor());
            
            coatingsService.saveCoatings(coatings);
            coatingsService.saveAllPhaseDetails(phaseId, detailsDTO);
            
            System.out.println("========== COATINGS SALVO COM SUCESSO ==========\n");
            
            return Response.status(Response.Status.CREATED)
                           .entity(new ResponseDTO("Fase Coatings criada com sucesso", phaseId))
                           .build();
                             
        } catch (Exception e) {
            System.err.println("========== ERRO NO SALVAMENTO COATINGS ==========");
            e.printStackTrace();
            
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ErrorDTO("Erro ao salvar Coatings", e.getMessage()))
                           .build();
        }
    }

    // ============== FULL UPDATE (PUT /{customerId}) ==============
    
    @PUT
    @Path("/{customerId}")
    @Transactional
    public Response updateCoatings(@PathParam("customerId") String customerId, CoatingsDTO detailsDTO) {
        try {
            System.out.println("========== INICIANDO UPDATE COMPLETO COATINGS ==========");
            
            String coatingsId = coatingsService.updateCoatings(customerId, detailsDTO);
            coatingsService.saveAllPhaseDetails(coatingsId, detailsDTO);
            
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
    public Response updateCoatingsDetails(@PathParam("id") String phaseId, CoatingsDTO detailsDTO) {
        try {
            coatingsService.deleteAllPhaseDetails(phaseId);
            coatingsService.saveAllPhaseDetails(phaseId, detailsDTO);
            
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
    public Response getCoatings(@PathParam("id") String id) {
        Optional<Coatings> coatings = coatingsService.getCoatingsByCustomerId(id);

        if (coatings.isPresent()) {
            return Response.ok(coatings.get()).build();
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
