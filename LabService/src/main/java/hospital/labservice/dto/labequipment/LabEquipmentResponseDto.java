package hospital.labservice.dto.labequipment;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.labservice.model.enums.EquipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for returning lab equipment data in API responses.
 * Null fields are excluded from JSON output.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabEquipmentResponseDto {

    private Long id;
    private String name;
    private String model;
    private String manufacturer;
    private String serialNumber;
    private EquipmentStatus status;
    private LocalDate lastCalibrationDate;
    private LocalDate nextCalibrationDate;
    private String location;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
