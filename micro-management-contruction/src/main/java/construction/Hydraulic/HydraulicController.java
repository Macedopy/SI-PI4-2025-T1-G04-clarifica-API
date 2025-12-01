package construction.hydraulic;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/hydraulic")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HydraulicController {

    @Inject
    HydraulicService hydraulicService;

    // ============== CREATE (POST) ==============
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createHydraulicAndDetails(HydraulicDTO detailsDTO) {
        String phaseId = UUID.randomUUID().toString();
        
        try {
            System.out.println("========== INICIANDO SALVAMENTO HYDRAULIC ==========");
            
            Hydraulic hydraulic = new Hydraulic();
            hydraulic.setId(phaseId);
            hydraulic.setName(detailsDTO.getPhaseName());
            hydraulic.setContractor(detailsDTO.getContractor());

            hydraulicService.saveHydraulic(hydraulic);
            hydraulicService.saveAllPhaseDetails(phaseId, detailsDTO);
            
            System.out.println("========== HYDRAULIC SALVO COM SUCESSO ==========\n");

            return Response.status(Response.Status.CREATED)
                    .entity(new ResponseDTO("Fase Hydraulic criada com sucesso", phaseId))
                    .build();

        } catch (Exception e) {
            System.err.println("========== ERRO NO SALVAMENTO HYDRAULIC ==========");
            e.printStackTrace();

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorDTO("Erro ao salvar Hydraulic", e.getMessage()))
                    .build();
        }
    }

    // ============== FULL UPDATE (PUT /{customerId}) ==============
    
    @PUT
    @Path("/{customerId}")
    @Transactional
    public Response updateHydraulic(@PathParam("customerId") String customerId, HydraulicDTO detailsDTO) {
        try {
            System.out.println("========== INICIANDO UPDATE COMPLETO HYDRAULIC ==========");
            
            String hydraulicId = hydraulicService.updateHydraulic(customerId, detailsDTO);
            hydraulicService.saveAllPhaseDetails(hydraulicId, detailsDTO);
            
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
    public Response updateHydraulicDetails(@PathParam("id") String phaseId, HydraulicDTO detailsDTO) {
        try {
            hydraulicService.deleteAllPhaseDetails(phaseId);
            hydraulicService.saveAllPhaseDetails(phaseId, detailsDTO);
            
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
    public Response getHydraulic(@PathParam("id") String id) {
        Optional<Hydraulic> hydraulic = hydraulicService.getHydrauliconByCustomerId(id);
        
        if (hydraulic.isPresent()) {
            return Response.ok(hydraulic.get()).build();
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
    