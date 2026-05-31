package hospital.coreservice.exception.doctor_schedule;

import hospital.coreservice.model.enums.DayOfWeek;

public class DoctorScheduleNotFoundException extends RuntimeException {
    public DoctorScheduleNotFoundException(String message) {
        super(message);
    }

    public static DoctorScheduleNotFoundException byId(Long scheduleId) {
        throw new DoctorScheduleNotFoundException("DoctorSchedule with id " + scheduleId + " not found");
    }

    public static DoctorScheduleNotFoundException byDoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek) {
        throw new DoctorScheduleNotFoundException("Doctor " + doctorId + " has no schedule on " + dayOfWeek);
    }

    public static DoctorScheduleNotFoundException activeScheduleNotFoundForDoctorAndDay(Long doctorId, DayOfWeek dayOfWeek) {
        return new DoctorScheduleNotFoundException(
                "No active schedule found for doctor ID: " + doctorId + " on " + dayOfWeek
        );
    }
}