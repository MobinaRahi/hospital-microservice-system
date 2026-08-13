package hospital.adminservice.dto.bed;

import hospital.adminservice.model.enums.BedStatus;
import hospital.adminservice.model.enums.BedType;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for updating an existing bed.
 * All fields are optional - only provided fields will be updated.
 *
 * @author MobinaRahi
 */
@Data
public class BedUpdateDto {

    @Size(max = 50, message = "Bed number must be at most 50 characters")
    private String bedNumber;

    private Long departmentId;
    private BedType type;
    private BedStatus status;

    @Size(max = 500, message = "Notes must be at most 500 characters")
    private String notes;
}
