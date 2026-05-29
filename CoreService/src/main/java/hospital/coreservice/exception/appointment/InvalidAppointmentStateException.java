package hospital.coreservice.exception.appointment;

import com.hospital.coreService.model.enums.AppointmentStatus;

public class InvalidAppointmentStateException extends RuntimeException {
    public InvalidAppointmentStateException(Long id, AppointmentStatus status) {
        super("Cannot reschedule appointment " + id + " because status is " + status);
    }
}
