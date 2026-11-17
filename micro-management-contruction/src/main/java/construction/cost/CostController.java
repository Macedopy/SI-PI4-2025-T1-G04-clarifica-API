package construction.cost;

import construction.user.User;
import construction.user.UserRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;

@Path("/cost")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CostController {

    @Inject
    MaterialPurchaseService materialService;

    @Inject
    ToolPurchaseService toolService;

    @Inject
    MachineryRecordService machineryService;

    @Inject
    MachineryFuelLogService fuelService;

    @Inject
    MachineryMaintenanceLogService maintenanceService;

    @Inject
    TeamMemberWorkLogService workLogService;

    @Inject
    CostDashboardService costDashboardService;

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

    private String teamMemberName(String teamMemberId) {
        if (teamMemberId == null) return null;
        construction.components.team_present.TeamMember member =
                construction.components.team_present.TeamMember.findById(teamMemberId);
        return member != null ? member.getName() : null;
    }

    // ============ MATERIAL PURCHASES ============

    public static class MaterialPurchaseRequest {
        public String phaseId;
        public String materialId;
        public double quantity;
        public double unitPrice;
        public String purchasedById;
        public String supplier;
        public String notes;
    }

    @POST
    @Path("/materials")
    public Response createMaterialPurchase(MaterialPurchaseRequest request) {
        try {
            MaterialPurchase purchase = materialService.create(
                    request.phaseId, request.materialId, request.quantity,
                    request.unitPrice, request.purchasedById, request.supplier, request.notes
            );
            return Response.status(Response.Status.CREATED).entity(purchase).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/materials/phase/{phaseId}")
    public Response listMaterialPurchases(@PathParam("phaseId") String phaseId) {
        List<MaterialPurchase> purchases = materialService.listByPhase(phaseId);
        purchases.forEach(p -> p.setPurchasedByName(userName(p.getPurchasedById())));
        return Response.ok(purchases).build();
    }

    // ============ TOOL PURCHASES ============

    public static class ToolPurchaseRequest {
        public String phaseId;
        public String toolId;
        public int quantity;
        public double unitPrice;
        public String purchasedById;
        public String supplier;
        public String notes;
    }

    @POST
    @Path("/tools")
    public Response createToolPurchase(ToolPurchaseRequest request) {
        try {
            ToolPurchase purchase = toolService.create(
                    request.phaseId, request.toolId, request.quantity,
                    request.unitPrice, request.purchasedById, request.supplier, request.notes
            );
            return Response.status(Response.Status.CREATED).entity(purchase).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/tools/phase/{phaseId}")
    public Response listToolPurchases(@PathParam("phaseId") String phaseId) {
        List<ToolPurchase> purchases = toolService.listByPhase(phaseId);
        purchases.forEach(p -> p.setPurchasedByName(userName(p.getPurchasedById())));
        return Response.ok(purchases).build();
    }

    // ============ MACHINERY RECORDS ============

    public static class MachineryRecordRequest {
        public String phaseId;
        public String machineryId;
        public boolean isOwned;
        public String rentalType;
        public double rentalPrice;
        public double currentValue;
    }

    @POST
    @Path("/machinery")
    public Response createMachineryRecord(MachineryRecordRequest request) {
        try {
            // Usar um userId padrão se não fornecido (pode ser melhorado com auth real)
            String createdById = request.machineryId != null ? request.machineryId : "system";
            MachineryRecord record = machineryService.create(
                    request.phaseId, request.machineryId, request.isOwned,
                    request.rentalType, request.rentalPrice, request.currentValue, createdById
            );
            return Response.status(Response.Status.CREATED).entity(record).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/machinery/{recordId}/stop")
    public Response stopMachinery(@PathParam("recordId") String recordId,
                                  @QueryParam("stoppedById") String stoppedById,
                                  @QueryParam("stopReason") String stopReason) {
        try {
            machineryService.stopMachinery(recordId, stoppedById, stopReason);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/machinery/{recordId}/resume")
    public Response resumeMachinery(@PathParam("recordId") String recordId) {
        try {
            machineryService.resumeMachinery(recordId);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/machinery/phase/{phaseId}")
    public Response listMachineryRecords(@PathParam("phaseId") String phaseId) {
        List<MachineryRecord> records = machineryService.listByPhase(phaseId);
        records.forEach(r -> r.setStoppedByName(userName(r.getStoppedById())));
        return Response.ok(records).build();
    }

    // ============ FUEL LOGS ============

    public static class FuelLogRequest {
        public String machineryId;
        public String phaseId;
        public LocalDate logDate;
        public double liters;
        public double pricePerLiter;
        public String recordedById;
        public String notes;
    }

    @POST
    @Path("/fuel")
    public Response createFuelLog(FuelLogRequest request) {
        try {
            MachineryFuelLog log = fuelService.create(
                    request.machineryId, request.phaseId, request.logDate,
                    request.liters, request.pricePerLiter, request.recordedById, request.notes
            );
            return Response.status(Response.Status.CREATED).entity(log).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/fuel/phase/{phaseId}")
    public Response listFuelLogs(@PathParam("phaseId") String phaseId) {
        List<MachineryFuelLog> logs = fuelService.listByPhase(phaseId);
        logs.forEach(l -> l.setRecordedByName(userName(l.getRecordedById())));
        return Response.ok(logs).build();
    }

    // ============ MAINTENANCE LOGS ============

    public static class MaintenanceLogRequest {
        public String machineryId;
        public String phaseId;
        public LocalDate maintenanceDate;
        public String description;
        public double cost;
        public String performedById;
        public String notes;
    }

    @POST
    @Path("/maintenance")
    public Response createMaintenanceLog(MaintenanceLogRequest request) {
        try {
            MachineryMaintenanceLog log = maintenanceService.create(
                    request.machineryId, request.phaseId, request.maintenanceDate,
                    request.description, request.cost, request.performedById, request.notes
            );
            return Response.status(Response.Status.CREATED).entity(log).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/maintenance/phase/{phaseId}")
    public Response listMaintenanceLogs(@PathParam("phaseId") String phaseId) {
        List<MachineryMaintenanceLog> logs = maintenanceService.listByPhase(phaseId);
        logs.forEach(l -> l.setPerformedByName(userName(l.getPerformedById())));
        return Response.ok(logs).build();
    }

    // ============ TEAM MEMBER WORK LOGS ============

    public static class WorkLogRequest {
        public String teamMemberId;
        public String phaseId;
        public LocalDate workDate;
        public double hoursWorked;
        public double hourlyRate;
        public String addedById;
        public String notes;
    }

    @POST
    @Path("/work-logs")
    public Response createWorkLog(WorkLogRequest request) {
        try {
            TeamMemberWorkLog log = workLogService.create(
                    request.teamMemberId, request.phaseId, request.workDate,
                    request.hoursWorked, request.hourlyRate, request.addedById, request.notes
            );
            return Response.status(Response.Status.CREATED).entity(log).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/work-logs/phase/{phaseId}")
    public Response listWorkLogs(@PathParam("phaseId") String phaseId) {
        List<TeamMemberWorkLog> logs = workLogService.listByPhase(phaseId);
        logs.forEach(l -> {
            l.setAddedByName(userName(l.getAddedById()));
            l.setTeamMemberName(teamMemberName(l.getTeamMemberId()));
        });
        return Response.ok(logs).build();
    }

    // ============ COST DASHBOARD ============

    @GET
    @Path("/dashboard/{constructionId}")
    public Response getCostDashboard(@PathParam("constructionId") String constructionId) {
        try {
            CostDashboardDTO dto = costDashboardService.getDashboardByConstruction(constructionId);
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }
}
