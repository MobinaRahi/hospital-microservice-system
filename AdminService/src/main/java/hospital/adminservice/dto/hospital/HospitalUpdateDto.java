package hospital.adminservice.dto.hospital;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for updating an existing hospital.
 * All fields are optional - only provided fields will be updated.
 *
 * <p><strong>Usage:</strong></p>
 * <ul>
 *   <li>Send only the fields you want to update</li>
 *   <li>Null fields will be ignored</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
public class HospitalUpdateDto {

    @Size(max = 200, message = "Hospital name must be at most 200 characters")
    private String name;

    @Size(max = 500, message = "Address must be at most 500 characters")
    private String address;

    @Size(max = 50, message = "Phone must be at most 50 characters")
    private String phone;

    @Size(max = 50, message = "Fax must be at most 50 characters")
    private String fax;

    @Size(max = 200, message = "Email must be at most 200 characters")
    private String email;

    @Size(max = 200, message = "Website must be at most 200 characters")
    private String website;

    @Size(max = 500, message = "Logo URL must be at most 500 characters")
    private String logo;

    @Size(max = 50, message = "Tax ID must be at most 50 characters")
    private String taxId;

    @Size(max = 50, message = "Bank account must be at most 50 characters")
    private String bankAccount;
}
