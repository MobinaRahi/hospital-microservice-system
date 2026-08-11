package hospital.billingservice.dto.invoice;

import hospital.billingservice.model.enums.InvoiceStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for updating an existing invoice.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceUpdateDto {

    private LocalDate dueDate;

    private BigDecimal discount;

    private BigDecimal tax;

    private BigDecimal insuranceCoverage;

    @Size(max = 1000, message = "Notes must be at most 1000 characters")
    private String notes;

    private InvoiceStatus status;
}
