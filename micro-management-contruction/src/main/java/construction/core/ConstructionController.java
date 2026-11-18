package construction.core;

import construction.components.executed_services.ExecutedService;
import construction.components.machinery.Machinery;
import construction.components.photo.PhotoRecord;
import construction.components.team_present.TeamMember;
import construction.components.tools.Tool;
import construction.components.used_material.Material;
import construction.dashboard.DashboardService;
import construction.user.User;
import construction.user.UserRole;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/constructions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConstructionController {

    @Inject
    DashboardService dashboardService;

    @POST
    @Transactional
    public Response create(ConstructionDTO dto) {
        Construction construction = new Construction();
        construction.setName(dto.getName());
        
        if (dto.getOwnerId() != null) {
            User owner = User.findById(dto.getOwnerId());
            if (owner != null && owner.getRole() == UserRole.OWNER) {
                construction.setOwner(owner);
            }
        }

        // Suportando a criação enviando lista de IDs (como no DTO antigo para compatibilidade)
        // Mas o DTO novo agora usa UserInfoDTO para saída.
        // Vou manter a lógica de entrada simples.

        construction.persist();
        return Response.status(Response.Status.CREATED).entity(mapToDTO(construction)).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        Construction construction = Construction.findById(id);
        if (construction == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(mapToDTO(construction)).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response getByUser(@PathParam("userId") String userId) {
        User user = User.findById(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Construction> constructions;
        if (user.getRole() == UserRole.OWNER) {
            constructions = Construction.find("owner", user).list();
        } else if (user.getRole() == UserRole.CLIENT) {
            constructions = Construction.find("select c from Construction c join c.clients cl where cl.id = ?1", userId).list();
        } else {
            constructions = Construction.find("select c from Construction c join c.masterBuilders mb where mb.id = ?1", userId).list();
        }

        return Response.ok(constructions.stream().map(this::mapToDTO).collect(Collectors.toList())).build();
    }

    /**
     * Lista as fases de uma obra. O id pode ser o da obra ou o de um usuario:
     * a UI (src/services/phases.service.ts) chama com o customerId.
     */
    @GET
    @Path("/{id}/phases")
    public Response getPhases(@PathParam("id") String id) {
        Construction construction = Construction.findById(id);
        if (construction == null) {
            construction = firstConstructionOf(User.findById(id));
        }
        if (construction == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Construction not found with ID: " + id))
                    .build();
        }

        List<Phase> phases = Phase.find("construction.id = :constructionId order by name",
                Map.of("constructionId", construction.getId())).list();

        List<Map<String, Object>> result = new ArrayList<>();
        for (Phase phase : phases) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", phase.getId());
            item.put("nome", phase.getName());
            item.put("name", phase.getName());
            item.put("templatePhaseId", phase.getTemplatePhaseId());
            item.put("contractor", phase.getContractor());
            item.put("icon", phase.getIcon());
            item.put("components", componentsOf(phase.getId()));
            result.add(item);
        }
        return Response.ok(result).build();
    }

    private Construction firstConstructionOf(User user) {
        if (user == null) {
            return null;
        }
        if (user.getRole() == UserRole.OWNER) {
            return Construction.find("owner", user).firstResult();
        }
        if (user.getRole() == UserRole.CLIENT) {
            return Construction.find(
                    "select c from Construction c join c.clients cl where cl.id = ?1", user.getId()).firstResult();
        }
        return Construction.find(
                "select c from Construction c join c.masterBuilders mb where mb.id = ?1", user.getId()).firstResult();
    }

    private List<String> componentsOf(String phaseId) {
        Map<String, Object> params = Map.of("phaseId", phaseId);
        List<String> components = new ArrayList<>();
        if (Material.find("phase.id = :phaseId", params).count() > 0) components.add("materiais");
        if (Tool.find("phase.id = :phaseId", params).count() > 0) components.add("ferramentas");
        if (Machinery.find("phase.id = :phaseId", params).count() > 0) components.add("maquinarios");
        if (TeamMember.find("phase.id = :phaseId", params).count() > 0) components.add("equipe");
        if (ExecutedService.find("phase.id = :phaseId", params).count() > 0) components.add("servicos");
        if (PhotoRecord.find("phase.id = :phaseId", params).count() > 0) components.add("fotos");
        return components;
    }

    private ConstructionDTO mapToDTO(Construction c) {
        ConstructionDTO dto = new ConstructionDTO();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setStatus(c.getStatus());
        
        if (c.getOwner() != null) {
            dto.setOwnerId(c.getOwner().getId());
            dto.setOwnerName(c.getOwner().fullName());
        }

        dto.setClients(c.getClients().stream()
            .map(u -> new ConstructionDTO.UserInfoDTO(u.getId(), u.fullName(), u.getEmail()))
            .collect(Collectors.toList()));

        dto.setMasterBuilders(c.getMasterBuilders().stream()
            .map(u -> new ConstructionDTO.UserInfoDTO(u.getId(), u.fullName(), u.getEmail()))
            .collect(Collectors.toList()));

        dto.setTemplateId(c.getTemplateId());
        dto.setCountryCode(c.getCountryCode());
        dto.setCurrencyCode(c.getCurrencyCode());
        dto.setLanguage(c.getLanguage());
        dto.setMeasurementSystem(c.getMeasurementSystem());

        // Adiciona o progresso calculado
        try {
            dto.setOverallProgress(dashboardService.calculateDashboard(c.getId()).getOverallProgress());
        } catch (Exception e) {
            dto.setOverallProgress(0);
        }

        return dto;
    }
}
