package hospital.coreservice.exception.appointment;


import hospital.coreservice.model.enums.AppointmentStatus;

public class InvalidCompleteStateException extends RuntimeException {
    public InvalidCompleteStateException(Long id, AppointmentStatus currentStatus) {
        super("Cannot complete appointment " + id + " because current status is " + currentStatus + ". Expected status: CHECKED_IN");
    }
}
