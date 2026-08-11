package hospital.billingservice.dto.servicecatalog;

import hospital.billingservice.model.enums.ServiceCategory;
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
 * DTO for creating a new service catalog entry.
 *
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *   <li>{@code code} is required and max 50 characters</li>
 *   <li>{@code name} is required and max 300 characters</li>
 *   <li>{@code category} is required</li>
 *   <li>{@code price} must be positive</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCatalogCreateDto {

    @NotBlank(message = "Service code is required")
    @Size(max = 50, message = "Service code must be at most 50 characters")
    private String code;

    @NotBlank(message = "Service name is required")
    @Size(max = 300, message = "Service name must be at most 300 characters")
    private String name;

    @NotNull(message = "Service category is required")
    private ServiceCategory category;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
}
