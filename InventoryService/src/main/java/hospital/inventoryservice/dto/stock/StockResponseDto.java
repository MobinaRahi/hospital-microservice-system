package hospital.inventoryservice.dto.stock;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.inventoryservice.dto.drug.DrugResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for returning stock data in API responses.
 * Used in GET endpoints and as nested DTO in other responses.
 *
 * <p><strong>Includes:</strong></p>
 * <ul>
 *   <li>Full stock details</li>
 *   <li>Drug information (nested)</li>
 *   <li>Computed fields: isExpired, isLowStock</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockResponseDto {

    /**
     * Unique ID of the stock record.
     */
    private Long id;

    /**
     * Drug information (nested).
     */
    private DrugResponseDto drug;

    /**
     * Batch/lot number from the manufacturer.
     */
    private String batchNumber;

    /**
     * Current quantity in stock.
     */
    private Integer quantity;

    /**
     * Minimum stock level — triggers reorder alert.
     */
    private Integer minStockLevel;

    /**
     * Maximum stock level — for capacity planning.
     */
    private Integer maxStockLevel;

    /**
     * Physical storage location.
     */
    private String location;

    /**
     * Expiry date of this batch.
     */
    private LocalDate expiryDate;

    /**
     * When this stock was last restocked.
     */
    private LocalDateTime lastRestockedAt;

    /**
     * ID of the user who last restocked this batch.
     */
    private Long lastRestockedBy;

    /**
     * Whether this stock batch is expired.
     * Computed field based on expiryDate.
     */
    private Boolean isExpired;

    /**
     * Whether this stock is running low.
     * Computed field based on quantity vs minStockLevel.
     */
    private Boolean isLowStock;
}
