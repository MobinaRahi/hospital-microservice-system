package hospital.inventoryservice.dto.supplier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing supplier.
 * Used in PUT /api/v1/inventory/suppliers/{id}
 *
 * <p><strong>Rules:</strong></p>
 * <ul>
 *   <li>All fields are optional — only provided fields will be updated</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierUpdateDto {

    /**
     * Updated company name.
     * Optional. Max 200 characters.
     */
    @Size(max = 200, message = "Supplier name must be at most 200 characters")
    private String name;

    /**
     * Updated primary phone number.
     * Optional. Max 20 characters.
     */
    @Size(max = 20, message = "Phone must be at most 20 characters")
    private String phone;

    /**
     * Updated secondary/mobile phone number.
     * Optional. Max 20 characters.
     */
    @Size(max = 20, message = "Mobile must be at most 20 characters")
    private String mobile;

    /**
     * Updated email address.
     * Optional. Must be valid format if provided. Max 200 characters.
     */
    @Email(message = "Invalid email format")
    @Size(max = 200, message = "Email must be at most 200 characters")
    private String email;

    /**
     * Updated physical address.
     * Optional. Max 500 characters.
     */
    @Size(max = 500, message = "Address must be at most 500 characters")
    private String address;

    /**
     * Updated contact person name.
     * Optional. Max 200 characters.
     */
    @Size(max = 200, message = "Contact person must be at most 200 characters")
    private String contactPerson;

    /**
     * Updated payment terms.
     * Optional. Max 200 characters.
     */
    @Size(max = 200, message = "Payment terms must be at most 200 characters")
    private String paymentTerms;

    /**
     * Whether this supplier is currently active.
     * Optional. Used to activate/deactivate suppliers.
     */
    private Boolean isActive;
}
