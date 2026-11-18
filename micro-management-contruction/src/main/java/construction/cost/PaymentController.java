package construction.cost;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentController {

    @GET
    @Path("/invoice/{invoiceId}")
    public Response listByInvoice(@PathParam("invoiceId") String invoiceId) {
        try {
            List<Payment> payments = Payment.find("invoice.id = ?1", invoiceId).list();
            List<PaymentDTO> dtos = payments.stream().map(this::toDTO).collect(Collectors.toList());
            return Response.ok(dtos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error listing payments: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Transactional
    public Response create(PaymentDTO dto) {
        try {
            Invoice invoice = Invoice.findById(dto.invoiceId);
            if (invoice == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Invoice not found").build();
            }

            Payment payment = new Payment();
            payment.setInvoice(invoice);
            payment.setInvoiceId(invoice.getId());
            payment.setAmount(dto.amount);
            payment.setPaidAt(dto.paidAt != null ? dto.paidAt : LocalDateTime.now());
            payment.setProofUrl(dto.proofUrl);
            payment.persist();

            return Response.status(Response.Status.CREATED).entity(toDTO(payment)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating payment: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") String id, PaymentDTO dto) {
        try {
            Payment payment = Payment.findById(id);
            if (payment == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Payment not found").build();
            }

            payment.setAmount(dto.amount);
            payment.setPaidAt(dto.paidAt);
            payment.setProofUrl(dto.proofUrl);

            return Response.ok(toDTO(payment)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating payment: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") String id) {
        try {
            Payment payment = Payment.findById(id);
            if (payment == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Payment not found").build();
            }
            payment.delete();
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting payment: " + e.getMessage())
                    .build();
        }
    }

    private PaymentDTO toDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.id = payment.getId();
        dto.invoiceId = payment.getInvoiceId();
        dto.amount = payment.getAmount();
        dto.paidAt = payment.getPaidAt();
        dto.proofUrl = payment.getProofUrl();
        return dto;
    }

    public static class PaymentDTO {
        public String id;
        public String invoiceId;
        public BigDecimal amount;
        public LocalDateTime paidAt;
        public String proofUrl;
    }
}
