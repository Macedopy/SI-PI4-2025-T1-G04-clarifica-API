package construction.core;

import construction.phase.PhaseService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/templates")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TemplateController {

    @Inject
    PhaseService phaseService;

    @GET
    public Response listTemplates() {
        try {
            List<ProjectTemplate> templates = ProjectTemplate.listAll();
            List<TemplateDTO> dtos = templates.stream().map(t -> new TemplateDTO(
                t.getId(),
                t.getCountryCode(),
                t.getName(),
                t.getDescription(),
                t.isDefault()
            )).collect(Collectors.toList());
            return Response.ok(dtos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error listing templates: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{templateId}/phases")
    public Response listTemplatePhases(@PathParam("templateId") String templateId) {
        try {
            List<TemplatePhase> phases = TemplatePhase.find("template_id = ?1 ORDER BY sequence", templateId).list();
            List<TemplatePhaseDTO> dtos = phases.stream().map(p -> new TemplatePhaseDTO(
                p.getId(),
                p.getPhaseId(),
                p.getNameKey(),
                p.getIcon(),
                p.getSequence(),
                p.isOptional(),
                p.isActive(),
                p.getComponents()
            )).collect(Collectors.toList());
            return Response.ok(dtos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error listing template phases: " + e.getMessage())
                    .build();
        }
    }

    // DTOs
    public static class TemplateDTO {
        public String id;
        public String countryCode;
        public String name;
        public String description;
        public boolean isDefault;

        public TemplateDTO(String id, String countryCode, String name, String description, boolean isDefault) {
            this.id = id;
            this.countryCode = countryCode;
            this.name = name;
            this.description = description;
            this.isDefault = isDefault;
        }
    }

    public static class TemplatePhaseDTO {
        public String id;
        public String phaseId;
        public String nameKey;
        public String icon;
        public Integer sequence;
        public boolean isOptional;
        public boolean isActive;
        public String components;

        public TemplatePhaseDTO(String id, String phaseId, String nameKey, String icon, Integer sequence, boolean isOptional, boolean isActive, String components) {
            this.id = id;
            this.phaseId = phaseId;
            this.nameKey = nameKey;
            this.icon = icon;
            this.sequence = sequence;
            this.isOptional = isOptional;
            this.isActive = isActive;
            this.components = components;
        }
    }
}
