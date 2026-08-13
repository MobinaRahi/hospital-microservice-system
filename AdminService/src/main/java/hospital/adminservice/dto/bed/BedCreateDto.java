package hospital.adminservice.dto.bed;

import hospital.adminservice.model.enums.BedType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * DTO for creating a new bed.
 *
 * <p><strong>Required Fields:</strong></p>
 * <ul>
 *   <li>{@code bedNumber} - Unique bed number (max 50 characters)</li>
 *   <li>{@code type} - Bed type (GENERAL, ICU, VIP, etc.)</li>
 * </ul>
 *
 * <p><strong>Optional Fields:</strong></p>
 * <ul>
 *   <li>{@code departmentId} - Department ID from CoreService</li>
 *   <li>{@code notes} - Additional notes (max 500 characters)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@SuperBuilder
public class BedCreateDto {

    @NotBlank(message = "Bed number is required")
    @Size(max = 50, message = "Bed number must be at most 50 characters")
    private String bedNumber;

    /**
     * Department ID from CoreService.
     * Links bed to a specific department.
     */
    private Long departmentId;

    @NotNull(message = "Bed type is required")
    private BedType type;

    @Size(max = 500, message = "Notes must be at most 500 characters")
    private String notes;
}
