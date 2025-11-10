package construction.foundation;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path(value = "/users")
public class FoundationController {
    @Inject
    FoundationService userService;

    @Path("/customer")
    @GET
    public Response customer() {
        try
        {
            List<FoundationDTO> users = userService.allCustomers();
            return Response.ok(users).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error getCustomer: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/customer/{id}")
    public Response customerById(@PathParam(value = id) String id) {
        try
        {
            FoundationDTO user = userService.customerById(id);
            return Response.ok(user).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error CustomerById: " + e.getMessage())
                    .build();
        }
    }
}
