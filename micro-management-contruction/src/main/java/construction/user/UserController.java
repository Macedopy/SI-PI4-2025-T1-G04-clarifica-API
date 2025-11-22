package construction.user;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path(value = "/users")
public class UserController {
    @Inject
    UserService userService;

    @Path("/customer")
    @GET
    public Response customer() {
        try
        {
            List<UserDTO> users = userService.allCustomers();
            return Response.ok(users).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error getCustomer: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/customer/{id}")
    public Response customerById(@PathParam("id") String id) {
        try
        {
            UserDTO user = userService.customerById(id);
            return Response.ok(user).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error CustomerById: " + e.getMessage())
                    .build();
        }
    }
}
