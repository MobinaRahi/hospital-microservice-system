package hospital.coreservice.exception.doctor_schedule;

import hospital.coreservice.model.enums.DayOfWeek;

/**
 * Thrown when attempting to create a duplicate doctor_schedule.
 *
 * @author Mobina
 */
public class DuplicateDoctorScheduleException extends RuntimeException {

    public DuplicateDoctorScheduleException(Long doctorId, DayOfWeek dayOfWeek) {
        super("Doctor schedule already exists for doctor ID: " + doctorId + " on " + dayOfWeek);
    }

}