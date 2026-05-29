package hospital.coreservice.exception.appointment;

import com.hospital.coreService.model.enums.AppointmentStatus;

public class InvalidCheckInStateException extends RuntimeException {
    public InvalidCheckInStateException(Long id, AppointmentStatus currentStatus) {
        super("Cannot check in appointment " + id + " because current status is " + currentStatus + ". Expected status: SCHEDULED");
    }
}
