package hospital.coreservice.service;

import hospital.coreservice.dto.doctor_schedule.DoctorScheduleCreateDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleResponseDto;
import hospital.coreservice.exception.doctor.DoctorNotFoundException;
import hospital.coreservice.mapper.DoctorScheduleMapper;
import hospital.coreservice.model.Doctor;
import hospital.coreservice.model.DoctorSchedule;
import hospital.coreservice.model.enums.DayOfWeek;
import hospital.coreservice.repository.DoctorRepository;
import hospital.coreservice.repository.DoctorScheduleRepository;
import hospital.coreservice.service.imp.DoctorScheduleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DoctorScheduleService Tests - CoreService")
class DoctorScheduleServiceTest {

    @Mock private DoctorScheduleRepository doctorScheduleRepository;
    @Mock private DoctorScheduleMapper doctorScheduleMapper;
    @Mock private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorScheduleServiceImpl doctorScheduleService;

    private DoctorSchedule schedule;
    private DoctorScheduleCreateDto createDto;
    private DoctorScheduleResponseDto responseDto;
    private Doctor doctor;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        doctor.setId(1L);

        schedule = new DoctorSchedule();
        schedule.setId(1L);
        schedule.setDoctor(doctor);
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setStartTime(LocalDateTime.of(2026, 7, 27, 9, 0));
        schedule.setEndTime(LocalDateTime.of(2026, 7, 27, 17, 0));
        schedule.setActive(true);

        createDto = new DoctorScheduleCreateDto();
        createDto.setDoctorId(1L);
        createDto.setDayOfWeek(DayOfWeek.MONDAY);
        createDto.setStartTime(LocalDateTime.of(2026, 7, 27, 9, 0));
        createDto.setEndTime(LocalDateTime.of(2026, 7, 27, 17, 0));

        responseDto = new DoctorScheduleResponseDto();
        responseDto.setId(1L);
        responseDto.setDayOfWeek(DayOfWeek.MONDAY);
    }

    @Test
    @DisplayName("Should create doctor schedule")
    void shouldCreateDoctorSchedule() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorScheduleMapper.toEntity(any())).thenReturn(schedule);
        when(doctorScheduleRepository.save(any())).thenReturn(schedule);
        when(doctorScheduleMapper.toResponseDto(any())).thenReturn(responseDto);

        DoctorScheduleResponseDto result = doctorScheduleService.createDoctorSchedule(createDto);

        assertThat(result).isNotNull();
        verify(doctorScheduleRepository).save(any());
    }

    @Test
    @DisplayName("Should get schedules by doctor id")
    void shouldGetByDoctorId() {
        when(doctorScheduleRepository.findByDoctorId(1L)).thenReturn(List.of(schedule));
        when(doctorScheduleMapper.toResponseDto(any())).thenReturn(responseDto);

        List<DoctorScheduleResponseDto> result = doctorScheduleService.getDoctorSchedulesByDoctorId(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Should get schedule by doctor and day")
    void shouldGetByDoctorAndDay() {
        when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(1L, DayOfWeek.MONDAY))
                .thenReturn(Optional.of(schedule));
        when(doctorScheduleMapper.toResponseDto(any())).thenReturn(responseDto);

        DoctorScheduleResponseDto result = doctorScheduleService.getDoctorScheduleByDoctorAndDay(1L, DayOfWeek.MONDAY);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should throw when doctor not found on create")
    void shouldThrowWhenDoctorNotFound() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> doctorScheduleService.createDoctorSchedule(createDto))
                .isInstanceOf(DoctorNotFoundException.class);
    }
}
