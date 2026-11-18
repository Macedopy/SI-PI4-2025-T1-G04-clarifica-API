package construction.cost;

import construction.core.Phase;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/cost-items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CostItemController {

    @GET
    @Path("/phase/{phaseId}")
    public Response listByPhase(@PathParam("phaseId") String phaseId) {
        try {
            List<CostItem> items = CostItem.find("phase.id = ?1", phaseId).list();
            List<CostItemDTO> dtos = items.stream().map(this::toDTO).collect(Collectors.toList());
            return Response.ok(dtos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error listing cost items: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Transactional
    public Response create(CostItemDTO dto) {
        try {
            Phase phase = Phase.findById(dto.phaseId);
            if (phase == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Phase not found").build();
            }

            CostItem item = new CostItem();
            item.setPhase(phase);
            item.setPhaseId(phase.getId());
            item.setDescription(dto.description);
            item.setVendorId(dto.vendorId);
            item.setBudgetAmount(dto.budgetAmount);
            item.setCommittedAmount(dto.committedAmount);
            item.setInvoicedAmount(dto.invoicedAmount);
            item.setActualCostAmount(dto.actualCostAmount);
            item.setPaidAmount(dto.paidAmount);
            item.setRetainedAmount(dto.retainedAmount);
            item.setCurrencyCode(dto.currencyCode);
            item.persist();

            return Response.status(Response.Status.CREATED).entity(toDTO(item)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating cost item: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") String id, CostItemDTO dto) {
        try {
            CostItem item = CostItem.findById(id);
            if (item == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Cost item not found").build();
            }

            item.setDescription(dto.description);
            item.setVendorId(dto.vendorId);
            item.setBudgetAmount(dto.budgetAmount);
            item.setCommittedAmount(dto.committedAmount);
            item.setInvoicedAmount(dto.invoicedAmount);
            item.setActualCostAmount(dto.actualCostAmount);
            item.setPaidAmount(dto.paidAmount);
            item.setRetainedAmount(dto.retainedAmount);
            item.setCurrencyCode(dto.currencyCode);

            return Response.ok(toDTO(item)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating cost item: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") String id) {
        try {
            CostItem item = CostItem.findById(id);
            if (item == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Cost item not found").build();
            }
            item.delete();
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting cost item: " + e.getMessage())
                    .build();
        }
    }

    private CostItemDTO toDTO(CostItem item) {
        CostItemDTO dto = new CostItemDTO();
        dto.id = item.getId();
        dto.phaseId = item.getPhaseId();
        dto.activityId = item.getActivityId();
        dto.description = item.getDescription();
        dto.vendorId = item.getVendorId();
        dto.budgetAmount = item.getBudgetAmount();
        dto.committedAmount = item.getCommittedAmount();
        dto.invoicedAmount = item.getInvoicedAmount();
        dto.actualCostAmount = item.getActualCostAmount();
        dto.paidAmount = item.getPaidAmount();
        dto.retainedAmount = item.getRetainedAmount();
        dto.currencyCode = item.getCurrencyCode();
        return dto;
    }

    public static class CostItemDTO {
        public String id;
        public String phaseId;
        public String activityId;
        public String description;
        public String vendorId;
        public BigDecimal budgetAmount;
        public BigDecimal committedAmount;
        public BigDecimal invoicedAmount;
        public BigDecimal actualCostAmount;
        public BigDecimal paidAmount;
        public BigDecimal retainedAmount;
        public String currencyCode;
    }
}
