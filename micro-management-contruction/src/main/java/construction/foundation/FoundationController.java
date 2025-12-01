package construction.foundation;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/foundation")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FoundationController {
    
    @Inject
    FoundationService foundationService;

    // ============== CREATE (POST) ==============
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createFoundationAndDetails(FoundationDTO detailsDTO) {
        // Gera um ID único para a nova fase
        String phaseId = UUID.randomUUID().toString();
        
        try {
            System.out.println("========== INICIANDO SALVAMENTO FOUNDATION ==========");
            
            Foundation foundation = new Foundation();
            foundation.setId(phaseId);
            foundation.setName(detailsDTO.getPhaseName()); 
            foundation.setContractor(detailsDTO.getContractor());
            
            // 1. Salva a entidade principal (Foundation)
            foundationService.saveFoundation(foundation);
            
            // 2. Salva todos os detalhes (TeamMembers, Machinery, etc.)
            foundationService.saveAllPhaseDetails(phaseId, detailsDTO);
            
            System.out.println("========== FOUNDATION SALVA COM SUCESSO ==========\n");
            
            return Response.status(Response.Status.CREATED)
                           .entity(new ResponseDTO("Fase Foundation criada com sucesso", phaseId))
                           .build();
                             
        } catch (Exception e) {
            System.err.println("========== ERRO NO SALVAMENTO FOUNDATION ==========");
            e.printStackTrace();
            
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ErrorDTO("Erro ao salvar Foundation", e.getMessage()))
                           .build();
        }
    }

    // ============== FULL UPDATE (PUT /customerId) ==============
    // Este endpoint recebe todos os dados (principal + detalhes) e faz a substituição completa.
    
    @PUT
    @Path("/{customerId}")
    @Transactional
    public Response updateFoundation(@PathParam("customerId") String customerId, FoundationDTO detailsDTO) {
        try {
            System.out.println("========== INICIANDO UPDATE COMPLETO FOUNDATION ==========");
            
            // 1. foundationService.updateFoundation: 
            //    - Atualiza os campos principais.
            //    - DELETA TODOS os registros filhos antigos.
            // Retorna o ID da Foundation existente.
            String foundationId = foundationService.updateFoundation(customerId, detailsDTO);

            // 2. foundationService.saveAllPhaseDetails: 
            //    - RECRIA TODOS os registros filhos com os novos dados do DTO.
            foundationService.saveAllPhaseDetails(foundationId, detailsDTO);
            
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

    // ============== DETAILS ONLY UPDATE (PUT /id/details) ==============
    // Endpoint para quem deseja atualizar SOMENTE os detalhes de uma Foundation já existente.
    
    @PUT
    @Path("/{id}/details")
    @Transactional
    public Response updateFoundationDetails(
        @PathParam("id") String phaseId, 
        FoundationDTO detailsDTO) {
        
        try {
            // 1. Deleta os detalhes existentes (limpeza obrigatória antes de recriar)
            foundationService.deleteAllPhaseDetails(phaseId); 
            
            // 2. Salva/Recria os novos detalhes
            foundationService.saveAllPhaseDetails(phaseId, detailsDTO);
            
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
    public Response getFoundation(@PathParam("id") String id) {
        Optional<Foundation> foundation = foundationService.getFoundationByCustomerId(id);

        if (foundation.isPresent()) {
            return Response.ok(foundation.get()).build();
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