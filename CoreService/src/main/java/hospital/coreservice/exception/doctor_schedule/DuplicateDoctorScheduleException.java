package hospital.coreservice.exception.doctor_schedule;

import hospital.coreservice.model.enums.DayOfWeek;

public class DuplicateDoctorScheduleException extends RuntimeException {

    public DuplicateDoctorScheduleException(Long doctorId, DayOfWeek dayOfWeek) {
        super("Doctor schedule already exists for doctor ID: " + doctorId + " on " + dayOfWeek);
    }

}