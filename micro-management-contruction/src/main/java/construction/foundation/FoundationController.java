package construction.foundation;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

// Import necessário para o PUT no final do arquivo
import jakarta.ws.rs.NotFoundException; 

@Path("/foundation")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FoundationController {
    
    @Inject
    FoundationService foundationService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createFoundationAndDetails(FoundationDTO detailsDTO) {
        String phaseId = UUID.randomUUID().toString();
        
        try {
            System.out.println("========== INICIANDO SALVAMENTO ==========");
            System.out.println("Phase ID: " + phaseId);
            System.out.println("DTO recebido: " + detailsDTO);
            
            Foundation foundation = new Foundation();
            foundation.setId(phaseId);
            foundation.setName(detailsDTO.getPhaseName()); 
            foundation.setContractor(detailsDTO.getContractor());
            
            System.out.println("[1/3] Salvando Foundation...");
            foundationService.saveFoundation(foundation);
            System.out.println("[1/3] ✓ Foundation salva com sucesso!");
            
            System.out.println("[2/3] Verificando detalhes do DTO...");
            if (detailsDTO.getEquipe() != null) {
                System.out.println("  - Equipe: " + detailsDTO.getEquipe().size() + " membros");
            } else {
                System.out.println("  - Equipe: NULL");
            }
            // ... (restante dos sysouts do seu código original)
            
            System.out.println("[3/3] Salvando detalhes da fase...");
            foundationService.saveAllPhaseDetails(phaseId, detailsDTO);
            System.out.println("[3/3] ✓ Detalhes salvos com sucesso!");
            
            System.out.println("========== SALVAMENTO CONCLUÍDO COM SUCESSO ==========\n");
            
            return Response.status(Response.Status.CREATED)
                           .entity(new ResponseDTO("Fase criada com sucesso", phaseId))
                           .build();
                             
        } catch (Exception e) {
            System.err.println("========== ERRO NO SALVAMENTO ==========");
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
            System.err.println("=========================================\n");
            
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ErrorDTO("Erro ao salvar", e.getMessage()))
                           .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateFoundation(@PathParam("id") String id, Foundation updatedFoundation) {
        try {
            foundationService.updateFoundation(id, updatedFoundation);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Erro ao atualizar: " + e.getMessage())
                           .build();
        }
    }

    @PUT
    @Path("/{id}/details")
    @Transactional
    public Response updateFoundationDetails(
        @PathParam("id") String phaseId, 
        FoundationDTO detailsDTO) {
        
        try {
             // Você pode precisar de um import para FoundationService se FoundationDTO não estiver no mesmo pacote.
             // foundationService.getFoundationById(phaseId).orElseThrow(() -> new NotFoundException("Foundation not found"));

            foundationService.saveAllPhaseDetails(phaseId, detailsDTO);
            
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Erro ao atualizar detalhes: " + e.getMessage())
                           .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getFoundation(@PathParam("id") String id) {
        
        Optional<Foundation> foundation = foundationService.getFoundationById(id);

        if (foundation.isPresent()) {
            return Response.ok(foundation.get()).build();
        }
        
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // Classes auxiliares para resposta
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