package hospital.billingservice.service;

import hospital.billingservice.dto.patientinsurance.PatientInsuranceCreateDto;
import hospital.billingservice.dto.patientinsurance.PatientInsuranceResponseDto;
import hospital.billingservice.exception.patientinsurance.DuplicatePolicyNumberException;
import hospital.billingservice.exception.patientinsurance.PatientInsuranceNotFoundException;
import hospital.billingservice.mapper.PatientInsuranceMapper;
import hospital.billingservice.model.PatientInsurance;
import hospital.billingservice.repository.PatientInsuranceRepository;
import hospital.billingservice.service.impl.PatientInsuranceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PatientInsuranceServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class PatientInsuranceServiceImplTest {

    @Mock private PatientInsuranceRepository patientInsuranceRepository;
    @Mock private PatientInsuranceMapper patientInsuranceMapper;

    @InjectMocks
    private PatientInsuranceServiceImpl patientInsuranceService;

    private PatientInsurance testPatientInsurance;

    @BeforeEach
    void setUp() {
        testPatientInsurance = PatientInsurance.builder()
                .id(1L)
                .patientId(100L)
                .policyNumber("POL-001")
                .isPrimary(true)
                .build();
    }

    @Nested
    @DisplayName("Create Patient Insurance")
    class CreatePatientInsuranceTests {

        @Test
        @DisplayName("should create patient insurance successfully")
        void shouldCreatePatientInsurance() {
            PatientInsuranceCreateDto dto = PatientInsuranceCreateDto.builder()
                    .patientId(100L).policyNumber("POL-001").build();

            when(patientInsuranceRepository.existsByPolicyNumber("POL-001")).thenReturn(false);
            when(patientInsuranceMapper.toEntity(any(PatientInsuranceCreateDto.class))).thenReturn(testPatientInsurance);
            when(patientInsuranceMapper.toResponseDto(any(PatientInsurance.class)))
                    .thenReturn(PatientInsuranceResponseDto.builder().id(1L).build());
            when(patientInsuranceRepository.save(any(PatientInsurance.class))).thenReturn(testPatientInsurance);

            PatientInsuranceResponseDto result = patientInsuranceService.createPatientInsurance(dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(patientInsuranceRepository).save(any(PatientInsurance.class));
        }

        @Test
        @DisplayName("should throw when policy number exists")
        void shouldThrowWhenPolicyNumberExists() {
            PatientInsuranceCreateDto dto = PatientInsuranceCreateDto.builder().policyNumber("POL-001").build();

            when(patientInsuranceRepository.existsByPolicyNumber("POL-001")).thenReturn(true);

            assertThatThrownBy(() -> patientInsuranceService.createPatientInsurance(dto))
                    .isInstanceOf(DuplicatePolicyNumberException.class);
        }
    }

    @Nested
    @DisplayName("Read Patient Insurance")
    class ReadPatientInsuranceTests {

        @Test
        @DisplayName("should get patient insurance by id")
        void shouldGetById() {
            PatientInsuranceResponseDto expected = PatientInsuranceResponseDto.builder().id(1L).build();

            when(patientInsuranceRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testPatientInsurance));
            when(patientInsuranceMapper.toResponseDto(testPatientInsurance)).thenReturn(expected);

            PatientInsuranceResponseDto result = patientInsuranceService.getPatientInsuranceById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(patientInsuranceRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> patientInsuranceService.getPatientInsuranceById(999L))
                    .isInstanceOf(PatientInsuranceNotFoundException.class);
        }

        @Test
        @DisplayName("should get insurances by patient")
        void shouldGetByPatient() {
            when(patientInsuranceRepository.findByPatientId(100L)).thenReturn(List.of(testPatientInsurance));
            when(patientInsuranceMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    PatientInsuranceResponseDto.builder().id(1L).build()));

            assertThat(patientInsuranceService.getInsurancesByPatient(100L)).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Delete Patient Insurance")
    class DeletePatientInsuranceTests {

        @Test
        @DisplayName("should soft delete patient insurance")
        void shouldSoftDelete() {
            when(patientInsuranceRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testPatientInsurance));

            patientInsuranceService.deletePatientInsurance(1L);

            verify(patientInsuranceRepository).save(argThat(PatientInsurance::isDeleted));
        }
    }

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should check policy number existence")
        void shouldCheckPolicyNumberExistence() {
            when(patientInsuranceRepository.existsByPolicyNumber("POL-001")).thenReturn(true);

            assertThat(patientInsuranceService.policyNumberExists("POL-001")).isTrue();
        }
    }
}
