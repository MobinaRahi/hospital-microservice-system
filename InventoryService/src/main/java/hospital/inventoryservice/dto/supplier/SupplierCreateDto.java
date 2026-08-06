package hospital.inventoryservice.dto.supplier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new supplier.
 * Used in POST /api/v1/inventory/suppliers
 *
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *   <li>{@code name} is required and max 200 characters</li>
 *   <li>{@code email} must be valid email format if provided</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierCreateDto {

    /**
     * Company name of the supplier.
     * Required. Max 200 characters.
     */
    @NotBlank(message = "Supplier name is required")
    @Size(max = 200, message = "Supplier name must be at most 200 characters")
    private String name;

    /**
     * Primary phone number.
     * Optional. Max 20 characters.
     */
    @Size(max = 20, message = "Phone must be at most 20 characters")
    private String phone;

    /**
     * Secondary/mobile phone number.
     * Optional. Max 20 characters.
     */
    @Size(max = 20, message = "Mobile must be at most 20 characters")
    private String mobile;

    /**
     * Email address.
     * Optional but must be valid format if provided. Max 200 characters.
     */
    @Email(message = "Invalid email format")
    @Size(max = 200, message = "Email must be at most 200 characters")
    private String email;

    /**
     * Physical address.
     * Optional. Max 500 characters.
     */
    @Size(max = 500, message = "Address must be at most 500 characters")
    private String address;

    /**
     * Name of the primary contact person.
     * Optional. Max 200 characters.
     */
    @Size(max = 200, message = "Contact person must be at most 200 characters")
    private String contactPerson;

    /**
     * Payment terms (e.g., "Net 30", "Cash on delivery").
     * Optional. Max 200 characters.
     */
    @Size(max = 200, message = "Payment terms must be at most 200 characters")
    private String paymentTerms;
}
