package construction.phase;

import construction.core.Phase;
import construction.dashboard.DashboardDTO;
import construction.dashboard.DashboardService;
import construction.user.User;
import construction.user.UserRole;
import construction.user.UserRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/create-components")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PhaseController {

    @Inject
    PhaseService phaseService;

    @Inject
    DashboardService dashboardService;

    @Inject
    UserRepository userRepository;

    @POST
    public Response createPhase(PhaseRequestDTO request) {
        try {
            // Verificar permissão se o requesterId for enviado
            if (request.getRequesterId() != null) {
                User requester = userRepository.findById(request.getRequesterId());
                if (requester != null && requester.getRole() == UserRole.CLIENT) {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity("Only administrators or master builders can create phases.")
                            .build();
                }
            }

            // Usar o contractor do request (antes era passado como null)
            // Resolve o id da obra (a UI envia o id do usuário) antes de criar.
            String constructionId = phaseService.resolveConstructionId(request.getConstructionId());

            phaseService.createPhase(
                    constructionId,
                    request.getName(),
                    request.getContractor(),
                    request.getIcon()
            );

            // Disparar cálculo do dashboard e retornar resultados
            DashboardDTO dashboard = dashboardService.calculateDashboard(constructionId);

            return Response.status(Response.Status.CREATED)
                    .entity(dashboard)
                    .build();
        } catch (IllegalArgumentException e) {
            // Fase duplicada ou erro de validação
            return Response.status(Response.Status.CONFLICT)
                    .entity(e.getMessage())
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating phase: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/batch")
    public Response createPhasesBatch(List<PhaseService.PhaseCreationRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("At least one phase is required.")
                    .build();
        }

        // Extrair constructionId do primeiro request (todos devem ser da mesma construção)
        // Como PhaseCreationRequest não tem constructionId, usamos um parâmetro de query ou header
        // Para simplicidade, o constructionId deve ser enviado como query param
        throw new UnsupportedOperationException("Use /batch/{constructionId} endpoint instead.");
    }

    @POST
    @Path("/batch/{constructionId}")
    public Response createPhasesBatch(
            @PathParam("constructionId") String constructionId,
            List<PhaseService.PhaseCreationRequest> requests) {
        try {
            if (requests == null || requests.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("At least one phase is required.")
                        .build();
            }

            List<Phase> created = phaseService.createPhasesBatch(constructionId, requests);

            // Calcular dashboard após criar todas as fases
            DashboardDTO dashboard = dashboardService.calculateDashboard(constructionId);

            return Response.status(Response.Status.CREATED)
                    .entity(dashboard)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(e.getMessage())
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating phases batch: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{constructionId}/{requesterId}/{phaseId}")
    public Response deletePhase(
            @PathParam("constructionId") String constructionId,
            @PathParam("requesterId") String requesterId,
            @PathParam("phaseId") String phaseId) {
        try {
            // Verificar permissão do solicitante
            User requester = userRepository.findById(requesterId);
            if (requester == null || requester.getRole() == UserRole.CLIENT) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Only administrators or master builders can delete phases.")
                        .build();
            }

            phaseService.deletePhase(phaseId);

            // Disparar cálculo do dashboard e retornar resultados
            DashboardDTO dashboard = dashboardService.calculateDashboard(constructionId);

            return Response.ok(dashboard).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting phase: " + e.getMessage())
                    .build();
        }
    }
}
