package construction.masonry;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/masonry")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MasonryController {
    
    @Inject
    MasonryService masonryService;

    // ============== CREATE (POST) ==============
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createMasonryAndDetails(MasonryDTO detailsDTO) {
        String phaseId = UUID.randomUUID().toString();
        
        try {
            System.out.println("========== INICIANDO SALVAMENTO MASONRY ==========");
            
            Masonry masonry = new Masonry();
            masonry.setId(phaseId);
            masonry.setName(detailsDTO.getPhaseName()); 
            masonry.setContractor(detailsDTO.getContractor());
            
            masonryService.saveMasonry(masonry);
            masonryService.saveAllPhaseDetails(phaseId, detailsDTO);
            
            System.out.println("========== MASONRY SALVO COM SUCESSO ==========\n");
            
            return Response.status(Response.Status.CREATED)
                           .entity(new ResponseDTO("Fase Masonry criada com sucesso", phaseId))
                           .build();
                             
        } catch (Exception e) {
            System.err.println("========== ERRO NO SALVAMENTO MASONRY ==========");
            e.printStackTrace();
            
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ErrorDTO("Erro ao salvar Masonry", e.getMessage()))
                           .build();
        }
    }

    // ============== FULL UPDATE (PUT /{customerId}) ==============
    
    @PUT
    @Path("/{customerId}")
    @Transactional
    public Response updateMasonry(@PathParam("customerId") String customerId, MasonryDTO detailsDTO) {
        try {
            System.out.println("========== INICIANDO UPDATE COMPLETO MASONRY ==========");
            
            // 1. Atualiza campos principais e deleta detalhes antigos
            String masonryId = masonryService.updateMasonry(customerId, detailsDTO);

            // 2. Recria todos os detalhes com os novos dados
            masonryService.saveAllPhaseDetails(masonryId, detailsDTO);
            
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
    public Response updateMasonryDetails(@PathParam("id") String phaseId, MasonryDTO detailsDTO) {
        try {
            // 1. Deleta os detalhes existentes
            masonryService.deleteAllPhaseDetails(phaseId);
            
            // 2. Salva/Recria os novos detalhes
            masonryService.saveAllPhaseDetails(phaseId, detailsDTO);
            
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
    public Response getMasonry(@PathParam("id") String id) {
        Optional<Masonry> masonry = masonryService.getMasonryByCustomerId(id);
        
        if (masonry.isPresent()) {
            return Response.ok(masonry.get()).build();
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