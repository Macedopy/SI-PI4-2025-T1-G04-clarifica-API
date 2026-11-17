package construction.components.general_information;

import construction.core.Construction;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/general-information")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GeneralInformationController {

    @GET
    public Response list() {
        return Response.ok(GeneralInformation.listAll()).build();
    }

    @GET
    @Path("/{constructionId}")
    public Response get(@PathParam("constructionId") String constructionId) {
        GeneralInformation info = GeneralInformation
                .find("constructionId", constructionId)
                .firstResult();

        if (info == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(info).build();
    }

    @PUT
    @Path("/{constructionId}")
    @Transactional
    public Response upsert(@PathParam("constructionId") String constructionId,
                           @Valid GeneralInformation input) {
        Construction construction = Construction.findById(constructionId);
        if (construction == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        GeneralInformation info = GeneralInformation
                .find("constructionId", constructionId)
                .firstResult();

        boolean isNew = info == null;
        if (isNew) {
            info = new GeneralInformation();
            info.setConstructionId(constructionId);
        }

        info.setAddress(input.getAddress());
        info.setLandArea(input.getLandArea());
        info.setTopography(input.getTopography());
        info.setStartDate(input.getStartDate());
        info.setTechnicalManager(input.getTechnicalManager());
        info.setObservations(input.getObservations());

        if (isNew) {
            info.persist();
        }

        return Response.ok(info).build();
    }
}
