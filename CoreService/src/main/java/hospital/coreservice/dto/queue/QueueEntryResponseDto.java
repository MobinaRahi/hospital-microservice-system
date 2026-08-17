package hospital.coreservice.dto.queue;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.coreservice.model.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for returning queue entry data in API responses.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueueEntryResponseDto {

    private Long id;
    private Long appointmentId;
    private Long patientId;
    private Long doctorId;
    private LocalDate queueDate;
    private Integer queuePosition;
    private Priority priority;
    private String status;
    private Integer estimatedWaitMinutes;
    private LocalDateTime calledAt;
    private Boolean isForToday;
    private Boolean isEmergency;
    private LocalDateTime createdAt;
}
