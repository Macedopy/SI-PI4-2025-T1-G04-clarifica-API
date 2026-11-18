package construction.core;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/change-orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChangeOrderController {

    @GET
    @Path("/construction/{constructionId}")
    public Response listByConstruction(@PathParam("constructionId") String constructionId) {
        try {
            List<ChangeOrder> orders = ChangeOrder.find("construction_id = ?1 ORDER BY created_at DESC", constructionId).list();
            List<ChangeOrderDTO> dtos = orders.stream().map(this::toDTO).collect(Collectors.toList());
            return Response.ok(dtos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error listing change orders: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Transactional
    public Response create(ChangeOrderDTO dto) {
        try {
            ChangeOrder order = new ChangeOrder();
            order.setConstructionId(dto.constructionId);
            order.setPhaseId(dto.phaseId);
            order.setTitle(dto.title);
            order.setDescription(dto.description);
            order.setPriceChange(dto.priceChange);
            order.setDaysChange(dto.daysChange);
            order.setStatus(dto.status);
            if ("approved".equals(dto.status) && order.getApprovedAt() == null) {
                order.setApprovedAt(LocalDateTime.now());
            }
            order.persist();

            return Response.status(Response.Status.CREATED).entity(toDTO(order)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating change order: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") String id, ChangeOrderDTO dto) {
        try {
            ChangeOrder order = ChangeOrder.findById(id);
            if (order == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Change order not found").build();
            }

            order.setTitle(dto.title);
            order.setDescription(dto.description);
            order.setPriceChange(dto.priceChange);
            order.setDaysChange(dto.daysChange);
            order.setStatus(dto.status);
            if ("approved".equals(dto.status) && order.getApprovedAt() == null) {
                order.setApprovedAt(LocalDateTime.now());
            }

            return Response.ok(toDTO(order)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating change order: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") String id) {
        try {
            ChangeOrder order = ChangeOrder.findById(id);
            if (order == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Change order not found").build();
            }
            order.delete();
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting change order: " + e.getMessage())
                    .build();
        }
    }

    private ChangeOrderDTO toDTO(ChangeOrder order) {
        ChangeOrderDTO dto = new ChangeOrderDTO();
        dto.id = order.getId();
        dto.constructionId = order.getConstructionId();
        dto.phaseId = order.getPhaseId();
        dto.title = order.getTitle();
        dto.description = order.getDescription();
        dto.priceChange = order.getPriceChange();
        dto.daysChange = order.getDaysChange();
        dto.status = order.getStatus();
        dto.approvedAt = order.getApprovedAt();
        return dto;
    }

    public static class ChangeOrderDTO {
        public String id;
        public String constructionId;
        public String phaseId;
        public String title;
        public String description;
        public BigDecimal priceChange;
        public Integer daysChange;
        public String status;
        public LocalDateTime approvedAt;
    }
}
