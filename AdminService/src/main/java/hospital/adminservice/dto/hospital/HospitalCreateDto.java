package hospital.adminservice.dto.hospital;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for creating a new hospital.
 *
 * <p><strong>Required Fields:</strong></p>
 * <ul>
 *   <li>{@code name} - Hospital name (max 200 characters)</li>
 *   <li>{@code code} - Unique hospital code (max 50 characters)</li>
 * </ul>
 *
 * <p><strong>Optional Fields:</strong></p>
 * <ul>
 *   <li>{@code registrationNumber} - Official registration number</li>
 *   <li>{@code address} - Physical address</li>
 *   <li>{@code phone} - Contact phone</li>
 *   <li>{@code fax} - Fax number</li>
 *   <li>{@code email} - Email address</li>
 *   <li>{@code website} - Website URL</li>
 *   <li>{@code logo} - Logo image URL</li>
 *   <li>{@code taxId} - Tax identification number</li>
 *   <li>{@code bankAccount} - Bank account number</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
public class HospitalCreateDto {

    @NotBlank(message = "Hospital name is required")
    @Size(max = 200, message = "Hospital name must be at most 200 characters")
    private String name;

    @NotBlank(message = "Hospital code is required")
    @Size(max = 50, message = "Hospital code must be at most 50 characters")
    private String code;

    @Size(max = 100, message = "Registration number must be at most 100 characters")
    private String registrationNumber;

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
