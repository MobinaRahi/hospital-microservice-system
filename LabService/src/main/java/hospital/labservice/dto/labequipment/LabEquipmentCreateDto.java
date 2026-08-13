package hospital.labservice.dto.labequipment;

import hospital.labservice.model.enums.EquipmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for creating a new lab equipment.
 *
 * <p><strong>Required Fields:</strong></p>
 * <ul>
 *   <li>{@code name} - Equipment name (max 200 characters)</li>
 *   <li>{@code serialNumber} - Unique serial number (max 100 characters)</li>
 * </ul>
 *
 * <p><strong>Optional Fields:</strong></p>
 * <ul>
 *   <li>{@code model} - Equipment model number</li>
 *   <li>{@code manufacturer} - Equipment manufacturer</li>
 *   <li>{@code status} - Equipment status (defaults to OPERATIONAL)</li>
 *   <li>{@code lastCalibrationDate} - Date of last calibration</li>
 *   <li>{@code nextCalibrationDate} - Date when next calibration is due</li>
 *   <li>{@code location} - Physical location in the lab</li>
 *   <li>{@code notes} - Additional notes about the equipment</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabEquipmentCreateDto {

    @NotBlank(message = "Equipment name is required")
    @Size(max = 200, message = "Equipment name must be at most 200 characters")
    private String name;

    @Size(max = 100, message = "Model must be at most 100 characters")
    private String model;

    @Size(max = 100, message = "Manufacturer must be at most 100 characters")
    private String manufacturer;

    @NotBlank(message = "Serial number is required")
    @Size(max = 100, message = "Serial number must be at most 100 characters")
    private String serialNumber;

    private EquipmentStatus status;

    private LocalDate lastCalibrationDate;
    private LocalDate nextCalibrationDate;

    @Size(max = 100, message = "Location must be at most 100 characters")
    private String location;

    @Size(max = 500, message = "Notes must be at most 500 characters")
    private String notes;
}
