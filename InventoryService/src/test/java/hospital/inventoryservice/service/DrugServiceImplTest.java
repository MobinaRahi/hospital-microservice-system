package hospital.inventoryservice.service;

import hospital.inventoryservice.dto.drug.DrugCreateDto;
import hospital.inventoryservice.dto.drug.DrugResponseDto;
import hospital.inventoryservice.dto.drug.DrugUpdateDto;
import hospital.inventoryservice.exception.drug.DrugNotFoundException;
import hospital.inventoryservice.exception.drug.DuplicateDrugBarcodeException;
import hospital.inventoryservice.exception.drugcategory.DrugCategoryNotFoundException;
import hospital.inventoryservice.mapper.DrugMapper;
import hospital.inventoryservice.model.Drug;
import hospital.inventoryservice.model.DrugCategory;
import hospital.inventoryservice.model.enums.DrugForm;
import hospital.inventoryservice.repository.DrugCategoryRepository;
import hospital.inventoryservice.repository.DrugRepository;
import hospital.inventoryservice.service.impl.DrugServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
 * Unit tests for {@link DrugServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class DrugServiceImplTest {

    @Mock private DrugRepository drugRepository;
    @Mock private DrugCategoryRepository categoryRepository;
    @Mock private DrugMapper drugMapper;

    @InjectMocks
    private DrugServiceImpl drugService;

    private DrugCategory testCategory;
    private Drug testDrug;
    private DrugCreateDto createDto;
    private DrugResponseDto responseDto;

    @BeforeEach
    void setUp() {
        testCategory = DrugCategory.builder().id(100L).name("Antibiotics").build();

        testDrug = Drug.builder()
                .id(1L)
                .genericName("Amoxicillin")
                .brandName("Amoxil")
                .form(DrugForm.CAPSULE)
                .category(testCategory)
                .price(BigDecimal.valueOf(15.50))
                .barcode("1234567890")
                .isActive(true)
                .build();

        createDto = DrugCreateDto.builder()
                .genericName("Amoxicillin")
                .brandName("Amoxil")
                .form(DrugForm.CAPSULE)
                .categoryId(100L)
                .price(BigDecimal.valueOf(15.50))
                .barcode("1234567890")
                .build();

        responseDto = DrugResponseDto.builder()
                .id(1L)
                .genericName("Amoxicillin")
                .brandName("Amoxil")
                .form(DrugForm.CAPSULE)
                .price(BigDecimal.valueOf(15.50))
                .barcode("1234567890")
                .isActive(true)
                .build();
    }

    // ════════════════════════════════════════════════════════════════════
    // Create
    // ════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Create Drug")
    class CreateDrugTests {

        @Test
        @DisplayName("should create drug successfully when all data is valid")
        void shouldCreateDrugSuccessfully() {
            // Given
            when(drugRepository.existsByBarcode("1234567890")).thenReturn(false);
            when(categoryRepository.findById(100L)).thenReturn(Optional.of(testCategory));
            when(drugMapper.toEntity(any(DrugCreateDto.class))).thenReturn(testDrug);
            when(drugMapper.toResponseDto(any(Drug.class))).thenReturn(responseDto);
            when(drugRepository.save(any(Drug.class))).thenReturn(testDrug);

            // When
            DrugResponseDto result = drugService.createDrug(createDto);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getGenericName()).isEqualTo("Amoxicillin");
            assertThat(result.getBarcode()).isEqualTo("1234567890");

            verify(drugRepository).save(any(Drug.class));
            verify(drugMapper).toResponseDto(any(Drug.class));
        }

        @Test
        @DisplayName("should throw DuplicateDrugBarcodeException when barcode exists")
        void shouldThrowWhenBarcodeExists() {
            // Given
            when(drugRepository.existsByBarcode("1234567890")).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> drugService.createDrug(createDto))
                    .isInstanceOf(DuplicateDrugBarcodeException.class);

            verify(drugRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw DrugCategoryNotFoundException when category doesn't exist")
        void shouldThrowWhenCategoryNotFound() {
            // Given
            when(drugRepository.existsByBarcode("1234567890")).thenReturn(false);
            when(categoryRepository.findById(100L)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> drugService.createDrug(createDto))
                    .isInstanceOf(DrugCategoryNotFoundException.class);

            verify(drugRepository, never()).save(any());
        }

        @Test
        @DisplayName("should set category from categoryId")
        void shouldSetCategoryFromCategoryId() {
            // Given
            when(drugRepository.existsByBarcode("1234567890")).thenReturn(false);
            when(categoryRepository.findById(100L)).thenReturn(Optional.of(testCategory));
            when(drugMapper.toEntity(any(DrugCreateDto.class))).thenReturn(testDrug);
            when(drugMapper.toResponseDto(any(Drug.class))).thenReturn(responseDto);
            when(drugRepository.save(any(Drug.class))).thenReturn(testDrug);

            // When
            drugService.createDrug(createDto);

            // Then
            verify(drugMapper).toEntity(any(DrugCreateDto.class));
            verify(drugRepository).save(argThat(drug -> drug.getCategory() == testCategory));
        }
    }

    // ════════════════════════════════════════════════════════════════════
    // Read
    // ════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Read Drugs")
    class ReadDrugTests {

        @Test
        @DisplayName("should get drug by id successfully")
        void shouldGetDrugById() {
            // Given
            when(drugRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testDrug));
            when(drugMapper.toResponseDto(testDrug)).thenReturn(responseDto);

            // When
            DrugResponseDto result = drugService.getDrugById(1L);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw DrugNotFoundException when drug doesn't exist")
        void shouldThrowWhenDrugNotFound() {
            // Given
            when(drugRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> drugService.getDrugById(999L))
                    .isInstanceOf(DrugNotFoundException.class);
        }

        @Test
        @DisplayName("should get drug by barcode")
        void shouldGetDrugByBarcode() {
            // Given
            when(drugRepository.findByBarcode("1234567890")).thenReturn(Optional.of(testDrug));
            when(drugMapper.toResponseDto(testDrug)).thenReturn(responseDto);

            // When
            DrugResponseDto result = drugService.getDrugByBarcode("1234567890");

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getBarcode()).isEqualTo("1234567890");
        }

        @Test
        @DisplayName("should get all drugs")
        void shouldGetAllDrugs() {
            // Given
            when(drugRepository.findAllNotDeleted()).thenReturn(List.of(testDrug));
            when(drugMapper.toResponseDtoList(anyList())).thenReturn(List.of(responseDto));

            // When
            List<DrugResponseDto> result = drugService.getAllDrugs();

            // Then
            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("should search by generic name")
        void shouldSearchByGenericName() {
            // Given
            when(drugRepository.findByGenericNameContainingIgnoreCase("amox"))
                    .thenReturn(List.of(testDrug));
            when(drugMapper.toResponseDtoList(anyList())).thenReturn(List.of(responseDto));

            // When
            List<DrugResponseDto> result = drugService.searchByGenericName("amox");

            // Then
            assertThat(result).hasSize(1);
            verify(drugRepository).findByGenericNameContainingIgnoreCase("amox");
        }
    }

    // ════════════════════════════════════════════════════════════════════
    // Update
    // ════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Update Drug")
    class UpdateDrugTests {

        @Test
        @DisplayName("should update drug successfully")
        void shouldUpdateDrug() {
            // Given
            DrugUpdateDto updateDto = DrugUpdateDto.builder()
                    .price(BigDecimal.valueOf(20.00))
                    .build();

            when(drugRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testDrug));
            when(drugMapper.toResponseDto(any(Drug.class))).thenReturn(responseDto);
            when(drugRepository.save(any(Drug.class))).thenReturn(testDrug);

            // When
            DrugResponseDto result = drugService.updateDrug(1L, updateDto);

            // Then
            assertThat(result).isNotNull();
            verify(drugMapper).updateEntity(eq(updateDto), eq(testDrug));
            verify(drugRepository).save(testDrug);
        }

        @Test
        @DisplayName("should toggle active status")
        void shouldToggleActive() {
            // Given
            testDrug.setIsActive(true);
            when(drugRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testDrug));
            when(drugMapper.toResponseDto(any(Drug.class))).thenReturn(responseDto);
            when(drugRepository.save(any(Drug.class))).thenReturn(testDrug);

            // When
            drugService.toggleActive(1L);

            // Then
            verify(drugRepository).save(argThat(drug -> !drug.getIsActive()));
        }
    }

    // ════════════════════════════════════════════════════════════════════
    // Delete
    // ════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Delete Drug")
    class DeleteDrugTests {

        @Test
        @DisplayName("should soft-delete drug")
        void shouldSoftDelete() {
            // Given
            when(drugRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testDrug));

            // When
            drugService.deleteDrug(1L);

            // Then
            verify(drugRepository).save(argThat(Drug::isDeleted));
        }

        @Test
        @DisplayName("should throw when drug not found for delete")
        void shouldThrowWhenNotFound() {
            // Given
            when(drugRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> drugService.deleteDrug(999L))
                    .isInstanceOf(DrugNotFoundException.class);
        }
    }

    // ════════════════════════════════════════════════════════════════════
    // Alerts
    // ════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Drug Alerts")
    class AlertTests {

        @Test
        @DisplayName("should get drugs with low stock")
        void shouldGetDrugsWithLowStock() {
            when(drugRepository.findDrugsWithLowStock()).thenReturn(List.of(testDrug));
            when(drugMapper.toResponseDtoList(anyList())).thenReturn(List.of(responseDto));

            List<DrugResponseDto> result = drugService.getDrugsWithLowStock();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("should get prescription-only drugs")
        void shouldGetPrescriptionDrugs() {
            when(drugRepository.findByRequiresPrescriptionTrue()).thenReturn(List.of(testDrug));
            when(drugMapper.toResponseDtoList(anyList())).thenReturn(List.of(responseDto));

            List<DrugResponseDto> result = drugService.getPrescriptionDrugs();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("should check barcode existence")
        void shouldCheckBarcodeExistence() {
            when(drugRepository.existsByBarcode("123")).thenReturn(true);

            assertThat(drugService.barcodeExists("123")).isTrue();
        }
    }
}
