package hospital.coreservice.exception.appointment;

import com.hospital.coreService.exception.patient.PatientNotFoundException;

public class AppointmentNotFoundException extends RuntimeException{
    public AppointmentNotFoundException(String message) {
        super(message);
    }
    public static AppointmentNotFoundException byId(Long id) {
        return new AppointmentNotFoundException("َAppointment with id " + id + " not found");
    }

}
