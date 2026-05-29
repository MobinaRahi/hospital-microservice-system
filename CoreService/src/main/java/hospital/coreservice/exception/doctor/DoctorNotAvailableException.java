package hospital.coreservice.exception.doctor;

import java.time.LocalDate;
import java.time.LocalTime;

public class DoctorNotAvailableException extends RuntimeException {

    public DoctorNotAvailableException(Long doctorId, LocalDate date, LocalTime time) {
        super("Doctor " + doctorId + " is not available on " + date + " at " + time);
    }
}
