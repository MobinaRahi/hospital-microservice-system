package hospital.coreservice.exception.patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Exception for patient operations.
 *
 * @author Mobina
 */
public class PatientAppointmentConflictException extends RuntimeException {

    public PatientAppointmentConflictException(Long patientId, LocalDate date, LocalTime time) {
        super("Patient " + patientId + " already has an appointment on " + date + " at " + time);
    }
}
