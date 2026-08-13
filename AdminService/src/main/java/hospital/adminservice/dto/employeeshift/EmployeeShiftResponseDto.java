package hospital.adminservice.dto.employeeshift;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.adminservice.dto.shift.ShiftResponseDto;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for returning employee shift data in API responses.
 * Includes nested shift definition for convenience.
 * Null fields are excluded from JSON output.
 *
 * @author MobinaRahi
 */
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeShiftResponseDto {

    private Long id;
    private Long employeeId;

    /**
     * Nested shift definition.
     */
    private ShiftResponseDto shift;

    private LocalDate date;
    private LocalDateTime actualStart;
    private LocalDateTime actualEnd;
    private Boolean isPresent;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
