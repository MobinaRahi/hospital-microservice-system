package hospital.inventoryservice.dto.supplier;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for returning supplier data in API responses.
 * Used in GET endpoints and as nested DTO in other responses.
 *
 * <p><strong>Includes:</strong></p>
 * <ul>
 *   <li>Full supplier details</li>
 *   <li>Active status</li>
 *   <li>Creation timestamp</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupplierResponseDto {

    /**
     * Unique ID of the supplier.
     */
    private Long id;

    /**
     * Company name.
     */
    private String name;

    /**
     * Primary phone number.
     */
    private String phone;

    /**
     * Secondary/mobile phone number.
     */
    private String mobile;

    /**
     * Email address.
     */
    private String email;

    /**
     * Physical address.
     */
    private String address;

    /**
     * Name of the primary contact person.
     */
    private String contactPerson;

    /**
     * Payment terms.
     */
    private String paymentTerms;

    /**
     * Whether this supplier is currently active.
     */
    private Boolean isActive;

    /**
     * When this supplier was created.
     */
    private LocalDateTime createdAt;
}
