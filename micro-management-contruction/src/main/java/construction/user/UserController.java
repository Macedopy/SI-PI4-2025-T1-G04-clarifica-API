package construction.user;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/users")
public class UserController {
    
    @Inject
    UserService userService;

    @POST
    @Path("/customer")
    public Response createCustomer(UserDTO userDTO) {
        try {
            UserDTO createdUser = userService.createUser(userDTO);
            return Response.status(Response.Status.CREATED)
                    .entity(createdUser)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating customer: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/customer")
    public Response customer() {
        try {
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
        try {
            UserDTO user = userService.customerById(id);
            return Response.ok(user).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error CustomerById: " + e.getMessage())
                    .build();
        }
    }
}
