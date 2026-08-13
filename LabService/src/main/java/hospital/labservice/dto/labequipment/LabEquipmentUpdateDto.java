package hospital.labservice.dto.labequipment;

import hospital.labservice.model.enums.EquipmentStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for updating an existing lab equipment.
 * All fields are optional - only provided fields will be updated.
 *
 * <p><strong>Note:</strong></p>
 * <p>Fields {@code name} and {@code serialNumber} are not updatable
 * as they define the equipment identity.</p>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabEquipmentUpdateDto {

    @Size(max = 100, message = "Model must be at most 100 characters")
    private String model;

    @Size(max = 100, message = "Manufacturer must be at most 100 characters")
    private String manufacturer;

    private EquipmentStatus status;

    private LocalDate lastCalibrationDate;
    private LocalDate nextCalibrationDate;

    @Size(max = 100, message = "Location must be at most 100 characters")
    private String location;

    @Size(max = 500, message = "Notes must be at most 500 characters")
    private String notes;
}
