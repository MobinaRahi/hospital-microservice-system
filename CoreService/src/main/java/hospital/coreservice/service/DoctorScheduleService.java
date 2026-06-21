package hospital.coreservice.service;

import hospital.coreservice.dto.doctor_schedule.DoctorScheduleCreateDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleResponseDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleUpdateDto;
import hospital.coreservice.model.enums.DayOfWeek;

import java.time.LocalTime;
import java.util.List;
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
    List<DoctorScheduleResponseDto> getDoctorSchedulesByStartTimeAfter(LocalTime time);
    List<DoctorScheduleResponseDto> getActiveDoctorSchedulesByStartTimeAfter(LocalTime time);
    List<DoctorScheduleResponseDto> getDoctorSchedulesByEndTimeBefore(LocalTime time);
    List<DoctorScheduleResponseDto> getActiveDoctorSchedulesByEndTimeBefore(LocalTime time);
    boolean existsByDoctorAndDay(Long doctorId, DayOfWeek dayOfWeek);
}