package hospital.inventoryservice.dto.drugcategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new drug category.
 * Used in POST /api/v1/inventory/drug-categories
 *
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *   <li>{@code name} is required and max 200 characters</li>
 *   <li>{@code parentId} is optional (null = root category)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrugCategoryCreateDto {

    /**
     * Name of the category.
     * Required. Max 200 characters.
     */
    @NotBlank(message = "Category name is required")
    @Size(max = 200, message = "Category name must be at most 200 characters")
    private String name;

    /**
     * Unique code for the category (e.g., "ANT-001").
     * Optional. Max 50 characters.
     */
    @Size(max = 50, message = "Category code must be at most 50 characters")
    private String code;

    /**
     * Description of the category.
     * Optional. Max 1000 characters.
     */
    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    /**
     * ID of the parent category.
     * Null for root categories.
     */
    private Long parentId;
}
