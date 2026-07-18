package hospital.coreservice.service;

import hospital.coreservice.dto.doctor_schedule.DoctorScheduleCreateDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleResponseDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleUpdateDto;
import hospital.coreservice.model.enums.DayOfWeek;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for DoctorSchedule management.
 * Handles CRUD for doctor working hours.
 *
 * @author Mobina
 */
public interface DoctorScheduleService {
    DoctorScheduleResponseDto createDoctorSchedule(DoctorScheduleCreateDto createDto);
    DoctorScheduleResponseDto updateDoctorSchedule(Long scheduleId, DoctorScheduleUpdateDto updateDto);
    void bulkCreateDoctorSchedules(List<DoctorScheduleCreateDto> createDtoList);
    DoctorScheduleResponseDto getDoctorScheduleById(Long scheduleId);
    List<DoctorScheduleResponseDto> getDoctorSchedulesByDoctorId(Long doctorId);
    List<DoctorScheduleResponseDto> getActiveDoctorSchedulesByDoctorId(Long doctorId);
    List<DoctorScheduleResponseDto> getInactiveDoctorSchedulesByDoctorId(Long doctorId);
    List<DoctorScheduleResponseDto> getAllDoctorSchedules();
    DoctorScheduleResponseDto getDoctorScheduleByDoctorAndDay(Long doctorId, DayOfWeek dayOfWeek);
    DoctorScheduleResponseDto getActiveDoctorScheduleByDoctorAndDay(Long doctorId, DayOfWeek dayOfWeek);
    void activateDoctorSchedule(Long scheduleId);
    void deactivateDoctorSchedule(Long scheduleId);
    List<DoctorScheduleResponseDto> getActiveDoctorSchedules();
    List<DoctorScheduleResponseDto> getInactiveDoctorSchedules();

    // ===== LocalTime → LocalDateTime =====
    List<DoctorScheduleResponseDto> getDoctorSchedulesByStartTimeAfter(LocalDateTime time);
    List<DoctorScheduleResponseDto> getActiveDoctorSchedulesByStartTimeAfter(LocalDateTime time);
    List<DoctorScheduleResponseDto> getDoctorSchedulesByEndTimeBefore(LocalDateTime time);
    List<DoctorScheduleResponseDto> getActiveDoctorSchedulesByEndTimeBefore(LocalDateTime time);
    // =====================================

    boolean existsByDoctorAndDay(Long doctorId, DayOfWeek dayOfWeek);
}