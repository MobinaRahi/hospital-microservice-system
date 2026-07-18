package hospital.coreservice.exception.appointment;


import hospital.coreservice.model.enums.AppointmentStatus;

/**
 * Thrown when an invalid appointment operation is attempted.
 *
 * @author Mobina
 */
public class InvalidCheckInStateException extends RuntimeException {
    public InvalidCheckInStateException(Long id, AppointmentStatus currentStatus) {
        super("Cannot check in appointment " + id + " because current status is " + currentStatus + ". Expected status: SCHEDULED");
    }
}
