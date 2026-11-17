package construction.phase;

import construction.user.User;
import construction.user.UserRole;
import construction.user.UserRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/phases/{phaseId}/annotations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PhaseAnnotationController {

    @Inject
    PhaseAnnotationService annotationService;

    @Inject
    UserRepository userRepository;

    private String userName(String userId) {
        if (userId == null) return null;
        User user = userRepository.findById(userId);
        if (user == null) return null;
        String first = user.getFirstName() == null ? "" : user.getFirstName().trim();
        String last = user.getLastName() == null ? "" : user.getLastName().trim();
        String full = (first + " " + last).trim();
        return full.isEmpty() ? null : full;
    }

    private void fillAuthorNames(List<PhaseAnnotation> annotations) {
        for (PhaseAnnotation annotation : annotations) {
            annotation.setCreatedByName(userName(annotation.getCreatedById()));
        }
    }

    // DTO interno para requisição
    public static class AnnotationRequest {
        public String createdById;
        public String title;
        public String content;
        public boolean isPublic;
        public String componentType;
        public String componentId;
    }

    public static class AnnotationUpdateRequest {
        public String userId;
        public String title;
        public String content;
        public boolean isPublic;
    }

    @POST
    public Response createAnnotation(@PathParam("phaseId") String phaseId, AnnotationRequest request) {
        try {
            if (request.createdById == null || request.title == null || request.content == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("createdById, title and content are required.")
                        .build();
            }

            PhaseAnnotation annotation = annotationService.createAnnotation(
                    phaseId,
                    request.createdById,
                    request.title,
                    request.content,
                    request.isPublic,
                    request.componentType,
                    request.componentId
            );
            annotation.setCreatedByName(userName(annotation.getCreatedById()));

            return Response.status(Response.Status.CREATED).entity(annotation).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating annotation: " + e.getMessage()).build();
        }
    }

    @GET
    public Response listAnnotations(@PathParam("phaseId") String phaseId,
                                    @QueryParam("publicOnly") boolean publicOnly) {
        try {
            List<PhaseAnnotation> annotations;
            if (publicOnly) {
                annotations = annotationService.listPublicByPhase(phaseId);
            } else {
                annotations = annotationService.listByPhase(phaseId);
            }
            fillAuthorNames(annotations);
            return Response.ok(annotations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error listing annotations: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/component")
    public Response listAnnotationsByComponent(@PathParam("phaseId") String phaseId,
                                               @QueryParam("componentType") String componentType,
                                               @QueryParam("componentId") String componentId) {
        try {
            List<PhaseAnnotation> annotations = annotationService.listByPhaseAndComponent(
                    phaseId, componentType, componentId);
            fillAuthorNames(annotations);
            return Response.ok(annotations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error listing annotations: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{annotationId}")
    public Response updateAnnotation(@PathParam("phaseId") String phaseId,
                                     @PathParam("annotationId") String annotationId,
                                     AnnotationUpdateRequest request) {
        try {
            if (request.userId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("userId is required.")
                        .build();
            }

            PhaseAnnotation annotation = annotationService.updateAnnotation(
                    annotationId,
                    request.userId,
                    request.title,
                    request.content,
                    request.isPublic
            );
            annotation.setCreatedByName(userName(annotation.getCreatedById()));

            return Response.ok(annotation).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating annotation: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{annotationId}")
    public Response deleteAnnotation(@PathParam("phaseId") String phaseId,
                                     @PathParam("annotationId") String annotationId,
                                     @QueryParam("userId") String userId) {
        try {
            if (userId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("userId is required.")
                        .build();
            }

            annotationService.deleteAnnotation(annotationId, userId);
            return Response.noContent().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting annotation: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/my")
    public Response listMyAnnotations(@QueryParam("userId") String userId) {
        try {
            if (userId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("userId is required.")
                        .build();
            }
            List<PhaseAnnotation> annotations = annotationService.listByCreatedBy(userId);
            fillAuthorNames(annotations);
            return Response.ok(annotations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error listing annotations: " + e.getMessage()).build();
        }
    }
}
