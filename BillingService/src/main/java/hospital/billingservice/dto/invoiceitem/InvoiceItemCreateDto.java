package hospital.billingservice.dto.invoiceitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for creating a new invoice item.
 *
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *   <li>{@code serviceCode} is required and max 50 characters</li>
 *   <li>{@code quantity} must be positive</li>
 *   <li>{@code unitPrice} must be positive</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemCreateDto {

    @NotBlank(message = "Service code is required")
    @Size(max = 50, message = "Service code must be at most 50 characters")
    private String serviceCode;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be positive")
    private BigDecimal unitPrice;
}
