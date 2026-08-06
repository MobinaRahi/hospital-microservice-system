package hospital.inventoryservice.dto.drugcategory;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for returning drug category data in API responses.
 * Used in GET endpoints and as nested DTO in other responses.
 *
 * <p><strong>Includes:</strong></p>
 * <ul>
 *   <li>Full category details</li>
 *   <li>Parent category (if any)</li>
 *   <li>Child categories (nested list)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrugCategoryResponseDto {

    /**
     * Unique ID of the category.
     */
    private Long id;

    /**
     * Name of the category.
     */
    private String name;

    /**
     * Unique code for the category.
     */
    private String code;

    /**
     * Description of the category.
     */
    private String description;

    /**
     * Parent category (null for root categories).
     */
    private DrugCategoryResponseDto parent;

    /**
     * List of child categories.
     */
    private List<DrugCategoryResponseDto> children;

    /**
     * Level in the hierarchy (1 = root, 2 = child, ...).
     */
    private Integer level;

    /**
     * Whether this category is active.
     */
    private Boolean isActive;

    /**
     * When this category was created.
     */
    private LocalDateTime createdAt;
}
