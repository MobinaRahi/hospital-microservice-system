package hospital.adminservice.dto.hospital;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for returning hospital data in API responses.
 * Null fields are excluded from JSON output.
 *
 * @author MobinaRahi
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HospitalResponseDto {

    private Long id;
    private String name;
    private String code;
    private String registrationNumber;
    private String address;
    private String phone;
    private String fax;
    private String email;
    private String website;
    private String logo;
    private String taxId;
    private String bankAccount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
