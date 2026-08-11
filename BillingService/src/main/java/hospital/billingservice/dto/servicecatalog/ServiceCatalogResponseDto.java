package hospital.billingservice.dto.servicecatalog;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.billingservice.model.enums.ServiceCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for returning service catalog data in API responses.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceCatalogResponseDto {

    private Long id;
    private String code;
    private String name;
    private ServiceCategory category;
    private BigDecimal price;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
