package hospital.tenantservice.dto.tenant;

import hospital.tenantservice.model.enums.IndustryType;
import hospital.tenantservice.model.enums.PlanType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for creating a new tenant (hospital/clinic).
 *
 * <p><strong>Required Fields:</strong></p>
 * <ul>
 *   <li>{@code name} - Tenant name (max 200 characters)</li>
 *   <li>{@code subdomain} - Unique subdomain (max 100 characters)</li>
 *   <li>{@code adminEmail} - Admin email (valid email format)</li>
 *   <li>{@code startDate} - Subscription start date</li>
 * </ul>
 *
 * <p><strong>Optional Fields:</strong></p>
 * <ul>
 *   <li>{@code adminPhone} - Admin phone number</li>
 *   <li>{@code plan} - Subscription plan (defaults to FREE)</li>
 *   <li>{@code address} - Physical address</li>
 *   <li>{@code city} - City</li>
 *   <li>{@code country} - Country</li>
 *   <li>{@code phone} - General contact phone</li>
 *   <li>{@code website} - Website URL</li>
 *   <li>{@code industry} - Industry type</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantCreateDto {

    @NotBlank(message = "Tenant name is required")
    @Size(max = 200, message = "Name must be at most 200 characters")
    private String name;

    @NotBlank(message = "Subdomain is required")
    @Size(min = 3, max = 100, message = "Subdomain must be between 3 and 100 characters")
    private String subdomain;

    @NotBlank(message = "Admin email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email must be at most 255 characters")
    private String adminEmail;

    @Size(max = 20, message = "Admin phone must be at most 20 characters")
    private String adminPhone;

    @NotNull(message = "Subscription start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "Subscription plan is required")
    private PlanType plan;

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
}
