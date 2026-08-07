package hospital.inventoryservice.service;

import hospital.inventoryservice.dto.drugcategory.DrugCategoryCreateDto;
import hospital.inventoryservice.dto.drugcategory.DrugCategoryResponseDto;
import hospital.inventoryservice.dto.drugcategory.DrugCategoryUpdateDto;

import java.util.List;

/**
 * Service interface for DrugCategory management.
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
public interface DrugCategoryService {

    // ════════════════════════════════════════════════════════════════════
    // Create
    // ════════════════════════════════════════════════════════════════════

    /**
     * Creates a new drug category.
     *
     * @param dto the category creation data
     * @return the created category
     */
    DrugCategoryResponseDto createCategory(DrugCategoryCreateDto dto);

    // ════════════════════════════════════════════════════════════════════
    // Read
    // ════════════════════════════════════════════════════════════════════

    /**
     * Gets a category by its ID.
     *
     * @param id the category ID
     * @return the category with its children
     */
    DrugCategoryResponseDto getCategoryById(Long id);

    /**
     * Gets all root categories (level 1, no parent).
     *
     * @return list of root categories
     */
    List<DrugCategoryResponseDto> getRootCategories();

    /**
     * Gets child categories of a parent.
     *
     * @param parentId the parent category ID
     * @return list of child categories
     */
    List<DrugCategoryResponseDto> getChildrenCategories(Long parentId);

    /**
     * Searches categories by name (case-insensitive, partial match).
     *
     * @param name the name to search
     * @return list of matching categories
     */
    List<DrugCategoryResponseDto> searchByName(String name);

    /**
     * Gets all active categories.
     *
     * @return list of all categories
     */
    List<DrugCategoryResponseDto> getAllCategories();

    // ════════════════════════════════════════════════════════════════════
    // Update
    // ════════════════════════════════════════════════════════════════════

    /**
     * Updates an existing category.
     * <p>Only provided fields in the DTO will be updated.</p>
     *
     * @param id  the category ID
     * @param dto the update data
     * @return the updated category
     */
    DrugCategoryResponseDto updateCategory(Long id, DrugCategoryUpdateDto dto);

    /**
     * Toggles the active status of a category.
     *
     * @param id the category ID
     * @return the updated category
     */
    DrugCategoryResponseDto toggleActive(Long id);

    // ════════════════════════════════════════════════════════════════════
    // Delete
    // ════════════════════════════════════════════════════════════════════

    /**
     * Soft-deletes a category.
     * <p>Fails if the category has drugs or children.</p>
     *
     * @param id the category ID
     */
    void deleteCategory(Long id);

    // ════════════════════════════════════════════════════════════════════
    // Validation
    // ════════════════════════════════════════════════════════════════════

    /**
     * Checks if a category name exists under a specific parent.
     *
     * @param name     the category name
     * @param parentId the parent ID (null for root)
     * @return true if exists
     */
    boolean nameExists(String name, Long parentId);
}
