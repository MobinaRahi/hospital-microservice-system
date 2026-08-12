package hospital.billingservice.service;

import hospital.billingservice.dto.servicecatalog.ServiceCatalogCreateDto;
import hospital.billingservice.dto.servicecatalog.ServiceCatalogResponseDto;
import hospital.billingservice.exception.servicecatalog.DuplicateServiceCodeException;
import hospital.billingservice.exception.servicecatalog.ServiceCatalogNotFoundException;
import hospital.billingservice.mapper.ServiceCatalogMapper;
import hospital.billingservice.model.ServiceCatalog;
import hospital.billingservice.model.enums.ServiceCategory;
import hospital.billingservice.repository.ServiceCatalogRepository;
import hospital.billingservice.service.impl.ServiceCatalogServiceImpl;
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
 * Unit tests for {@link ServiceCatalogServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class ServiceCatalogServiceImplTest {

    @Mock private ServiceCatalogRepository serviceCatalogRepository;
    @Mock private ServiceCatalogMapper serviceCatalogMapper;

    @InjectMocks
    private ServiceCatalogServiceImpl serviceCatalogService;

    private ServiceCatalog testService;

    @BeforeEach
    void setUp() {
        testService = ServiceCatalog.builder()
                .id(1L)
                .code("DOC-001")
                .name("Doctor Visit")
                .category(ServiceCategory.DOCTOR_VISIT)
                .price(BigDecimal.valueOf(50000))
                .isActive(true)
                .build();
    }

    @Nested
    @DisplayName("Create Service")
    class CreateServiceTests {

        @Test
        @DisplayName("should create service successfully")
        void shouldCreateService() {
            ServiceCatalogCreateDto dto = ServiceCatalogCreateDto.builder()
                    .code("DOC-001").name("Doctor Visit").category(ServiceCategory.DOCTOR_VISIT)
                    .price(BigDecimal.valueOf(50000)).build();

            when(serviceCatalogRepository.existsByCode("DOC-001")).thenReturn(false);
            when(serviceCatalogMapper.toEntity(any(ServiceCatalogCreateDto.class))).thenReturn(testService);
            when(serviceCatalogMapper.toResponseDto(any(ServiceCatalog.class)))
                    .thenReturn(ServiceCatalogResponseDto.builder().id(1L).build());
            when(serviceCatalogRepository.save(any(ServiceCatalog.class))).thenReturn(testService);

            ServiceCatalogResponseDto result = serviceCatalogService.createService(dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(serviceCatalogRepository).save(argThat(s -> s.getIsActive()));
        }

        @Test
        @DisplayName("should throw when code exists")
        void shouldThrowWhenCodeExists() {
            ServiceCatalogCreateDto dto = ServiceCatalogCreateDto.builder().code("DOC-001").build();

            when(serviceCatalogRepository.existsByCode("DOC-001")).thenReturn(true);

            assertThatThrownBy(() -> serviceCatalogService.createService(dto))
                    .isInstanceOf(DuplicateServiceCodeException.class);
        }
    }

    @Nested
    @DisplayName("Read Service")
    class ReadServiceTests {

        @Test
        @DisplayName("should get service by id")
        void shouldGetById() {
            ServiceCatalogResponseDto expected = ServiceCatalogResponseDto.builder().id(1L).build();

            when(serviceCatalogRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testService));
            when(serviceCatalogMapper.toResponseDto(testService)).thenReturn(expected);

            ServiceCatalogResponseDto result = serviceCatalogService.getServiceById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(serviceCatalogRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> serviceCatalogService.getServiceById(999L))
                    .isInstanceOf(ServiceCatalogNotFoundException.class);
        }

        @Test
        @DisplayName("should get active services")
        void shouldGetActiveServices() {
            when(serviceCatalogRepository.findByIsActiveTrue()).thenReturn(List.of(testService));
            when(serviceCatalogMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    ServiceCatalogResponseDto.builder().id(1L).build()));

            assertThat(serviceCatalogService.getAllActiveServices()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Update Service")
    class UpdateServiceTests {

        @Test
        @DisplayName("should toggle active status")
        void shouldToggleActive() {
            testService.setIsActive(true);
            ServiceCatalogResponseDto expected = ServiceCatalogResponseDto.builder().build();

            when(serviceCatalogRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testService));
            when(serviceCatalogMapper.toResponseDto(any(ServiceCatalog.class))).thenReturn(expected);
            when(serviceCatalogRepository.save(any(ServiceCatalog.class))).thenReturn(testService);

            serviceCatalogService.toggleActive(1L);

            verify(serviceCatalogRepository).save(argThat(s -> !s.getIsActive()));
        }
    }

    @Nested
    @DisplayName("Delete Service")
    class DeleteServiceTests {

        @Test
        @DisplayName("should soft delete service")
        void shouldSoftDelete() {
            when(serviceCatalogRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testService));

            serviceCatalogService.deleteService(1L);

            verify(serviceCatalogRepository).save(argThat(ServiceCatalog::isDeleted));
        }
    }

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should check code existence")
        void shouldCheckCodeExistence() {
            when(serviceCatalogRepository.existsByCode("DOC-001")).thenReturn(true);

            assertThat(serviceCatalogService.codeExists("DOC-001")).isTrue();
        }
    }
}
