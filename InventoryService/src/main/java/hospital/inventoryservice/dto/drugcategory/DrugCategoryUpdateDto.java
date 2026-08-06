package hospital.inventoryservice.dto.drugcategory;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing drug category.
 * Used in PUT /api/v1/inventory/drug-categories/{id}
 *
 * <p><strong>Rules:</strong></p>
 * <ul>
 *   <li>All fields are optional — only provided fields will be updated</li>
 *   <li>{@code parentId} cannot be changed after creation</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrugCategoryUpdateDto {

    /**
     * Updated name of the category.
     * Optional. Max 200 characters.
     */
    @Size(max = 200, message = "Category name must be at most 200 characters")
    private String name;

    /**
     * Updated description.
     * Optional. Max 1000 characters.
     */
    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    /**
     * Whether this category is active.
     * Optional. Used to activate/deactivate categories.
     */
    private Boolean isActive;
}
