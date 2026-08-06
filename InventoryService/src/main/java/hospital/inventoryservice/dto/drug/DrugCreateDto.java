package hospital.inventoryservice.dto.drug;

import hospital.inventoryservice.model.enums.DrugForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for creating a new drug.
 * Used in POST /api/v1/inventory/drugs
 *
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *   <li>{@code genericName} is required and max 200 characters</li>
 *   <li>{@code form} is required (enum value)</li>
 *   <li>{@code categoryId} is required (must reference existing category)</li>
 *   <li>{@code barcode} must be unique across all drugs</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrugCreateDto {

    /**
     * Generic (scientific) name of the drug.
     * Required. Max 200 characters.
     */
    @NotBlank(message = "Generic name is required")
    @Size(max = 200, message = "Generic name must be at most 200 characters")
    private String genericName;

    /**
     * Brand/trade name of the drug.
     * Optional. Max 200 characters.
     */
    @Size(max = 200, message = "Brand name must be at most 200 characters")
    private String brandName;

    /**
     * Drug strength (e.g., "500mg", "10ml").
     * Optional. Max 50 characters.
     */
    @Size(max = 50, message = "Strength must be at most 50 characters")
    private String strength;

    /**
     * Pharmaceutical form (tablet, capsule, syrup, ...).
     * Required.
     */
    @NotNull(message = "Drug form is required")
    private DrugForm form;

    /**
     * ID of the drug category.
     * Required. Must reference an existing category.
     */
    @NotNull(message = "Category ID is required")
    private Long categoryId;

    /**
     * Price per unit in local currency.
     * Optional. Must be positive.
     */
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    /**
     * Whether this drug requires a doctor's prescription.
     * Optional. Defaults to false.
     */
    private Boolean requiresPrescription;

    /**
     * Unique barcode for the drug.
     * Optional but must be unique if provided. Max 50 characters.
     */
    @Size(max = 50, message = "Barcode must be at most 50 characters")
    private String barcode;

    /**
     * Description and usage instructions.
     * Optional. Max 1000 characters.
     */
    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;
}
