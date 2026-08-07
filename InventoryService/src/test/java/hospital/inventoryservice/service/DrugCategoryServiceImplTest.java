package hospital.inventoryservice.service;

import hospital.inventoryservice.dto.drugcategory.DrugCategoryCreateDto;
import hospital.inventoryservice.dto.drugcategory.DrugCategoryResponseDto;
import hospital.inventoryservice.exception.drugcategory.CategoryHasChildrenException;
import hospital.inventoryservice.exception.drugcategory.DrugCategoryNotFoundException;
import hospital.inventoryservice.exception.drugcategory.DuplicateDrugCategoryNameException;
import hospital.inventoryservice.mapper.DrugCategoryMapper;
import hospital.inventoryservice.model.DrugCategory;
import hospital.inventoryservice.repository.DrugCategoryRepository;
import hospital.inventoryservice.service.impl.DrugCategoryServiceImpl;
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
 * Unit tests for {@link DrugCategoryServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class DrugCategoryServiceImplTest {

    @Mock private DrugCategoryRepository categoryRepository;
    @Mock private DrugCategoryMapper categoryMapper;

    @InjectMocks
    private DrugCategoryServiceImpl categoryService;

    private DrugCategory rootCategory;
    private DrugCategory childCategory;

    @BeforeEach
    void setUp() {
        rootCategory = DrugCategory.builder()
                .id(1L)
                .name("Medications")
                .level(1)
                .isActive(true)
                .build();

        childCategory = DrugCategory.builder()
                .id(2L)
                .name("Antibiotics")
                .level(2)
                .parent(rootCategory)
                .isActive(true)
                .build();
    }

    @Nested
    @DisplayName("Create Category")
    class CreateCategoryTests {

        @Test
        @DisplayName("should create root category")
        void shouldCreateRootCategory() {
            DrugCategoryCreateDto dto = DrugCategoryCreateDto.builder()
                    .name("Medications")
                    .build();

            DrugCategoryResponseDto expected = DrugCategoryResponseDto.builder()
                    .id(1L).name("Medications").level(1).build();

            when(categoryRepository.existsByNameAndParentId("Medications", null)).thenReturn(false);
            when(categoryMapper.toEntity(any(DrugCategoryCreateDto.class))).thenReturn(rootCategory);
            when(categoryMapper.toResponseDto(any(DrugCategory.class))).thenReturn(expected);
            when(categoryRepository.save(any(DrugCategory.class))).thenReturn(rootCategory);

            DrugCategoryResponseDto result = categoryService.createCategory(dto);

            assertThat(result.getLevel()).isEqualTo(1);
            verify(categoryRepository).save(argThat(c -> c.getParent() == null));
        }

        @Test
        @DisplayName("should create child category")
        void shouldCreateChildCategory() {
            DrugCategoryCreateDto dto = DrugCategoryCreateDto.builder()
                    .name("Antibiotics")
                    .parentId(1L)
                    .build();

            DrugCategoryResponseDto expected = DrugCategoryResponseDto.builder()
                    .id(2L).name("Antibiotics").level(2).build();

            when(categoryRepository.existsByNameAndParentId("Antibiotics", 1L)).thenReturn(false);
            when(categoryRepository.findById(1L)).thenReturn(Optional.of(rootCategory));
            when(categoryMapper.toEntity(any(DrugCategoryCreateDto.class))).thenReturn(childCategory);
            when(categoryMapper.toResponseDto(any(DrugCategory.class))).thenReturn(expected);
            when(categoryRepository.save(any(DrugCategory.class))).thenReturn(childCategory);

            DrugCategoryResponseDto result = categoryService.createCategory(dto);

            assertThat(result.getLevel()).isEqualTo(2);
        }

        @Test
        @DisplayName("should throw when name exists at same level")
        void shouldThrowWhenNameExists() {
            DrugCategoryCreateDto dto = DrugCategoryCreateDto.builder()
                    .name("Medications")
                    .build();

            when(categoryRepository.existsByNameAndParentId("Medications", null)).thenReturn(true);

            assertThatThrownBy(() -> categoryService.createCategory(dto))
                    .isInstanceOf(DuplicateDrugCategoryNameException.class);
        }

        @Test
        @DisplayName("should throw when parent doesn't exist")
        void shouldThrowWhenParentNotFound() {
            DrugCategoryCreateDto dto = DrugCategoryCreateDto.builder()
                    .name("Antibiotics")
                    .parentId(999L)
                    .build();

            when(categoryRepository.existsByNameAndParentId("Antibiotics", 999L)).thenReturn(false);
            when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> categoryService.createCategory(dto))
                    .isInstanceOf(DrugCategoryNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Read Categories")
    class ReadCategoryTests {

        @Test
        @DisplayName("should get category by id")
        void shouldGetById() {
            DrugCategoryResponseDto expected = DrugCategoryResponseDto.builder()
                    .id(1L).name("Medications").build();

            when(categoryRepository.findNotDeletedById(1L)).thenReturn(Optional.of(rootCategory));
            when(categoryMapper.toResponseDto(rootCategory)).thenReturn(expected);

            DrugCategoryResponseDto result = categoryService.getCategoryById(1L);

            assertThat(result.getName()).isEqualTo("Medications");
        }

        @Test
        @DisplayName("should get root categories")
        void shouldGetRootCategories() {
            when(categoryRepository.findByParentIsNull()).thenReturn(List.of(rootCategory));
            when(categoryMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    DrugCategoryResponseDto.builder().level(1).build()));

            List<DrugCategoryResponseDto> result = categoryService.getRootCategories();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("should get children categories")
        void shouldGetChildren() {
            when(categoryRepository.findByParentId(1L)).thenReturn(List.of(childCategory));
            when(categoryMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    DrugCategoryResponseDto.builder().level(2).build()));

            List<DrugCategoryResponseDto> result = categoryService.getChildrenCategories(1L);

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("should search by name")
        void shouldSearchByName() {
            when(categoryRepository.findByNameContainingIgnoreCase("med"))
                    .thenReturn(List.of(rootCategory));
            when(categoryMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    DrugCategoryResponseDto.builder().build()));

            List<DrugCategoryResponseDto> result = categoryService.searchByName("med");

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Delete Category")
    class DeleteCategoryTests {

        @Test
        @DisplayName("should soft-delete category without children")
        void shouldDeleteWithoutChildren() {
            when(categoryRepository.findNotDeletedById(1L)).thenReturn(Optional.of(rootCategory));
            when(categoryRepository.findByParentId(1L)).thenReturn(List.of());

            categoryService.deleteCategory(1L);

            verify(categoryRepository).save(argThat(DrugCategory::isDeleted));
        }

        @Test
        @DisplayName("should throw when category has children")
        void shouldThrowWhenHasChildren() {
            when(categoryRepository.findNotDeletedById(1L)).thenReturn(Optional.of(rootCategory));
            when(categoryRepository.findByParentId(1L)).thenReturn(List.of(childCategory));

            assertThatThrownBy(() -> categoryService.deleteCategory(1L))
                    .isInstanceOf(CategoryHasChildrenException.class);
        }

        @Test
        @DisplayName("should throw when category not found")
        void shouldThrowWhenNotFound() {
            when(categoryRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> categoryService.deleteCategory(999L))
                    .isInstanceOf(DrugCategoryNotFoundException.class);
        }
    }
}
