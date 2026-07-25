package hospital.coreservice.service;

import hospital.coreservice.client.AuthClient;
import hospital.coreservice.dto.patient.PatientCreateDto;
import hospital.coreservice.dto.patient.PatientResponseDto;
import hospital.coreservice.dto.request.CompleteRegistrationRequest;
import hospital.coreservice.exception.patient.DuplicateNationalIdException;
import hospital.coreservice.exception.patient.DuplicatePhoneNumberException;
import hospital.coreservice.exception.patient.PatientNotFoundException;
import hospital.coreservice.mapper.PatientMapper;
import hospital.coreservice.model.Patient;
import hospital.coreservice.model.Room;
import hospital.coreservice.model.enums.BloodType;
import hospital.coreservice.model.enums.Gender;
import hospital.coreservice.model.enums.PatientStatus;
import hospital.coreservice.repository.PatientRepository;
import hospital.coreservice.repository.RoomRepository;
import hospital.coreservice.service.imp.PatientServiceImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PatientService Tests - CoreService")
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private AuthClient authClient;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient patient;
    private PatientCreateDto createDto;
    private PatientResponseDto responseDto;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setNationalId("1234567890");
        patient.setFirstName("علی");
        patient.setLastName("رضایی");
        patient.setPhoneNumber("09123456789");
        patient.setGender(Gender.MAN);
        patient.setBloodType(BloodType.A_POSITIVE);
        patient.setStatus(PatientStatus.ACTIVE);
        patient.setBirthDate(LocalDate.of(1995, 5, 15));

        createDto = new PatientCreateDto();
        createDto.setUserId(20L);
        createDto.setNationalId("1234567890");
        createDto.setFirstName("علی");
        createDto.setLastName("رضایی");
        createDto.setPhoneNumber("09123456789");
        createDto.setGender(Gender.MAN);
        createDto.setBloodType(BloodType.A_POSITIVE);
        createDto.setBirthDate(LocalDate.of(1995, 5, 15));

        responseDto = new PatientResponseDto();
        responseDto.setId(1L);
        responseDto.setNationalId("1234567890");
        responseDto.setFirstName("علی");
        responseDto.setLastName("رضایی");
    }

    // ==================== CREATE ====================

    @Nested
    @DisplayName("Create Patient")
    class CreatePatient {

        @Test
        @DisplayName("Should create patient successfully")
        void shouldCreatePatientSuccessfully() {
            doNothing().when(authClient).validateUserHasRole(anyLong(), anyString());
            when(patientRepository.existsByNationalId(any())).thenReturn(false);
            when(patientRepository.existsByPhoneNumber(any())).thenReturn(false);
            when(patientMapper.toEntity(any())).thenReturn(patient);
            when(patientRepository.save(any())).thenReturn(patient);
            when(patientMapper.toResponseDto(any())).thenReturn(responseDto);

            PatientResponseDto result = patientService.createPatient(createDto);

            assertThat(result).isNotNull();
            assertThat(result.getNationalId()).isEqualTo("1234567890");
            verify(patientRepository).save(any(Patient.class));
            verify(authClient).validateUserHasRole(20L, "PATIENT");
        }

        @Test
        @DisplayName("Should throw DuplicateNationalIdException")
        void shouldThrowOnDuplicateNationalId() {
            when(patientRepository.existsByNationalId("1234567890")).thenReturn(true);

            assertThatThrownBy(() -> patientService.createPatient(createDto))
                    .isInstanceOf(DuplicateNationalIdException.class);
        }

        @Test
        @DisplayName("Should throw DuplicatePhoneNumberException")
        void shouldThrowOnDuplicatePhone() {
            when(patientRepository.existsByNationalId(any())).thenReturn(false);
            when(patientRepository.existsByPhoneNumber("09123456789")).thenReturn(true);

            assertThatThrownBy(() -> patientService.createPatient(createDto))
                    .isInstanceOf(DuplicatePhoneNumberException.class);
        }
    }

    // ==================== GET ====================

    @Nested
    @DisplayName("Get Patients")
    class GetPatients {

        @Test
        @DisplayName("Should return patient by id")
        void shouldReturnById() {
            when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
            when(patientMapper.toResponseDto(any())).thenReturn(responseDto);

            PatientResponseDto result = patientService.getPatientById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Should throw when patient not found")
        void shouldThrowWhenNotFound() {
            when(patientRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> patientService.getPatientById(999L))
                    .isInstanceOf(PatientNotFoundException.class);
        }

        @Test
        @DisplayName("Should return all patients")
        void shouldReturnAll() {
            when(patientRepository.findAll()).thenReturn(List.of(patient));
            when(patientMapper.toResponseDto(any())).thenReturn(responseDto);

            List<PatientResponseDto> result = patientService.getAllPatients();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Should return patient by national id")
        void shouldReturnByNationalId() {
            when(patientRepository.findByNationalId("1234567890")).thenReturn(Optional.of(patient));
            when(patientMapper.toResponseDto(any())).thenReturn(responseDto);

            PatientResponseDto result = patientService.getPatientByNationalId("1234567890");

            assertThat(result).isNotNull();
        }
    }

    // ==================== UPDATE ====================

    @Test
    @DisplayName("Should update patient")
    void shouldUpdatePatient() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any())).thenReturn(patient);
        when(patientMapper.toResponseDto(any())).thenReturn(responseDto);

        PatientResponseDto result = patientService.updatePatient(1L, new hospital.coreservice.dto.patient.PatientUpdateDto());

        assertThat(result).isNotNull();
        verify(patientRepository).save(any());
    }

    // ==================== ROOM ASSIGNMENT ====================

    @Nested
    @DisplayName("Room Assignment")
    class RoomAssignment {

        @Test
        @DisplayName("Should assign patient to room")
        void shouldAssignRoom() {
            Room room = new Room();
            room.setId(5L);
            room.setCapacity(2);
            room.setCurrentPatientList(new java.util.ArrayList<>());

            when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
            when(roomRepository.findById(5L)).thenReturn(Optional.of(room));
            when(patientRepository.save(any())).thenReturn(patient);
            when(roomRepository.save(any())).thenReturn(room);

            patientService.assignRoom(1L, 5L);

            verify(patientRepository).save(any());
            verify(roomRepository).save(any());
        }

        @Test
        @DisplayName("Should unassign patient from room")
        void shouldUnassignRoom() {
            Room room = new Room();
            room.setId(5L);
            patient.setCurrentRoom(room);

            when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
            when(patientRepository.save(any())).thenReturn(patient);
            when(roomRepository.save(any())).thenReturn(room);

            patientService.unassignRoom(1L);

            assertThat(patient.getCurrentRoom()).isNull();
        }
    }

    // ==================== COMPLETE REGISTRATION ====================

    @Test
    @DisplayName("Should complete patient registration")
    void shouldCompleteRegistration() {
        CompleteRegistrationRequest request = new CompleteRegistrationRequest();
        request.setFirstName("علی");
        request.setLastName("رضایی");
        request.setPhoneNumber("09123456789");
        request.setGender(Gender.MAN);
        request.setBloodType(BloodType.A_POSITIVE);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any())).thenReturn(patient);
        when(patientMapper.toResponseDto(any())).thenReturn(responseDto);

        PatientResponseDto result = patientService.completeRegistration(1L, request);

        assertThat(result).isNotNull();
        verify(patientMapper).updatePatientFromRegistration(patient, request);
        verify(patientRepository).save(patient);
    }

    // ==================== COUNTING ====================

    @Test
    @DisplayName("Should count active patients")
    void shouldCountActivePatients() {
        when(patientRepository.countByStatus(PatientStatus.ACTIVE)).thenReturn(42L);

        Long count = patientService.countActivePatients();

        assertThat(count).isEqualTo(42L);
    }

    @Test
    @DisplayName("Should count all patients")
    void shouldCountAllPatients() {
        when(patientRepository.count()).thenReturn(150L);

        Long count = patientService.countAllPatients();

        assertThat(count).isEqualTo(150L);
    }
}
