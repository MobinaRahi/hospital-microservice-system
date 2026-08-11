package hospital.billingservice.dto.invoice;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.billingservice.dto.invoiceitem.InvoiceItemResponseDto;
import hospital.billingservice.dto.payment.PaymentResponseDto;
import hospital.billingservice.model.enums.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for returning invoice data in API responses.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceResponseDto {

    private Long id;
    private String invoiceNumber;
    private Long patientId;
    private Long encounterId;
    private LocalDateTime issueDate;
    private LocalDate dueDate;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal insuranceCoverage;
    private BigDecimal patientShare;
    private BigDecimal totalAmount;
    private InvoiceStatus status;
    private String notes;
    private Long createdByUser;
    private List<InvoiceItemResponseDto> items;
    private List<PaymentResponseDto> payments;
    private Boolean isOverdue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
