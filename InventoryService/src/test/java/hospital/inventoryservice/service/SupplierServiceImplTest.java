package hospital.inventoryservice.service;

import hospital.inventoryservice.dto.supplier.SupplierCreateDto;
import hospital.inventoryservice.dto.supplier.SupplierResponseDto;
import hospital.inventoryservice.dto.supplier.SupplierUpdateDto;
import hospital.inventoryservice.exception.supplier.DuplicateSupplierEmailException;
import hospital.inventoryservice.exception.supplier.SupplierNotFoundException;
import hospital.inventoryservice.mapper.SupplierMapper;
import hospital.inventoryservice.model.Supplier;
import hospital.inventoryservice.repository.SupplierRepository;
import hospital.inventoryservice.service.impl.SupplierServiceImpl;
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
 * Unit tests for {@link SupplierServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class SupplierServiceImplTest {

    @Mock private SupplierRepository supplierRepository;
    @Mock private SupplierMapper supplierMapper;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    private Supplier testSupplier;

    @BeforeEach
    void setUp() {
        testSupplier = Supplier.builder()
                .id(1L)
                .name("PharmaCorp")
                .email("info@pharmacorp.com")
                .phone("555-1234")
                .isActive(true)
                .build();
    }

    @Nested
    @DisplayName("Create Supplier")
    class CreateSupplierTests {

        @Test
        @DisplayName("should create supplier successfully")
        void shouldCreateSupplier() {
            SupplierCreateDto dto = SupplierCreateDto.builder()
                    .name("PharmaCorp")
                    .email("info@pharmacorp.com")
                    .build();

            SupplierResponseDto expected = SupplierResponseDto.builder()
                    .id(1L).name("PharmaCorp").isActive(true).build();

            when(supplierRepository.existsByEmail("info@pharmacorp.com")).thenReturn(false);
            when(supplierMapper.toEntity(any(SupplierCreateDto.class))).thenReturn(testSupplier);
            when(supplierMapper.toResponseDto(any(Supplier.class))).thenReturn(expected);
            when(supplierRepository.save(any(Supplier.class))).thenReturn(testSupplier);

            SupplierResponseDto result = supplierService.createSupplier(dto);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("PharmaCorp");
            verify(supplierRepository).save(argThat(s -> s.getIsActive()));
        }

        @Test
        @DisplayName("should throw when email already exists")
        void shouldThrowWhenEmailExists() {
            SupplierCreateDto dto = SupplierCreateDto.builder()
                    .name("NewCorp")
                    .email("info@pharmacorp.com")
                    .build();

            when(supplierRepository.existsByEmail("info@pharmacorp.com")).thenReturn(true);

            assertThatThrownBy(() -> supplierService.createSupplier(dto))
                    .isInstanceOf(DuplicateSupplierEmailException.class);
        }
    }

    @Nested
    @DisplayName("Read Suppliers")
    class ReadSupplierTests {

        @Test
        @DisplayName("should get supplier by id")
        void shouldGetById() {
            SupplierResponseDto expected = SupplierResponseDto.builder().id(1L).name("PharmaCorp").build();

            when(supplierRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testSupplier));
            when(supplierMapper.toResponseDto(testSupplier)).thenReturn(expected);

            SupplierResponseDto result = supplierService.getSupplierById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when supplier not found")
        void shouldThrowWhenNotFound() {
            when(supplierRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> supplierService.getSupplierById(999L))
                    .isInstanceOf(SupplierNotFoundException.class);
        }

        @Test
        @DisplayName("should get all active suppliers")
        void shouldGetActiveSuppliers() {
            when(supplierRepository.findByIsActiveTrue()).thenReturn(List.of(testSupplier));
            when(supplierMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    SupplierResponseDto.builder().id(1L).build()));

            List<SupplierResponseDto> result = supplierService.getAllActiveSuppliers();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("should search by name")
        void shouldSearchByName() {
            when(supplierRepository.findByNameContainingIgnoreCase("pharma"))
                    .thenReturn(List.of(testSupplier));
            when(supplierMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    SupplierResponseDto.builder().build()));

            List<SupplierResponseDto> result = supplierService.searchByName("pharma");

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Update Supplier")
    class UpdateSupplierTests {

        @Test
        @DisplayName("should update supplier")
        void shouldUpdate() {
            SupplierUpdateDto dto = SupplierUpdateDto.builder().name("UpdatedCorp").build();

            when(supplierRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testSupplier));
            when(supplierMapper.toResponseDto(any(Supplier.class))).thenReturn(
                    SupplierResponseDto.builder().name("UpdatedCorp").build());
            when(supplierRepository.save(any(Supplier.class))).thenReturn(testSupplier);

            supplierService.updateSupplier(1L, dto);

            verify(supplierMapper).updateEntity(eq(dto), eq(testSupplier));
        }

        @Test
        @DisplayName("should toggle active status")
        void shouldToggleActive() {
            when(supplierRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testSupplier));
            when(supplierMapper.toResponseDto(any(Supplier.class))).thenReturn(
                    SupplierResponseDto.builder().build());
            when(supplierRepository.save(any(Supplier.class))).thenReturn(testSupplier);

            supplierService.toggleActive(1L);

            verify(supplierRepository).save(argThat(s -> !s.getIsActive()));
        }
    }

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should check email existence")
        void shouldCheckEmailExistence() {
            when(supplierRepository.existsByEmail("test@test.com")).thenReturn(true);

            assertThat(supplierService.emailExists("test@test.com")).isTrue();
        }
    }
}
