package hospital.coreservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * Request DTO for request operations.
 *
 * @author Mobina
 */
public class PatientBookingRequest {
    private Long doctorId;
    private String appointmentDate;
    private String startTime;
    private String endTime;
    private String type;
    private String reason;
    private String notes;

    private String fullName;
    private String nationalId;
    private String phone;
}