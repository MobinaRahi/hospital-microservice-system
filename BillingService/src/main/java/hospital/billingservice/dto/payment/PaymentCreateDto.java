package hospital.billingservice.dto.payment;

import hospital.billingservice.model.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for creating a new payment record.
 *
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *   <li>{@code invoiceId} is required</li>
 *   <li>{@code amount} must be positive</li>
 *   <li>{@code method} is required</li>
 *   <li>{@code paymentDate} is required</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreateDto {

    @NotNull(message = "Invoice ID is required")
    private Long invoiceId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod method;

    @Size(max = 200, message = "Reference number must be at most 200 characters")
    private String referenceNumber;

    @NotNull(message = "Payment date is required")
    private LocalDateTime paymentDate;

    @Size(max = 100, message = "Receipt number must be at most 100 characters")
    private String receiptNumber;

    private Long receivedBy;

    @Size(max = 500, message = "Notes must be at most 500 characters")
    private String notes;
}
