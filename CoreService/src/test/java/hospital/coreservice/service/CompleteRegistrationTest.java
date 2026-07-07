package hospital.coreservice.service;

import hospital.coreservice.client.AuthClient;
import hospital.coreservice.dto.patient.PatientResponseDto;
import hospital.coreservice.dto.request.CompleteRegistrationRequest;
import hospital.coreservice.mapper.PatientMapper;
import hospital.coreservice.model.Patient;
import hospital.coreservice.model.enums.BloodType;
import hospital.coreservice.model.enums.Gender;
import hospital.coreservice.repository.PatientRepository;
import hospital.coreservice.repository.RoomRepository;
import hospital.coreservice.service.imp.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CompleteRegistration - CoreService only updates patient data")
class CompleteRegistrationTest {

    @Mock
    PatientRepository patientRepository;

    @Mock
    RoomRepository roomRepository;

    @Mock
    PatientMapper patientMapper;

    @Mock
    AuthClient authClient;

    @InjectMocks
    PatientServiceImpl patientService;

    Patient mockPatient;
    CompleteRegistrationRequest request;

    @BeforeEach
    void setUp() {
        mockPatient = new Patient();
        mockPatient.setId(3L);
        mockPatient.setUserId(10L);
        mockPatient.setNationalId("0123456789");
        mockPatient.setFirstName("علی");
        mockPatient.setLastName("احمدی");
        mockPatient.setPhoneNumber("09123456789");
        mockPatient.setGender(Gender.UNKNOWN);
        mockPatient.setBloodType(BloodType.UNKNOWN);
        mockPatient.setAddress("ثبت نشده");
        mockPatient.setBirthDate(LocalDate.now().minusYears(30));

        request = new CompleteRegistrationRequest();
        request.setPatientId(3L);
        request.setUsername("ali_ahmadi");
        request.setEmail("ali@gmail.com");
        request.setNewPassword("MyNewPass@123");
        request.setConfirmPassword("MyNewPass@123");
        request.setFirstName("علی");
        request.setLastName("احمدی");
        request.setPhoneNumber("09123456789");
        request.setGender(Gender.MAN);
        request.setBloodType(BloodType.A_POSITIVE);
        request.setAddress("تهران، خیابان ولیعصر");
        request.setBirthDate(LocalDate.of(1990, 5, 15));
        request.setAllergies("پنی‌سیلین");
    }

    @Test
    @DisplayName("Should complete patient registration without touching AuthService user data")
    void shouldCompletePatientRegistration() {
        when(patientRepository.findById(3L)).thenReturn(Optional.of(mockPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(mockPatient);
        when(patientMapper.toResponseDto(any(Patient.class))).thenReturn(new PatientResponseDto());

        assertThatNoException().isThrownBy(() ->
                patientService.completeRegistration(3L, request)
        );

        verify(patientMapper).updatePatientFromRegistration(mockPatient, request);
        verify(patientRepository).save(mockPatient);
    }
}