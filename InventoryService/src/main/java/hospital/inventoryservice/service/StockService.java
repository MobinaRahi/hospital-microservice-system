package hospital.inventoryservice.service;

import hospital.inventoryservice.dto.stock.StockCreateDto;
import hospital.inventoryservice.dto.stock.StockResponseDto;
import hospital.inventoryservice.dto.stock.StockUpdateDto;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Stock management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each drug can have multiple stock batches</li>
 *   <li>Quantity cannot be negative</li>
 *   <li>Low stock alerts are triggered when quantity falls below minStockLevel</li>
 *   <li>Expired stock should not be dispensed</li>
 *   <li>Stock is updated when purchase orders are received</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface StockService {

    // ════════════════════════════════════════════════════════════════════
    // Create
    // ════════════════════════════════════════════════════════════════════

    /**
     * Creates a new stock batch.
     *
     * @param dto the stock creation data
     * @return the created stock
     */
    StockResponseDto createStock(StockCreateDto dto);

    // ════════════════════════════════════════════════════════════════════
    // Read
    // ════════════════════════════════════════════════════════════════════

    /**
     * Gets a stock record by its ID.
     *
     * @param id the stock ID
     * @return the stock record
     */
    StockResponseDto getStockById(Long id);

    /**
     * Gets all stock batches for a drug.
     *
     * @param drugId the drug ID
     * @return list of stock batches
     */
    List<StockResponseDto> getStocksByDrug(Long drugId);

    /**
     * Gets all active stock records.
     *
     * @return list of all stocks
     */
    List<StockResponseDto> getAllStocks();

    /**
     * Gets expired stock batches.
     *
     * @return list of expired stocks
     */
    List<StockResponseDto> getExpiredStocks();

    /**
     * Gets stocks expiring within a number of days.
     *
     * @param daysThreshold number of days before expiry
     * @return list of stocks expiring soon
     */
    List<StockResponseDto> getExpiringStocks(int daysThreshold);

    /**
     * Gets low stock alerts (quantity below minStockLevel).
     *
     * @return list of low stock items
     */
    List<StockResponseDto> getLowStockAlerts();

    /**
     * Gets total quantity of a drug across all batches.
     *
     * @param drugId the drug ID
     * @return total quantity
     */
    int getTotalQuantityByDrug(Long drugId);

    /**
     * Gets stocks by location.
     *
     * @param location the location to search
     * @return list of stocks in the location
     */
    List<StockResponseDto> getStocksByLocation(String location);

    // ════════════════════════════════════════════════════════════════════
    // Update
    // ════════════════════════════════════════════════════════════════════

    /**
     * Updates an existing stock record.
     * <p>Only provided fields in the DTO will be updated.</p>
     *
     * @param id  the stock ID
     * @param dto the update data
     * @return the updated stock
     */
    StockResponseDto updateStock(Long id, StockUpdateDto dto);

    /**
     * Increases stock quantity (e.g., when receiving a purchase order).
     *
     * @param id       the stock ID
     * @param quantity the quantity to add
     * @return the updated stock
     */
    StockResponseDto addStock(Long id, int quantity);

    /**
     * Decreases stock quantity (e.g., when dispensing).
     *
     * @param id       the stock ID
     * @param quantity the quantity to remove
     * @return the updated stock
     */
    StockResponseDto removeStock(Long id, int quantity);

    // ════════════════════════════════════════════════════════════════════
    // Delete
    // ════════════════════════════════════════════════════════════════════

    /**
     * Soft-deletes a stock record.
     *
     * @param id the stock ID
     */
    void deleteStock(Long id);
}
