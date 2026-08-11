package hospital.billingservice.dto.payment;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for updating an existing payment record.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUpdateDto {

    @Size(max = 200, message = "Reference number must be at most 200 characters")
    private String referenceNumber;

    @Size(max = 100, message = "Receipt number must be at most 100 characters")
    private String receiptNumber;

    @Size(max = 500, message = "Notes must be at most 500 characters")
    private String notes;
}
