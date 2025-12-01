package construction.finishing;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/finishing")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FinishingController {
    
    @Inject
    FinishingService finishingService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createFinishingAndDetails(FinishingDTO detailsDTO) {
        String phaseId = UUID.randomUUID().toString();
        
        try {
            System.out.println("========== INICIANDO SALVAMENTO FINISHING ==========");
            System.out.println("Phase ID: " + phaseId);
            System.out.println("DTO recebido: " + detailsDTO);
            
            Finishing finishing = new Finishing();
            finishing.setId(phaseId);
            finishing.setName(detailsDTO.getPhaseName()); 
            finishing.setContractor(detailsDTO.getContractor());
            
            System.out.println("[1/3] Salvando Finishing...");
            finishingService.saveFinishing(finishing);
            System.out.println("[1/3] ✓ Finishing salva com sucesso!");
            
            System.out.println("[2/3] Verificando detalhes do DTO...");
            if (detailsDTO.getEquipe() != null) {
                System.out.println("  - Equipe: " + detailsDTO.getEquipe().size() + " membros");
            } else {
                System.out.println("  - Equipe: NULL");
            }
            
            System.out.println("[3/3] Salvando detalhes da fase...");
            finishingService.saveAllPhaseDetails(phaseId, detailsDTO);
            System.out.println("[3/3] ✓ Detalhes salvos com sucesso!");
            
            System.out.println("========== FINISHING SALVO COM SUCESSO ==========\n");
            
            return Response.status(Response.Status.CREATED)
                           .entity(new ResponseDTO("Fase Finishing criada com sucesso", phaseId))
                           .build();
                             
        } catch (Exception e) {
            System.err.println("========== ERRO NO SALVAMENTO FINISHING ==========");
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
            System.err.println("=========================================\n");
            
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ErrorDTO("Erro ao salvar Finishing", e.getMessage()))
                           .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateFinishing(@PathParam("id") String id, FinishingDTO detailsDTO) {
        try {
            System.out.println("========== INICIANDO UPDATE COMPLETO FINISHING ==========");
            Finishing tempFinishing = new Finishing();
            
            if (detailsDTO.getPhaseName() != null) {
                tempFinishing.setName(detailsDTO.getPhaseName());
            }
            tempFinishing.setContractor(detailsDTO.getContractor());
            
            finishingService.updateFinishing(id, tempFinishing);

            finishingService.saveAllPhaseDetails(id, detailsDTO);
            
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

    @PUT
    @Path("/{id}/details")
    @Transactional
    public Response updateFinishingDetails(
        @PathParam("id") String phaseId, 
        FinishingDTO detailsDTO) {
        
        try {
            finishingService.saveAllPhaseDetails(phaseId, detailsDTO);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Erro ao atualizar detalhes: " + e.getMessage())
                           .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getFinishing(@PathParam("id") String id) {
        Optional<Finishing> finishing = finishingService.getFinishingByCustomerId(id);

        if (finishing.isPresent()) {
            return Response.ok(finishing.get()).build();
        }
        
        return Response.status(Response.Status.NOT_FOUND).build();
    }

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