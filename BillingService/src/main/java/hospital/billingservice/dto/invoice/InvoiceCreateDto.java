package hospital.billingservice.dto.invoice;

import hospital.billingservice.dto.invoiceitem.InvoiceItemCreateDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for creating a new invoice.
 *
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *   <li>{@code invoiceNumber} is required and max 50 characters</li>
 *   <li>{@code patientId} is required</li>
 *   <li>{@code issueDate} is required</li>
 *   <li>{@code items} must have at least one item</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceCreateDto {

    @NotBlank(message = "Invoice number is required")
    @Size(max = 50, message = "Invoice number must be at most 50 characters")
    private String invoiceNumber;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    private Long encounterId;

    @NotNull(message = "Issue date is required")
    private LocalDateTime issueDate;

    private LocalDate dueDate;

    @NotNull(message = "Subtotal is required")
    private BigDecimal subtotal;

    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal tax = BigDecimal.ZERO;

    private BigDecimal insuranceCoverage;

    @Size(max = 1000, message = "Notes must be at most 1000 characters")
    private String notes;

    @NotEmpty(message = "Invoice must have at least one item")
    private List<InvoiceItemCreateDto> items;
}
