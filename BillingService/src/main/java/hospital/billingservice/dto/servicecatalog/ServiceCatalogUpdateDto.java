package hospital.billingservice.dto.servicecatalog;

import hospital.billingservice.model.enums.ServiceCategory;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for updating an existing service catalog entry.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCatalogUpdateDto {

    @Size(max = 300, message = "Service name must be at most 300 characters")
    private String name;

    private ServiceCategory category;

    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private Boolean isActive;
}
