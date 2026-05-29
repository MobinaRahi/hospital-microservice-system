package hospital.coreservice.exception.appointment;


import hospital.coreservice.model.enums.AppointmentStatus;

public class InvalidCancelStateException extends RuntimeException {
    public InvalidCancelStateException(Long id, AppointmentStatus currentStatus) {
        super("Cannot cancel appointment " + id + " because current status is " + currentStatus + ". Only SCHEDULED appointments can be cancelled");
    }
}
