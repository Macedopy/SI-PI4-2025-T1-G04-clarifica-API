package construction.eletric;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/eletric")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EletricController {
    
    @Inject
    EletricService eletricService;

    // ============== CREATE (POST) ==============
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createEletricAndDetails(EletricDTO detailsDTO) {
        String phaseId = UUID.randomUUID().toString();
        
        try {
            System.out.println("========== INICIANDO SALVAMENTO ELÉTRICA ==========");
            
            Eletric eletric = new Eletric();
            eletric.setId(phaseId);
            eletric.setName(detailsDTO.getPhaseName()); 
            eletric.setContractor(detailsDTO.getContractor());
            
            eletricService.saveEletric(eletric);
            eletricService.saveAllPhaseDetails(phaseId, detailsDTO);
            
            System.out.println("========== ELÉTRICA SALVA COM SUCESSO ==========\n");
            
            return Response.status(Response.Status.CREATED)
                           .entity(new ResponseDTO("Fase Elétrica criada com sucesso", phaseId))
                           .build();
                             
        } catch (Exception e) {
            System.err.println("========== ERRO NO SALVAMENTO ELÉTRICA ==========");
            e.printStackTrace();
            
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ErrorDTO("Erro ao salvar Elétrica", e.getMessage()))
                           .build();
        }
    }

    // ============== FULL UPDATE (PUT /{customerId}) ==============
    
    @PUT
    @Path("/{customerId}")
    @Transactional
    public Response updateEletric(@PathParam("customerId") String customerId, EletricDTO detailsDTO) {
        try {
            System.out.println("========== INICIANDO UPDATE COMPLETO ELÉTRICA ==========");
            
            String eletricId = eletricService.updateEletric(customerId, detailsDTO);
            eletricService.saveAllPhaseDetails(eletricId, detailsDTO);
            
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
    public Response updateEletricDetails(@PathParam("id") String phaseId, EletricDTO detailsDTO) {
        try {
            eletricService.deleteAllPhaseDetails(phaseId);
            eletricService.saveAllPhaseDetails(phaseId, detailsDTO);
            
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
    public Response getEletric(@PathParam("id") String id) {
        Optional<Eletric> eletric = eletricService.getEletricByCustomerId(id);

        if (eletric.isPresent()) {
            return Response.ok(eletric.get()).build();
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
