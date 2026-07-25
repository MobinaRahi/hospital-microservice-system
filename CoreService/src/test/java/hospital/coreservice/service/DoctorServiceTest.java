package hospital.coreservice.service;

import hospital.coreservice.client.AuthClient;
import hospital.coreservice.dto.doctor.DoctorCreateDto;
import hospital.coreservice.dto.doctor.DoctorResponseDto;
import hospital.coreservice.exception.department.DepartmentNotFoundException;
import hospital.coreservice.exception.doctor.DoctorNotFoundException;
import hospital.coreservice.mapper.DoctorMapper;
import hospital.coreservice.model.Department;
import hospital.coreservice.model.Doctor;
import hospital.coreservice.model.enums.Speciality;
import hospital.coreservice.repository.DepartmentRepository;
import hospital.coreservice.repository.DoctorRepository;
import hospital.coreservice.repository.DoctorScheduleRepository;
import hospital.coreservice.service.imp.DoctorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("DoctorService Tests - CoreService")
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @Mock
    private DoctorScheduleRepository doctorScheduleRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Doctor doctor;
    private DoctorCreateDto createDto;
    private DoctorResponseDto responseDto;
    private Department department;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("دکتر");
        doctor.setLastName("محمدی");
        doctor.setLicenseNumber("LIC-12345");
        doctor.setSpeciality(Speciality.CARDIOLOGY);
        doctor.setActive(true);

        department = new Department();
        department.setId(10L);
        department.setDepartmentName("قلب و عروق");

        createDto = new DoctorCreateDto();
        createDto.setUserId(5L);
        createDto.setFirstName("دکتر");
        createDto.setLastName("محمدی");
        createDto.setLicenseNumber("LIC-12345");
        createDto.setSpeciality(Speciality.CARDIOLOGY);
        createDto.setDepartmentId(10L);

        responseDto = new DoctorResponseDto();
        responseDto.setId(1L);
        responseDto.setFirstName("دکتر");
        responseDto.setLastName("محمدی");
        responseDto.setLicenseNumber("LIC-12345");
        responseDto.setSpeciality(Speciality.CARDIOLOGY);
    }

    // ==================== CREATE ====================

    @Nested
    @DisplayName("Create Doctor")
    class CreateDoctor {

        @Test
        @DisplayName("Should create doctor successfully")
        void shouldCreateDoctorSuccessfully() {
            doNothing().when(authClient).validateUserHasRole(anyLong(), anyString());
            when(departmentRepository.findById(10L)).thenReturn(Optional.of(department));
            when(doctorMapper.toEntity(any())).thenReturn(doctor);
            when(doctorRepository.save(any())).thenReturn(doctor);
            when(doctorMapper.toResponseDto(any())).thenReturn(responseDto);

            DoctorResponseDto result = doctorService.createDoctor(createDto);

            assertThat(result).isNotNull();
            assertThat(result.getLicenseNumber()).isEqualTo("LIC-12345");
            verify(doctorRepository).save(any(Doctor.class));
            verify(authClient).validateUserHasRole(5L, "DOCTOR");
        }

        @Test
        @DisplayName("Should throw when department not found")
        void shouldThrowWhenDepartmentNotFound() {
            doNothing().when(authClient).validateUserHasRole(anyLong(), anyString());
            when(departmentRepository.findById(10L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> doctorService.createDoctor(createDto))
                    .isInstanceOf(DepartmentNotFoundException.class);
        }
    }

    // ==================== GET ====================

    @Nested
    @DisplayName("Get Doctors")
    class GetDoctors {

        @Test
        @DisplayName("Should return doctor by id")
        void shouldReturnById() {
            when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
            when(doctorMapper.toResponseDto(any())).thenReturn(responseDto);

            DoctorResponseDto result = doctorService.getDoctorById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Should throw when doctor not found")
        void shouldThrowWhenNotFound() {
            when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> doctorService.getDoctorById(999L))
                    .isInstanceOf(DoctorNotFoundException.class);
        }

        @Test
        @DisplayName("Should return all doctors")
        void shouldReturnAll() {
            when(doctorRepository.findAll()).thenReturn(List.of(doctor));
            when(doctorMapper.toResponseDto(any())).thenReturn(responseDto);

            List<DoctorResponseDto> result = doctorService.getAllDoctors();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Should return active doctors")
        void shouldReturnActive() {
            when(doctorRepository.findAllActive()).thenReturn(List.of(doctor));
            when(doctorMapper.toResponseDto(any())).thenReturn(responseDto);

            List<DoctorResponseDto> result = doctorService.getActiveDoctors();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Should return doctors by speciality")
        void shouldReturnBySpeciality() {
            when(doctorRepository.findBySpeciality(Speciality.CARDIOLOGY)).thenReturn(List.of(doctor));
            when(doctorMapper.toResponseDto(any())).thenReturn(responseDto);

            List<DoctorResponseDto> result = doctorService.getDoctorsBySpeciality(Speciality.CARDIOLOGY);

            assertThat(result).hasSize(1);
        }
    }

    // ==================== UPDATE ====================

    @Test
    @DisplayName("Should update doctor")
    void shouldUpdateDoctor() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any())).thenReturn(doctor);
        when(doctorMapper.toResponseDto(any())).thenReturn(responseDto);

        DoctorResponseDto result = doctorService.updateDoctor(1L, new hospital.coreservice.dto.doctor.DoctorUpdateDto());

        assertThat(result).isNotNull();
        verify(doctorRepository).save(any());
    }

    // ==================== ACTIVATE / DEACTIVATE ====================

    @Test
    @DisplayName("Should activate doctor")
    void shouldActivateDoctor() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        doctorService.activateDoctor(1L);

        verify(doctorRepository).activate(1L);
    }

    @Test
    @DisplayName("Should deactivate doctor")
    void shouldDeactivateDoctor() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        doctorService.deactivateDoctor(1L);

        verify(doctorRepository).deactivate(1L);
    }

    // ==================== DEPARTMENT ====================

    @Test
    @DisplayName("Should assign department to doctor")
    void shouldAssignDepartment() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(departmentRepository.findById(10L)).thenReturn(Optional.of(department));

        doctorService.assignDepartment(1L, 10L);

        verify(doctorRepository).save(any());
    }

    // ==================== COUNT ====================

    @Test
    @DisplayName("Should count active doctors")
    void shouldCountActiveDoctors() {
        when(doctorRepository.countActive()).thenReturn(25L);

        Long count = doctorService.countActiveDoctors();

        assertThat(count).isEqualTo(25L);
    }

    @Test
    @DisplayName("Should count all doctors")
    void shouldCountAllDoctors() {
        when(doctorRepository.count()).thenReturn(80L);

        Long count = doctorService.countAllDoctors();

        assertThat(count).isEqualTo(80L);
    }
}
