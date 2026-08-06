package hospital.inventoryservice.dto.drug;

import hospital.inventoryservice.model.enums.DrugForm;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for updating an existing drug.
 * Used in PUT /api/v1/inventory/drugs/{id}
 *
 * <p><strong>Rules:</strong></p>
 * <ul>
 *   <li>All fields are optional — only provided fields will be updated</li>
 *   <li>{@code genericName} should not be changed (create new drug instead)</li>
 *   <li>{@code categoryId} cannot be changed after creation</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrugUpdateDto {

    /**
     * Updated brand name.
     * Optional. Max 200 characters.
     */
    @Size(max = 200, message = "Brand name must be at most 200 characters")
    private String brandName;

    /**
     * Updated strength.
     * Optional. Max 50 characters.
     */
    @Size(max = 50, message = "Strength must be at most 50 characters")
    private String strength;

    /**
     * Updated pharmaceutical form.
     * Optional.
     */
    private DrugForm form;

    /**
     * Updated price.
     * Optional.
     */
    private BigDecimal price;

    /**
     * Updated prescription requirement flag.
     * Optional.
     */
    private Boolean requiresPrescription;

    /**
     * Updated barcode.
     * Optional. Must be unique if changed. Max 50 characters.
     */
    @Size(max = 50, message = "Barcode must be at most 50 characters")
    private String barcode;

    /**
     * Updated description.
     * Optional. Max 1000 characters.
     */
    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    /**
     * Whether this drug is currently active.
     * Optional. Used to activate/deactivate drugs.
     */
    private Boolean isActive;
}
