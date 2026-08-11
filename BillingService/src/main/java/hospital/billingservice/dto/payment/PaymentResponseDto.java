package hospital.billingservice.dto.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.billingservice.model.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for returning payment data in API responses.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponseDto {

    private Long id;
    private Long invoiceId;
    private BigDecimal amount;
    private PaymentMethod method;
    private String referenceNumber;
    private LocalDateTime paymentDate;
    private String receiptNumber;
    private Long receivedBy;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
