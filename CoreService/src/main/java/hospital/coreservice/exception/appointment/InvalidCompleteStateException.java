package hospital.coreservice.exception.appointment;


import hospital.coreservice.model.enums.AppointmentStatus;

/**
 * Thrown when an invalid appointment operation is attempted.
 *
 * @author Mobina
 */
public class InvalidCompleteStateException extends RuntimeException {
    public InvalidCompleteStateException(Long id, AppointmentStatus currentStatus) {
        super("Cannot complete appointment " + id + " because current status is " + currentStatus + ". Expected status: CHECKED_IN");
    }
}
