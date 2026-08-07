package hospital.inventoryservice.controller;

import hospital.inventoryservice.dto.drugcategory.DrugCategoryCreateDto;
import hospital.inventoryservice.dto.drugcategory.DrugCategoryResponseDto;
import hospital.inventoryservice.dto.drugcategory.DrugCategoryUpdateDto;
import hospital.inventoryservice.dto.response.ApiResponse;
import hospital.inventoryservice.service.DrugCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for DrugCategory management.
 * Provides CRUD operations for the hierarchical drug category tree.
 *
 * <p><strong>Hierarchy:</strong></p>
 * <pre>
 * Medications (level=1)
 *   └── Antibiotics (level=2)
 *         └── Penicillins (level=3)
 * </pre>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/inventory/drug-categories")
@RequiredArgsConstructor
@Tag(name = "Drug Category Management", description = "Hierarchical drug category CRUD APIs")
public class DrugCategoryApi {

    // ==================== Dependencies ====================

    private final DrugCategoryService categoryService;

    // ==================== Create ====================

    /**
     * Creates a new drug category.
     *
     * <p><strong>Required fields:</strong> name</p>
     * <p><strong>Optional fields:</strong> code, description, parentId</p>
     *
     * <p><strong>Validations:</strong></p>
     * <ul>
     *   <li>Name must be unique within the same parent level</li>
     *   <li>Parent must exist if parentId is provided</li>
     *   <li>Circular references are prevented</li>
     * </ul>
     */
    @PostMapping
    @Operation(summary = "Create a new drug category")
    public ResponseEntity<ApiResponse<DrugCategoryResponseDto>> createCategory(
            @Valid @RequestBody DrugCategoryCreateDto createDto) {
        DrugCategoryResponseDto created = categoryService.createCategory(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Category created successfully", HttpStatus.CREATED.value()));
    }

    // ==================== Update ====================

    /**
     * Updates an existing drug category.
     * <p>Only provided fields will be updated.</p>
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a drug category by ID")
    public ResponseEntity<ApiResponse<DrugCategoryResponseDto>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody DrugCategoryUpdateDto updateDto) {
        DrugCategoryResponseDto updated = categoryService.updateCategory(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Category updated successfully", HttpStatus.OK.value()));
    }

    /**
     * Toggles the active status of a category.
     * Inactive categories are hidden from selection lists.
     */
    @PatchMapping("/{id}/toggle-active")
    @Operation(summary = "Toggle category active status")
    public ResponseEntity<ApiResponse<DrugCategoryResponseDto>> toggleActive(@PathVariable Long id) {
        DrugCategoryResponseDto toggled = categoryService.toggleActive(id);
        return ResponseEntity.ok(ApiResponse.success(toggled, "Category status toggled successfully", HttpStatus.OK.value()));
    }

    // ==================== Read ====================

    /**
     * Gets a category by its ID, including parent and children.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get drug category by ID")
    public ResponseEntity<ApiResponse<DrugCategoryResponseDto>> getCategoryById(@PathVariable Long id) {
        DrugCategoryResponseDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success(category, "Category retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets root categories (level 1, no parent).
     * Used for building the category tree in the UI.
     */
    @GetMapping("/root")
    @Operation(summary = "Get root categories")
    public ResponseEntity<ApiResponse<List<DrugCategoryResponseDto>>> getRootCategories() {
        List<DrugCategoryResponseDto> categories = categoryService.getRootCategories();
        return ResponseEntity.ok(ApiResponse.success(categories, "Root categories retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets child categories of a specific parent.
     * Used for lazy-loading the tree in the UI.
     */
    @GetMapping("/{parentId}/children")
    @Operation(summary = "Get child categories of a parent")
    public ResponseEntity<ApiResponse<List<DrugCategoryResponseDto>>> getChildrenCategories(@PathVariable Long parentId) {
        List<DrugCategoryResponseDto> categories = categoryService.getChildrenCategories(parentId);
        return ResponseEntity.ok(ApiResponse.success(categories, "Child categories retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets all categories.
     * Returns the full flat list of categories.
     */
    @GetMapping
    @Operation(summary = "Get all categories")
    public ResponseEntity<ApiResponse<List<DrugCategoryResponseDto>>> getAllCategories() {
        List<DrugCategoryResponseDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(categories, "Categories retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Searches categories by name (case-insensitive, partial match).
     */
    @GetMapping("/search")
    @Operation(summary = "Search categories by name")
    public ResponseEntity<ApiResponse<List<DrugCategoryResponseDto>>> searchCategories(@RequestParam String name) {
        List<DrugCategoryResponseDto> categories = categoryService.searchByName(name);
        return ResponseEntity.ok(ApiResponse.success(categories, "Categories retrieved successfully", HttpStatus.OK.value()));
    }

    // ==================== Delete ====================

    /**
     * Soft-deletes a drug category.
     * <p>Fails if the category has child categories or drugs.</p>
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a drug category")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", HttpStatus.OK.value()));
    }
}
