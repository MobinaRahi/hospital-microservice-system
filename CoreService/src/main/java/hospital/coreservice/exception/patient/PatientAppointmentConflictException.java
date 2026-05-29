package hospital.coreservice.exception.patient;

import java.time.LocalDate;
import java.time.LocalTime;

public class PatientAppointmentConflictException extends RuntimeException {

    public PatientAppointmentConflictException(Long patientId, LocalDate date, LocalTime time) {
        super("Patient " + patientId + " already has an appointment on " + date + " at " + time);
    }
}
