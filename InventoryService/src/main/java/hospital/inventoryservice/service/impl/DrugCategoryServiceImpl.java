package hospital.inventoryservice.service.impl;

import hospital.inventoryservice.exception.drugcategory.DuplicateDrugCategoryNameException;
import hospital.inventoryservice.dto.drugcategory.DrugCategoryCreateDto;
import hospital.inventoryservice.dto.drugcategory.DrugCategoryResponseDto;
import hospital.inventoryservice.dto.drugcategory.DrugCategoryUpdateDto;


import hospital.inventoryservice.mapper.DrugCategoryMapper;
import hospital.inventoryservice.exception.drugcategory.DrugCategoryNotFoundException;
import hospital.inventoryservice.exception.drugcategory.DuplicateDrugCategoryNameException;
import hospital.inventoryservice.exception.drugcategory.CategoryHasChildrenException;
import hospital.inventoryservice.model.DrugCategory;
import hospital.inventoryservice.repository.DrugCategoryRepository;
import hospital.inventoryservice.service.DrugCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link DrugCategoryService}.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Categories form a tree structure (parent-child)</li>
 *   <li>A category cannot be its own parent (no circular references)</li>
 *   <li>A category cannot be deleted if it has drugs or children</li>
 *   <li>Category name must be unique within the same parent level</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DrugCategoryServiceImpl implements DrugCategoryService {

    private final DrugCategoryRepository categoryRepository;
    private final DrugCategoryMapper categoryMapper;

    // ════════════════════════════════════════════════════════════════════
    // Create
    // ════════════════════════════════════════════════════════════════════

    @Override
    public DrugCategoryResponseDto createCategory(DrugCategoryCreateDto dto) {
        log.info("Creating drug category: {}", dto.getName());

        // Validate name uniqueness within parent level
        if (nameExists(dto.getName(), dto.getParentId())) {
            throw new DuplicateDrugCategoryNameException(dto.getName());
        }

        // Map DTO to entity
        DrugCategory category = categoryMapper.toEntity(dto);

        // Set parent if provided
        if (dto.getParentId() != null) {
            DrugCategory parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> DrugCategoryNotFoundException.byId(dto.getParentId()));

            category.setParent(parent);
            category.setLevel(parent.getLevel() + 1);
        } else {
            category.setLevel(1);
        }

        // Save and return
        DrugCategory saved = categoryRepository.save(category);
        log.info("Category created with id: {}", saved.getId());

        return categoryMapper.toResponseDto(saved);
    }

    // ════════════════════════════════════════════════════════════════════
    // Read
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public DrugCategoryResponseDto getCategoryById(Long id) {
        log.debug("Fetching category by id: {}", id);

        DrugCategory category = categoryRepository.findNotDeletedById(id)
                .orElseThrow(() -> DrugCategoryNotFoundException.byId(id));

        return categoryMapper.toResponseDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DrugCategoryResponseDto> getRootCategories() {
        log.debug("Fetching root categories");

        List<DrugCategory> categories = categoryRepository.findByParentIsNull();
        return categoryMapper.toResponseDtoList(categories);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DrugCategoryResponseDto> getChildrenCategories(Long parentId) {
        log.debug("Fetching children of category: {}", parentId);

        List<DrugCategory> categories = categoryRepository.findByParentId(parentId);
        return categoryMapper.toResponseDtoList(categories);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DrugCategoryResponseDto> searchByName(String name) {
        log.debug("Searching categories by name: {}", name);

        List<DrugCategory> categories = categoryRepository.findByNameContainingIgnoreCase(name);
        return categoryMapper.toResponseDtoList(categories);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DrugCategoryResponseDto> getAllCategories() {
        log.debug("Fetching all categories");

        List<DrugCategory> categories = categoryRepository.findAllNotDeleted();
        return categoryMapper.toResponseDtoList(categories);
    }

    // ════════════════════════════════════════════════════════════════════
    // Update
    // ════════════════════════════════════════════════════════════════════

    @Override
    public DrugCategoryResponseDto updateCategory(Long id, DrugCategoryUpdateDto dto) {
        log.info("Updating category id: {}", id);

        DrugCategory category = categoryRepository.findNotDeletedById(id)
                .orElseThrow(() -> DrugCategoryNotFoundException.byId(id));

        // Validate name uniqueness if changed
        if (dto.getName() != null && !dto.getName().equals(category.getName())) {
            if (nameExists(dto.getName(), category.getParent() != null ? category.getParent().getId() : null)) {
                throw new DuplicateDrugCategoryNameException(dto.getName());
            }
        }

        // Map update DTO to entity
        categoryMapper.updateEntity(dto, category);

        DrugCategory saved = categoryRepository.save(category);
        log.info("Category updated id: {}", saved.getId());

        return categoryMapper.toResponseDto(saved);
    }

    @Override
    public DrugCategoryResponseDto toggleActive(Long id) {
        log.info("Toggling active status for category id: {}", id);

        DrugCategory category = categoryRepository.findNotDeletedById(id)
                .orElseThrow(() -> DrugCategoryNotFoundException.byId(id));

        category.setIsActive(!category.getIsActive());
        DrugCategory saved = categoryRepository.save(category);

        return categoryMapper.toResponseDto(saved);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Delete
    // ════════════════════════════════════════════════════════════════════

    @Override
    public void deleteCategory(Long id) {
        log.info("Soft-deleting category id: {}", id);

        DrugCategory category = categoryRepository.findNotDeletedById(id)
                .orElseThrow(() -> DrugCategoryNotFoundException.byId(id));

        // Check if category has children
        List<DrugCategory> children = categoryRepository.findByParentId(id);
        if (!children.isEmpty()) {
            throw new CategoryHasChildrenException(id);
        }

        category.softDelete(null);
        categoryRepository.save(category);
    }

    // ════════════════════════════════════════════════════════════════════
    // Validation
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public boolean nameExists(String name, Long parentId) {
        return categoryRepository.existsByNameAndParentId(name, parentId);
    }
}
