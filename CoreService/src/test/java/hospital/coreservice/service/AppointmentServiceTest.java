package hospital.coreservice.service;

import hospital.coreservice.dto.appointment.AppointmentCreateDto;
import hospital.coreservice.dto.appointment.AppointmentResponseDto;
import hospital.coreservice.dto.appointment.TimeSlotResponseDto;
import hospital.coreservice.exception.appointment.AppointmentNotFoundException;
import hospital.coreservice.exception.doctor.DoctorNotAvailableException;
import hospital.coreservice.exception.doctor.DoctorNotFoundException;
import hospital.coreservice.exception.patient.PatientNotFoundException;
import hospital.coreservice.mapper.AppointmentMapper;
import hospital.coreservice.model.*;
import hospital.coreservice.model.enums.*;
import hospital.coreservice.repository.*;
import hospital.coreservice.service.imp.AppointmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AppointmentService Tests - CoreService")
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @Mock
    private DoctorScheduleRepository doctorScheduleRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private Patient patient;
    private Doctor doctor;
    private Department department;
    private AppointmentCreateDto createDto;
    private Appointment appointment;
    private AppointmentResponseDto responseDto;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("علی");
        patient.setLastName("رضایی");

        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("دکتر");
        doctor.setLastName("محمدی");

        department = new Department();
        department.setId(1L);
        department.setDepartmentName("داخلی");

        createDto = new AppointmentCreateDto();
        createDto.setPatientId(1L);
        createDto.setDoctorId(1L);
        createDto.setDepartmentId(1L);
        createDto.setAppointmentDate(LocalDate.now().plusDays(1));
        createDto.setStartTime(LocalTime.of(10, 0));
        createDto.setEndTime(LocalTime.of(10, 30));
        createDto.setReason("درد قفسه سینه");
        createDto.setType(AppointmentType.IN_PERSON);

        appointment = new Appointment();
        appointment.setId(100L);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDepartment(department);
        appointment.setAppointmentDate(createDto.getAppointmentDate());
        appointment.setStartTime(createDto.getStartTime());
        appointment.setEndTime(createDto.getEndTime());
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setReason(createDto.getReason());
        appointment.setType(createDto.getType());

        responseDto = new AppointmentResponseDto();
        responseDto.setId(100L);
        responseDto.setStatus(AppointmentStatus.SCHEDULED);
        responseDto.setType(AppointmentType.IN_PERSON);
        responseDto.setAppointmentDate(createDto.getAppointmentDate());
        responseDto.setStartTime(createDto.getStartTime());
        responseDto.setEndTime(createDto.getEndTime());
        responseDto.setReason(createDto.getReason());
    }

    // ==================== CREATE ====================

    @Nested
    @DisplayName("Create Appointment")
    class CreateAppointment {

        @Test
        @DisplayName("Should create appointment successfully when doctor is available")
        void shouldCreateAppointmentSuccessfully() {
            when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
            when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
            when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
            when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(any(), any()))
                    .thenReturn(Optional.of(new DoctorSchedule()));
            when(appointmentRepository.findByDoctorIdAndAppointmentDate(any(), any()))
                    .thenReturn(List.of());
            when(appointmentMapper.toEntity(any())).thenReturn(appointment);
            when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
            when(appointmentMapper.toResponseDto(any())).thenReturn(responseDto);

            AppointmentResponseDto result = appointmentService.createAppointment(createDto);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(100L);
            verify(appointmentRepository).save(any(Appointment.class));
        }

        @Test
        @DisplayName("Should throw DoctorNotAvailableException when doctor has no schedule")
        void shouldThrowWhenDoctorHasNoSchedule() {
            when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
            when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
            when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
            when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(any(), any()))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> appointmentService.createAppointment(createDto))
                    .isInstanceOf(DoctorNotAvailableException.class);
        }

        @Test
        @DisplayName("Should throw PatientNotFoundException when patient not found")
        void shouldThrowWhenPatientNotFound() {
            when(patientRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> appointmentService.createAppointment(createDto))
                    .isInstanceOf(PatientNotFoundException.class);
        }

        @Test
        @DisplayName("Should throw DoctorNotFoundException when doctor not found")
        void shouldThrowWhenDoctorNotFound() {
            when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
            when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> appointmentService.createAppointment(createDto))
                    .isInstanceOf(DoctorNotFoundException.class);
        }
    }

    // ==================== GET ====================

    @Nested
    @DisplayName("Get Appointments")
    class GetAppointments {

        @Test
        @DisplayName("Should return appointment by id")
        void shouldReturnAppointmentById() {
            when(appointmentRepository.findById(100L)).thenReturn(Optional.of(appointment));
            when(appointmentMapper.toResponseDto(appointment)).thenReturn(responseDto);

            AppointmentResponseDto result = appointmentService.getAppointmentById(100L);

            assertThat(result.getId()).isEqualTo(100L);
        }

        @Test
        @DisplayName("Should throw when appointment not found by id")
        void shouldThrowWhenAppointmentNotFound() {
            when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> appointmentService.getAppointmentById(999L))
                    .isInstanceOf(AppointmentNotFoundException.class);
        }

        @Test
        @DisplayName("Should return appointments by patient id")
        void shouldReturnByPatientId() {
            when(appointmentRepository.findByPatientId(1L)).thenReturn(List.of(appointment));
            when(appointmentMapper.toResponseDto(any())).thenReturn(responseDto);

            List<AppointmentResponseDto> result = appointmentService.getAppointmentsByPatientId(1L);

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Should return appointments by doctor id")
        void shouldReturnByDoctorId() {
            when(appointmentRepository.findByDoctorId(1L)).thenReturn(List.of(appointment));
            when(appointmentMapper.toResponseDto(any())).thenReturn(responseDto);

            List<AppointmentResponseDto> result = appointmentService.getAppointmentsByDoctorId(1L);

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Should return today appointments")
        void shouldReturnTodayAppointments() {
            when(appointmentRepository.findByAppointmentDate(any(LocalDate.class)))
                    .thenReturn(List.of(appointment));
            when(appointmentMapper.toResponseDto(any())).thenReturn(responseDto);

            List<AppointmentResponseDto> result = appointmentService.getTodayAppointments();

            assertThat(result).hasSize(1);
        }
    }

    // ==================== STATUS TRANSITIONS ====================

    @Nested
    @DisplayName("Status Transitions")
    class StatusTransitions {

        @Test
        @DisplayName("Should cancel scheduled appointment")
        void shouldCancelScheduledAppointment() {
            when(appointmentRepository.findById(100L)).thenReturn(Optional.of(appointment));

            appointmentService.cancelAppointment(100L, "بیمار انصراف داد", 5L);

            verify(appointmentRepository).cancelAppointment(100L, "بیمار انصراف داد", 5L);
        }

        @Test
        @DisplayName("Should check-in scheduled appointment")
        void shouldCheckInScheduledAppointment() {
            when(appointmentRepository.findById(100L)).thenReturn(Optional.of(appointment));

            appointmentService.checkInAppointment(100L);

            verify(appointmentRepository).updateStatus(100L, AppointmentStatus.CHECK_IN);
        }

        @Test
        @DisplayName("Should complete checked-in appointment")
        void shouldCompleteCheckedInAppointment() {
            appointment.setStatus(AppointmentStatus.CHECK_IN);
            when(appointmentRepository.findById(100L)).thenReturn(Optional.of(appointment));

            appointmentService.completeAppointment(100L);

            verify(appointmentRepository).updateStatus(100L, AppointmentStatus.COMPLETED);
        }

        @Test
        @DisplayName("Should not cancel non-scheduled appointment")
        void shouldNotCancelNonScheduled() {
            appointment.setStatus(AppointmentStatus.COMPLETED);
            when(appointmentRepository.findById(100L)).thenReturn(Optional.of(appointment));

            assertThatThrownBy(() -> appointmentService.cancelAppointment(100L, "test", 1L))
                    .isInstanceOf(hospital.coreservice.exception.appointment.InvalidCancelStateException.class);
        }
    }

    // ==================== AVAILABLE SLOTS ====================

    @Nested
    @DisplayName("Available Slots")
    class AvailableSlots {

        @Test
        @DisplayName("Should return available slots when schedule exists")
        void shouldReturnAvailableSlots() {
            DoctorSchedule schedule = new DoctorSchedule();
            schedule.setDoctor(doctor);
            schedule.setDayOfWeek(DayOfWeek.MONDAY);
            schedule.setStartTime(LocalDateTime.of(2026, 7, 27, 9, 0));
            schedule.setEndTime(LocalDateTime.of(2026, 7, 27, 10, 0));
            schedule.setSlotDuration(30);

            when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(eq(1L), any()))
                    .thenReturn(Optional.of(schedule));
            when(appointmentRepository.findByDoctorIdAndAppointmentDate(any(), any()))
                    .thenReturn(List.of());

            List<TimeSlotResponseDto> slots = appointmentService.getAvailableSlots(1L, LocalDate.of(2026, 7, 27));

            assertThat(slots).hasSize(2);
            assertThat(slots.get(0).getStartTime()).isEqualTo(LocalTime.of(9, 0));
        }

        @Test
        @DisplayName("Should return empty list when no schedule")
        void shouldReturnEmptyWhenNoSchedule() {
            when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(any(), any()))
                    .thenReturn(Optional.empty());

            List<TimeSlotResponseDto> slots = appointmentService.getAvailableSlots(1L, LocalDate.now());

            assertThat(slots).isEmpty();
        }
    }

    // ==================== RESCHEDULE ====================

    @Nested
    @DisplayName("Reschedule")
    class Reschedule {

        @Test
        @DisplayName("Should reschedule scheduled appointment")
        void shouldRescheduleAppointment() {
            when(appointmentRepository.findById(100L)).thenReturn(Optional.of(appointment));
            when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(any(), any()))
                    .thenReturn(Optional.of(new DoctorSchedule()));
            when(appointmentRepository.findByDoctorIdAndAppointmentDate(any(), any()))
                    .thenReturn(List.of());
            when(appointmentRepository.save(any())).thenReturn(appointment);
            when(appointmentMapper.toResponseDto(any())).thenReturn(responseDto);

            LocalDate newDate = LocalDate.now().plusDays(2);
            LocalTime newStart = LocalTime.of(11, 0);
            LocalTime newEnd = LocalTime.of(11, 30);

            AppointmentResponseDto result = appointmentService.rescheduleAppointment(100L, newDate, newStart, newEnd);

            assertThat(result).isNotNull();
            verify(appointmentRepository).save(any());
        }
    }

    // ==================== STATISTICS ====================

    @Test
    @DisplayName("Should return total appointment count")
    void shouldReturnTotalCount() {
        when(appointmentRepository.count()).thenReturn(150L);

        Long count = appointmentService.countTotalAppointments();

        assertThat(count).isEqualTo(150L);
    }
}
