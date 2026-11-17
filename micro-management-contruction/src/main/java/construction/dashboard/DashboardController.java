package construction.dashboard;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DashboardController {

    @Inject
    DashboardService dashboardService;

    @GET
    @Path("/{constructionId}")
    public Response getDashboard(@PathParam("constructionId") String constructionId) {
        try {
            DashboardDTO dashboard = dashboardService.calculateDashboard(constructionId);
            return Response.ok(dashboard).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error calculating dashboard: " + e.getMessage())
                    .build();
        }
    }
}
