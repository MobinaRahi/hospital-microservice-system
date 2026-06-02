package hospital.coreservice.service;

import hospital.coreservice.dto.doctor_schedule.DoctorScheduleCreateDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleResponseDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleUpdateDto;
import hospital.coreservice.model.enums.DayOfWeek;

import java.time.LocalTime;
import java.util.List;

/**
 * Service interface for DoctorSchedule management.
 *
 * @author Mobina
 */
public interface DoctorScheduleService {

    // ========== Core Operations ==========

    /**
     * Create new doctor schedule
     */
    DoctorScheduleResponseDto createDoctorSchedule(DoctorScheduleCreateDto createDto);

    /**
     * Update existing doctor schedule
     */
    DoctorScheduleResponseDto updateDoctorSchedule(Long scheduleId, DoctorScheduleUpdateDto updateDto);

    /**
     * Create multiple schedules for a doctor (weekly schedule)
     */
    void bulkCreateDoctorSchedules(List<DoctorScheduleCreateDto> createDtoList);

    // ========== Basic Retrieval ==========

    /**
     * Get schedule by ID
     */
    DoctorScheduleResponseDto getDoctorScheduleById(Long scheduleId);

    /**
     * Get all schedules for a specific doctor
     */
    List<DoctorScheduleResponseDto> getDoctorSchedulesByDoctorId(Long doctorId);

    /**
     * Get all schedules
     */
    List<DoctorScheduleResponseDto> getAllDoctorSchedules();

    // ========== Single Day Retrieval ==========

    /**
     * Get schedule by doctor ID and day of week
     */
    DoctorScheduleResponseDto getDoctorScheduleByDoctorAndDay(Long doctorId, DayOfWeek dayOfWeek);

    /**
     * Get active schedule by doctor ID and day of week
     */
    DoctorScheduleResponseDto getActiveDoctorScheduleByDoctorAndDay(Long doctorId, DayOfWeek dayOfWeek);

    // ========== Status Management ==========

    /**
     * Activate schedule
     */
    void activateDoctorSchedule(Long scheduleId);

    /**
     * Deactivate schedule
     */
    void deactivateDoctorSchedule(Long scheduleId);

    /**
     * Get all active schedules
     */
    List<DoctorScheduleResponseDto> getActiveDoctorSchedules();

    /**
     * Get active schedules for a specific doctor
     */
    List<DoctorScheduleResponseDto> getActiveDoctorSchedulesByDoctorId(Long doctorId);

    /**
     * Get all inactive schedules
     */
    List<DoctorScheduleResponseDto> getInactiveDoctorSchedules();

    /**
     * Get inactive schedules for a specific doctor
     */
    List<DoctorScheduleResponseDto> getInactiveDoctorSchedulesByDoctorId(Long doctorId);

    // ========== Time Based Queries ==========

    /**
     * Get schedules where start time is after given time
     */
    List<DoctorScheduleResponseDto> getDoctorSchedulesByStartTimeAfter(LocalTime time);

    /**
     * Get schedules where end time is before given time
     */
    List<DoctorScheduleResponseDto> getDoctorSchedulesByEndTimeBefore(LocalTime time);

    // ========== Validation ==========

    /**
     * Check if schedule exists for doctor on specific day
     */
    boolean existsByDoctorAndDay(Long doctorId, DayOfWeek dayOfWeek);
}