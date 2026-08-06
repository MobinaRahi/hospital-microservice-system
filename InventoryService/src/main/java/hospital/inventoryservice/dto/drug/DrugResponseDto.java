package hospital.inventoryservice.dto.drug;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.inventoryservice.dto.drugcategory.DrugCategoryResponseDto;
import hospital.inventoryservice.model.enums.DrugForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for returning drug data in API responses.
 * Used in GET endpoints and as nested DTO in other responses.
 *
 * <p><strong>Includes:</strong></p>
 * <ul>
 *   <li>Full drug details</li>
 *   <li>Category information (nested)</li>
 *   <li>Audit timestamps</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrugResponseDto {

    /**
     * Unique ID of the drug.
     */
    private Long id;

    /**
     * Generic (scientific) name.
     */
    private String genericName;

    /**
     * Brand/trade name.
     */
    private String brandName;

    /**
     * Drug strength (e.g., "500mg").
     */
    private String strength;

    /**
     * Pharmaceutical form.
     */
    private DrugForm form;

    /**
     * Category information (nested).
     */
    private DrugCategoryResponseDto category;

    /**
     * Price per unit.
     */
    private BigDecimal price;

    /**
     * Whether this drug requires a prescription.
     */
    private Boolean requiresPrescription;

    /**
     * Unique barcode.
     */
    private String barcode;

    /**
     * Description and usage instructions.
     */
    private String description;

    /**
     * Whether this drug is currently active.
     */
    private Boolean isActive;

    /**
     * When this drug was created.
     */
    private LocalDateTime createdAt;

    /**
     * When this drug was last updated.
     */
    private LocalDateTime updatedAt;
}
