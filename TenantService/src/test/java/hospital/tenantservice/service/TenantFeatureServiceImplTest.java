package hospital.tenantservice.service;

import hospital.tenantservice.dto.tenantfeature.TenantFeatureCreateDto;
import hospital.tenantservice.dto.tenantfeature.TenantFeatureResponseDto;
import hospital.tenantservice.exception.tenantfeature.DuplicateTenantFeatureException;
import hospital.tenantservice.exception.tenantfeature.TenantFeatureNotFoundException;
import hospital.tenantservice.mapper.TenantFeatureMapper;
import hospital.tenantservice.model.TenantFeature;
import hospital.tenantservice.repository.TenantFeatureRepository;
import hospital.tenantservice.service.impl.TenantFeatureServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link TenantFeatureServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class TenantFeatureServiceImplTest {

    @Mock private TenantFeatureRepository tenantFeatureRepository;
    @Mock private TenantFeatureMapper tenantFeatureMapper;

    @InjectMocks
    private TenantFeatureServiceImpl tenantFeatureService;

    private TenantFeature testFeature;

    @BeforeEach
    void setUp() {
        testFeature = TenantFeature.builder()
                .id(1L)
                .tenantId(1L)
                .featureCode("MODULE_BILLING")
                .featureName("Billing Module")
                .description("Access to billing and invoicing")
                .isActive(true)
                .isPlanFeature(true)
                .build();
    }

    @Nested
    @DisplayName("Add Feature")
    class AddFeatureTests {

        @Test
        @DisplayName("should add feature successfully")
        void shouldAddFeature() {
            TenantFeatureCreateDto dto = TenantFeatureCreateDto.builder()
                    .tenantId(1L)
                    .featureCode("MODULE_BILLING")
                    .featureName("Billing Module")
                    .build();

            when(tenantFeatureRepository.existsByTenantIdAndFeatureCode(1L, "MODULE_BILLING")).thenReturn(false);
            when(tenantFeatureMapper.toEntity(any(TenantFeatureCreateDto.class))).thenReturn(testFeature);
            when(tenantFeatureRepository.save(any(TenantFeature.class))).thenReturn(testFeature);
            when(tenantFeatureMapper.toResponseDto(any(TenantFeature.class)))
                    .thenReturn(TenantFeatureResponseDto.builder().id(1L).build());

            TenantFeatureResponseDto result = tenantFeatureService.addFeature(dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(tenantFeatureRepository).save(any(TenantFeature.class));
        }

        @Test
        @DisplayName("should throw when feature already exists")
        void shouldThrowWhenFeatureExists() {
            TenantFeatureCreateDto dto = TenantFeatureCreateDto.builder()
                    .tenantId(1L)
                    .featureCode("MODULE_BILLING")
                    .featureName("Billing Module")
                    .build();

            when(tenantFeatureRepository.existsByTenantIdAndFeatureCode(1L, "MODULE_BILLING")).thenReturn(true);

            assertThatThrownBy(() -> tenantFeatureService.addFeature(dto))
                    .isInstanceOf(DuplicateTenantFeatureException.class);
        }
    }

    @Nested
    @DisplayName("Read Feature")
    class ReadFeatureTests {

        @Test
        @DisplayName("should get feature by id")
        void shouldGetById() {
            TenantFeatureResponseDto expected = TenantFeatureResponseDto.builder().id(1L).build();

            when(tenantFeatureRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testFeature));
            when(tenantFeatureMapper.toResponseDto(testFeature)).thenReturn(expected);

            TenantFeatureResponseDto result = tenantFeatureService.getFeatureById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found by id")
        void shouldThrowWhenNotFoundById() {
            when(tenantFeatureRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> tenantFeatureService.getFeatureById(999L))
                    .isInstanceOf(TenantFeatureNotFoundException.class);
        }

        @Test
        @DisplayName("should get feature by code")
        void shouldGetByCode() {
            when(tenantFeatureRepository.findByTenantIdAndFeatureCode(1L, "MODULE_BILLING"))
                    .thenReturn(Optional.of(testFeature));
            when(tenantFeatureMapper.toResponseDto(testFeature))
                    .thenReturn(TenantFeatureResponseDto.builder().id(1L).featureCode("MODULE_BILLING").build());

            TenantFeatureResponseDto result = tenantFeatureService.getFeatureByCode(1L, "MODULE_BILLING");

            assertThat(result.getFeatureCode()).isEqualTo("MODULE_BILLING");
        }

        @Test
        @DisplayName("should get features by tenant")
        void shouldGetByTenant() {
            when(tenantFeatureRepository.findByTenantId(1L)).thenReturn(List.of(testFeature));
            when(tenantFeatureMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(TenantFeatureResponseDto.builder().id(1L).build()));

            assertThat(tenantFeatureService.getFeaturesByTenant(1L)).hasSize(1);
        }

        @Test
        @DisplayName("should get active features by tenant")
        void shouldGetActiveByTenant() {
            when(tenantFeatureRepository.findByTenantIdAndIsActive(1L, true)).thenReturn(List.of(testFeature));
            when(tenantFeatureMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(TenantFeatureResponseDto.builder().id(1L).build()));

            assertThat(tenantFeatureService.getActiveFeaturesByTenant(1L)).hasSize(1);
        }

        @Test
        @DisplayName("should get plan features")
        void shouldGetPlanFeatures() {
            when(tenantFeatureRepository.findByTenantIdAndIsPlanFeature(1L, true)).thenReturn(List.of(testFeature));
            when(tenantFeatureMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(TenantFeatureResponseDto.builder().id(1L).build()));

            assertThat(tenantFeatureService.getPlanFeatures(1L)).hasSize(1);
        }

        @Test
        @DisplayName("should get add-on features")
        void shouldGetAddOnFeatures() {
            when(tenantFeatureRepository.findByTenantIdAndIsPlanFeatureFalse(1L)).thenReturn(List.of(testFeature));
            when(tenantFeatureMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(TenantFeatureResponseDto.builder().id(1L).build()));

            assertThat(tenantFeatureService.getAddOnFeatures(1L)).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Enable/Disable Feature")
    class EnableDisableTests {

        @Test
        @DisplayName("should enable feature")
        void shouldEnableFeature() {
            testFeature.setIsActive(false);
            when(tenantFeatureRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testFeature));
            when(tenantFeatureRepository.save(any(TenantFeature.class))).thenReturn(testFeature);
            when(tenantFeatureMapper.toResponseDto(any(TenantFeature.class)))
                    .thenReturn(TenantFeatureResponseDto.builder().id(1L).build());

            tenantFeatureService.enableFeature(1L);

            verify(tenantFeatureRepository).save(argThat(f -> f.getIsActive()));
        }

        @Test
        @DisplayName("should disable feature")
        void shouldDisableFeature() {
            when(tenantFeatureRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testFeature));
            when(tenantFeatureRepository.save(any(TenantFeature.class))).thenReturn(testFeature);
            when(tenantFeatureMapper.toResponseDto(any(TenantFeature.class)))
                    .thenReturn(TenantFeatureResponseDto.builder().id(1L).build());

            tenantFeatureService.disableFeature(1L);

            verify(tenantFeatureRepository).save(argThat(f -> !f.getIsActive()));
        }

        @Test
        @DisplayName("should throw when enabling non-existent feature")
        void shouldThrowWhenEnablingNonExistent() {
            when(tenantFeatureRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> tenantFeatureService.enableFeature(999L))
                    .isInstanceOf(TenantFeatureNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Delete Feature")
    class DeleteFeatureTests {

        @Test
        @DisplayName("should soft delete feature")
        void shouldSoftDelete() {
            when(tenantFeatureRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testFeature));

            tenantFeatureService.deleteFeature(1L);

            verify(tenantFeatureRepository).save(argThat(TenantFeature::isDeleted));
        }
    }

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should check if tenant has feature")
        void shouldCheckIfTenantHasFeature() {
            when(tenantFeatureRepository.existsByTenantIdAndFeatureCode(1L, "MODULE_BILLING")).thenReturn(true);

            assertThat(tenantFeatureService.tenantHasFeature(1L, "MODULE_BILLING")).isTrue();
        }

        @Test
        @DisplayName("should count features by tenant")
        void shouldCountByTenant() {
            when(tenantFeatureRepository.countByTenantId(1L)).thenReturn(5L);

            assertThat(tenantFeatureService.countFeaturesByTenant(1L)).isEqualTo(5L);
        }
    }
}
