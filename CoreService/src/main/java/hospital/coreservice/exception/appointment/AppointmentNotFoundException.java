package hospital.coreservice.exception.appointment;

public class AppointmentNotFoundException extends RuntimeException{
    public AppointmentNotFoundException(String message) {
        super(message);
    }
    public static AppointmentNotFoundException byId(Long id) {
        return new AppointmentNotFoundException("َAppointment with id " + id + " not found");
    }

}
