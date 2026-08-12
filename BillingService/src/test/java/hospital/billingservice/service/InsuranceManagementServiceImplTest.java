package hospital.billingservice.service;

import hospital.billingservice.dto.insurancemanagement.InsuranceManagementCreateDto;
import hospital.billingservice.dto.insurancemanagement.InsuranceManagementResponseDto;
import hospital.billingservice.exception.insurance.DuplicateInsuranceCodeException;
import hospital.billingservice.exception.insurance.InsuranceManagementNotFoundException;
import hospital.billingservice.mapper.InsuranceManagementMapper;
import hospital.billingservice.model.InsuranceManagement;
import hospital.billingservice.repository.InsuranceManagementRepository;
import hospital.billingservice.service.impl.InsuranceManagementServiceImpl;
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
 * Unit tests for {@link InsuranceManagementServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class InsuranceManagementServiceImplTest {

    @Mock private InsuranceManagementRepository insuranceRepository;
    @Mock private InsuranceManagementMapper insuranceMapper;

    @InjectMocks
    private InsuranceManagementServiceImpl insuranceService;

    private InsuranceManagement testInsurance;

    @BeforeEach
    void setUp() {
        testInsurance = InsuranceManagement.builder()
                .id(1L)
                .name("Tamin Ejtemaei")
                .code("TE-001")
                .coveragePercent(90)
                .isActive(true)
                .build();
    }

    @Nested
    @DisplayName("Create Insurance")
    class CreateInsuranceTests {

        @Test
        @DisplayName("should create insurance successfully")
        void shouldCreateInsurance() {
            InsuranceManagementCreateDto dto = InsuranceManagementCreateDto.builder()
                    .name("Tamin Ejtemaei").code("TE-001").build();

            when(insuranceRepository.existsByCode("TE-001")).thenReturn(false);
            when(insuranceMapper.toEntity(any(InsuranceManagementCreateDto.class))).thenReturn(testInsurance);
            when(insuranceMapper.toResponseDto(any(InsuranceManagement.class)))
                    .thenReturn(InsuranceManagementResponseDto.builder().id(1L).name("Tamin Ejtemaei").build());
            when(insuranceRepository.save(any(InsuranceManagement.class))).thenReturn(testInsurance);

            InsuranceManagementResponseDto result = insuranceService.createInsurance(dto);

            assertThat(result.getName()).isEqualTo("Tamin Ejtemaei");
            verify(insuranceRepository).save(argThat(i -> i.getIsActive()));
        }

        @Test
        @DisplayName("should throw when code exists")
        void shouldThrowWhenCodeExists() {
            InsuranceManagementCreateDto dto = InsuranceManagementCreateDto.builder().code("TE-001").build();

            when(insuranceRepository.existsByCode("TE-001")).thenReturn(true);

            assertThatThrownBy(() -> insuranceService.createInsurance(dto))
                    .isInstanceOf(DuplicateInsuranceCodeException.class);
        }
    }

    @Nested
    @DisplayName("Read Insurance")
    class ReadInsuranceTests {

        @Test
        @DisplayName("should get insurance by id")
        void shouldGetById() {
            InsuranceManagementResponseDto expected = InsuranceManagementResponseDto.builder()
                    .id(1L).name("Tamin Ejtemaei").build();

            when(insuranceRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testInsurance));
            when(insuranceMapper.toResponseDto(testInsurance)).thenReturn(expected);

            InsuranceManagementResponseDto result = insuranceService.getInsuranceById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(insuranceRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> insuranceService.getInsuranceById(999L))
                    .isInstanceOf(InsuranceManagementNotFoundException.class);
        }

        @Test
        @DisplayName("should get all active insurances")
        void shouldGetActiveInsurances() {
            when(insuranceRepository.findByIsActiveTrue()).thenReturn(List.of(testInsurance));
            when(insuranceMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    InsuranceManagementResponseDto.builder().id(1L).build()));

            assertThat(insuranceService.getAllActiveInsurances()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Update Insurance")
    class UpdateInsuranceTests {

        @Test
        @DisplayName("should toggle active status")
        void shouldToggleActive() {
            testInsurance.setIsActive(true);
            InsuranceManagementResponseDto expected = InsuranceManagementResponseDto.builder().build();

            when(insuranceRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testInsurance));
            when(insuranceMapper.toResponseDto(any(InsuranceManagement.class))).thenReturn(expected);
            when(insuranceRepository.save(any(InsuranceManagement.class))).thenReturn(testInsurance);

            insuranceService.toggleActive(1L);

            verify(insuranceRepository).save(argThat(i -> !i.getIsActive()));
        }
    }

    @Nested
    @DisplayName("Delete Insurance")
    class DeleteInsuranceTests {

        @Test
        @DisplayName("should soft delete insurance")
        void shouldSoftDelete() {
            when(insuranceRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testInsurance));

            insuranceService.deleteInsurance(1L);

            verify(insuranceRepository).save(argThat(InsuranceManagement::isDeleted));
        }
    }

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should check code existence")
        void shouldCheckCodeExistence() {
            when(insuranceRepository.existsByCode("TE-001")).thenReturn(true);

            assertThat(insuranceService.codeExists("TE-001")).isTrue();
        }
    }
}
