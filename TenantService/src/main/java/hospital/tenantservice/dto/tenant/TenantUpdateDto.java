package hospital.tenantservice.dto.tenant;

import hospital.tenantservice.model.enums.IndustryType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing tenant.
 *
 * <p>All fields are optional - only provided fields will be updated.
 * Critical fields like {@code subdomain} and {@code plan} cannot be updated
 * through this DTO (use dedicated endpoints for those).</p>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantUpdateDto {

    @Size(max = 200, message = "Name must be at most 200 characters")
    private String name;

    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email must be at most 255 characters")
    private String adminEmail;

    @Size(max = 20, message = "Admin phone must be at most 20 characters")
    private String adminPhone;

    @Size(max = 500, message = "Address must be at most 500 characters")
    private String address;

    @Size(max = 100, message = "City must be at most 100 characters")
    private String city;

    @Size(max = 100, message = "Country must be at most 100 characters")
    private String country;

    @Size(max = 20, message = "Phone must be at most 20 characters")
    private String phone;

    @Size(max = 255, message = "Website must be at most 255 characters")
    private String website;

    private IndustryType industry;

    @Size(max = 2000, message = "Description must be at most 2000 characters")
    private String description;

    @Size(max = 50, message = "Tax number must be at most 50 characters")
    private String taxNumber;

    @Size(max = 50, message = "Timezone must be at most 50 characters")
    private String timezone;

    @Size(max = 500, message = "Logo URL must be at most 500 characters")
    private String logoUrl;
}
