package construction.phase;

import com.fasterxml.jackson.databind.JsonNode;
import construction.components.executed_services.ExecutedService;
import construction.components.executed_services.ExecutedServiceStatus;
import construction.components.machinery.Condition;
import construction.components.machinery.FuelUnit;
import construction.components.machinery.Machinery;
import construction.components.photo.PhotoRecord;
import construction.components.team_present.MemberStatus;
import construction.components.team_present.TeamMember;
import construction.components.tools.Tool;
import construction.components.tools.ToolCondition;
import construction.components.used_material.Material;
import construction.components.used_material.MaterialCategory;
import construction.components.used_material.MaterialUnit;
import construction.core.Phase;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Endpoint consumido pela UI dinamica (src/phases/DynamicPhase.tsx):
 *   GET /dynamic-phase/{phaseId}/{customerId} -> dados completos da fase
 *   PUT /dynamic-phase/{phaseId}/{customerId} -> grava o relatorio da fase
 */
@Path("/dynamic-phase")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DynamicPhaseController {

    @GET
    @Path("/{phaseId}/{customerId}")
    public Response getPhase(@PathParam("phaseId") String phaseId,
                             @PathParam("customerId") String customerId) {
        Phase phase = Phase.findById(phaseId);
        if (phase == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Phase not found with ID: " + phaseId))
                    .build();
        }
        return Response.ok(buildPhaseData(phase)).build();
    }

    @PUT
    @Path("/{phaseId}/{customerId}")
    @Transactional
    public Response savePhase(@PathParam("phaseId") String phaseId,
                              @PathParam("customerId") String customerId,
                              JsonNode body) {
        Phase phase = Phase.findById(phaseId);
        if (phase == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Phase not found with ID: " + phaseId))
                    .build();
        }
        if (body == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Payload vazio"))
                    .build();
        }

        if (body.has("materiais")) {
            replaceMaterials(phase, body.get("materiais"));
        }
        if (body.has("ferramentas")) {
            replaceTools(phase, body.get("ferramentas"));
        }
        if (body.has("maquinarios")) {
            replaceMachinery(phase, body.get("maquinarios"));
        }
        if (body.has("equipe")) {
            replaceTeamMembers(phase, body.get("equipe"));
        }
        if (body.has("servicos")) {
            replaceServices(phase, body.get("servicos"));
        }

        return Response.ok(buildPhaseData(phase)).build();
    }

    // ------------------------------------------------------------------ leitura

    private Map<String, Object> buildPhaseData(Phase phase) {
        String phaseId = phase.getId();
        Map<String, Object> params = Map.of("phaseId", phaseId);

        List<Material> materiais = Material.find("phase.id = :phaseId", params).list();
        List<Tool> ferramentas = Tool.find("phase.id = :phaseId", params).list();
        List<Machinery> maquinarios = Machinery.find("phase.id = :phaseId", params).list();
        List<TeamMember> equipe = TeamMember.find("phase.id = :phaseId", params).list();
        List<ExecutedService> servicos = ExecutedService.find("phase.id = :phaseId", params).list();
        List<PhotoRecord> fotos = PhotoRecord.find("phase.id = :phaseId", params).list();

        List<Map<String, Object>> equipeDto = new ArrayList<>();
        for (TeamMember member : equipe) {
            equipeDto.add(toTeamMemberDto(member));
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", phase.getId());
        data.put("name", phase.getName());
        data.put("nome", phase.getName());
        data.put("contractor", phase.getContractor());
        data.put("materiais", materiais);
        data.put("ferramentas", ferramentas);
        data.put("maquinarios", maquinarios);
        data.put("equipe", equipeDto);
        data.put("teamMembers", equipeDto);
        data.put("servicos", servicos);
        data.put("services", servicos);
        data.put("fotos", fotos);
        data.put("photoRecords", fotos);
        data.put("components", componentsOf(materiais, ferramentas, maquinarios, equipe, servicos, fotos));
        return data;
    }

    private Map<String, Object> toTeamMemberDto(TeamMember member) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("name", member.getName());
        details.put("role", member.getRole());
        details.put("cpf", member.getCpf());

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", member.getId());
        dto.put("phaseId", null);
        dto.put("details", details);
        dto.put("name", member.getName());
        dto.put("role", member.getRole());
        dto.put("cpf", member.getCpf());
        dto.put("hoursWorked", member.getHoursWorked());
        dto.put("status", member.getStatus() == null ? null : member.getStatus().getDisplayName());
        dto.put("notes", member.getNotes());
        return dto;
    }

    private List<String> componentsOf(List<Material> materiais, List<Tool> ferramentas,
                                      List<Machinery> maquinarios, List<TeamMember> equipe,
                                      List<ExecutedService> servicos, List<PhotoRecord> fotos) {
        List<String> components = new ArrayList<>();
        if (!materiais.isEmpty()) components.add("materiais");
        if (!ferramentas.isEmpty()) components.add("ferramentas");
        if (!maquinarios.isEmpty()) components.add("maquinarios");
        if (!equipe.isEmpty()) components.add("equipe");
        if (!servicos.isEmpty()) components.add("servicos");
        if (!fotos.isEmpty()) components.add("fotos");
        return components;
    }

    // ------------------------------------------------------------------ escrita

    private void replaceMaterials(Phase phase, JsonNode items) {
        Material.delete("phase.id = :phaseId", Map.of("phaseId", phase.getId()));
        for (JsonNode item : items) {
            Material material = new Material();
            material.setName(item.path("name").asText(""));
            material.setCategory(MaterialCategory.fromValue(item.path("category").asText(null)));
            material.setUnit(MaterialUnit.fromString(item.path("unit").asText("UNIT")));
            material.setConsumedQuantity(item.path("consumedQuantity").asDouble(0));
            material.setCurrentStock(item.path("currentStock").asDouble(0));
            material.setMinimumStock(item.path("minimumStock").asDouble(0));
            material.setNeedsRestock(item.path("needsRestock").asBoolean(false));
            material.setUrgency(item.path("urgency").asText(null));
            material.setPhase(phase);
            material.persist();
        }
    }

    private void replaceTools(Phase phase, JsonNode items) {
        Tool.delete("phase.id = :phaseId", Map.of("phaseId", phase.getId()));
        for (JsonNode item : items) {
            Tool tool = new Tool();
            tool.setName(item.path("name").asText(""));
            tool.setCategory(item.path("category").asText(""));
            tool.setTotalQuantity(Math.max(1, item.path("totalQuantity").asInt(1)));
            tool.setInUse(Math.max(0, item.path("inUse").asInt(0)));
            tool.setInMaintenance(Math.max(0, item.path("inMaintenance").asInt(0)));
            tool.setCondition(ToolCondition.fromString(item.path("condition").asText("GOOD")));
            tool.setNotes(item.path("notes").asText(null));
            tool.setPhase(phase);
            tool.persist();
        }
    }

    private void replaceMachinery(Phase phase, JsonNode items) {
        Machinery.delete("phase.id = :phaseId", Map.of("phaseId", phase.getId()));
        for (JsonNode item : items) {
            Machinery machinery = new Machinery();
            machinery.setName(item.path("name").asText(""));
            machinery.setCategory(item.path("category").asText(""));
            machinery.setHoursWorked(item.path("hoursWorked").asDouble(0));
            machinery.setFuelUsed(item.path("fuelUsed").asDouble(0));
            machinery.setFuel(fuelUnitOf(item.path("fuelUnit").asText("LITERS")));
            machinery.setTotalQuantity(Math.max(1, item.path("totalQuantity").asInt(1)));
            machinery.setInOperation(Math.max(0, item.path("inOperation").asInt(0)));
            machinery.setInMaintenance(Math.max(0, item.path("inMaintenance").asInt(0)));
            machinery.setCondition(conditionOf(item.path("condition").asText("GOOD")));
            machinery.setNotes(item.path("notes").asText(null));
            machinery.setPhase(phase);
            machinery.persist();
        }
    }

    private void replaceTeamMembers(Phase phase, JsonNode items) {
        TeamMember.delete("phase.id = :phaseId", Map.of("phaseId", phase.getId()));
        for (JsonNode item : items) {
            JsonNode details = item.path("details");
            TeamMember member = new TeamMember();
            member.setName(details.path("name").asText(item.path("name").asText("")));
            member.setRole(details.path("role").asText(item.path("role").asText("")));
            member.setCpf(details.path("cpf").asText(item.path("cpf").asText(null)));
            member.setHoursWorked(item.path("hoursWorked").asDouble(0));
            member.setStatus(MemberStatus.fromString(item.path("status").asText("PRESENT")));
            member.setNotes(item.path("notes").asText(null));
            member.setPhase(phase);
            member.persist();
        }
    }

    private void replaceServices(Phase phase, JsonNode items) {
        ExecutedService.delete("phase.id = :phaseId", Map.of("phaseId", phase.getId()));
        for (JsonNode item : items) {
            ExecutedService service = new ExecutedService();
            service.setName(item.path("name").asText(""));
            service.setTeam(item.path("team").asText(""));
            service.setPlannedHours(Math.max(1, item.path("plannedHours").asDouble(1)));
            service.setExecutedHours(Math.max(0, item.path("executedHours").asDouble(0)));
            service.setStatus(ExecutedServiceStatus.fromValue(item.path("status").asText("PLANNED")));
            service.setProgress(Math.min(100, Math.max(0, item.path("progress").asInt(0))));
            service.setNotes(item.path("notes").asText(null));
            service.setPhase(phase);
            service.persist();
        }
    }

    private FuelUnit fuelUnitOf(String value) {
        String normalized = value == null ? "" : value.trim().toUpperCase();
        if (normalized.equals("HORAS") || normalized.equals("HOURS")) {
            return FuelUnit.valueOf("HOURS");
        }
        for (FuelUnit unit : FuelUnit.values()) {
            if (unit.name().equals(normalized)) {
                return unit;
            }
        }
        return FuelUnit.valueOf("LITERS");
    }

    private Condition conditionOf(String value) {
        String normalized = value == null ? "" : value.trim().toUpperCase();
        for (Condition condition : Condition.values()) {
            if (condition.name().equals(normalized)) {
                return condition;
            }
        }
        return Condition.valueOf("GOOD");
    }
}
