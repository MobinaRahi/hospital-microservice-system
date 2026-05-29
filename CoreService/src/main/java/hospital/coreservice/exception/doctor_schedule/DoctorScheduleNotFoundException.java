package hospital.coreservice.exception.doctor_schedule;

public class DoctorScheduleNotFoundException extends RuntimeException{

    public DoctorScheduleNotFoundException(Long doctorId, String dayOfWeek) {
        super("Doctor " + doctorId + " has no schedule on " + dayOfWeek);
    }

}
