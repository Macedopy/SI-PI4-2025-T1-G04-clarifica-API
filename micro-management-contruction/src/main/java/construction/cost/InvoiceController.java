package construction.cost;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/invoices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InvoiceController {

    @GET
    @Path("/cost-item/{costItemId}")
    public Response listByCostItem(@PathParam("costItemId") String costItemId) {
        try {
            List<Invoice> invoices = Invoice.find("cost_item.id = ?1", costItemId).list();
            List<InvoiceDTO> dtos = invoices.stream().map(this::toDTO).collect(Collectors.toList());
            return Response.ok(dtos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error listing invoices: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Transactional
    public Response create(InvoiceDTO dto) {
        try {
            CostItem costItem = CostItem.findById(dto.costItemId);
            if (costItem == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Cost item not found").build();
            }

            Invoice invoice = new Invoice();
            invoice.setCostItem(costItem);
            invoice.setCostItemId(costItem.getId());
            invoice.setInvoiceNumber(dto.invoiceNumber);
            invoice.setAmount(dto.amount);
            invoice.setStatus(dto.status);
            invoice.setDueDate(dto.dueDate);
            invoice.persist();

            return Response.status(Response.Status.CREATED).entity(toDTO(invoice)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating invoice: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") String id, InvoiceDTO dto) {
        try {
            Invoice invoice = Invoice.findById(id);
            if (invoice == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Invoice not found").build();
            }

            invoice.setInvoiceNumber(dto.invoiceNumber);
            invoice.setAmount(dto.amount);
            invoice.setStatus(dto.status);
            invoice.setDueDate(dto.dueDate);

            return Response.ok(toDTO(invoice)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating invoice: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") String id) {
        try {
            Invoice invoice = Invoice.findById(id);
            if (invoice == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Invoice not found").build();
            }
            invoice.delete();
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting invoice: " + e.getMessage())
                    .build();
        }
    }

    private InvoiceDTO toDTO(Invoice invoice) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.id = invoice.getId();
        dto.costItemId = invoice.getCostItemId();
        dto.invoiceNumber = invoice.getInvoiceNumber();
        dto.amount = invoice.getAmount();
        dto.status = invoice.getStatus();
        dto.dueDate = invoice.getDueDate();
        return dto;
    }

    public static class InvoiceDTO {
        public String id;
        public String costItemId;
        public String invoiceNumber;
        public BigDecimal amount;
        public String status;
        public LocalDate dueDate;
    }
}
