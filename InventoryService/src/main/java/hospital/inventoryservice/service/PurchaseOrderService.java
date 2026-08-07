package hospital.inventoryservice.service;

import hospital.inventoryservice.dto.purchaseorder.PurchaseOrderCreateDto;
import hospital.inventoryservice.dto.purchaseorder.PurchaseOrderResponseDto;
import hospital.inventoryservice.dto.purchaseorder.PurchaseOrderUpdateDto;
import hospital.inventoryservice.model.enums.PurchaseOrderStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for PurchaseOrder management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Status workflow: PENDING → APPROVED → SENT → PARTIAL → COMPLETED</li>
 *   <li>Cancel can happen at any status</li>
 *   <li>Receiving a purchase order auto-updates stock quantities</li>
 *   <li>Total amount is auto-calculated from items</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface PurchaseOrderService {

    // ════════════════════════════════════════════════════════════════════
    // Create
    // ════════════════════════════════════════════════════════════════════

    /**
     * Creates a new purchase order with items.
     *
     * @param dto the purchase order creation data
     * @return the created purchase order
     */
    PurchaseOrderResponseDto createPurchaseOrder(PurchaseOrderCreateDto dto);

    // ════════════════════════════════════════════════════════════════════
    // Read
    // ════════════════════════════════════════════════════════════════════

    /**
     * Gets a purchase order by its ID.
     *
     * @param id the purchase order ID
     * @return the purchase order with items
     */
    PurchaseOrderResponseDto getPurchaseOrderById(Long id);

    /**
     * Gets all purchase orders.
     *
     * @return list of all purchase orders
     */
    List<PurchaseOrderResponseDto> getAllPurchaseOrders();

    /**
     * Gets purchase orders by status.
     *
     * @param status the status to filter
     * @return list of matching purchase orders
     */
    List<PurchaseOrderResponseDto> getPurchaseOrdersByStatus(PurchaseOrderStatus status);

    /**
     * Gets purchase orders from a specific supplier.
     *
     * @param supplierId the supplier ID
     * @return list of purchase orders
     */
    List<PurchaseOrderResponseDto> getPurchaseOrdersBySupplier(Long supplierId);

    /**
     * Gets purchase orders in a date range.
     *
     * @param startDate start of the range
     * @param endDate   end of the range
     * @return list of purchase orders
     */
    List<PurchaseOrderResponseDto> getPurchaseOrdersByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Gets overdue purchase orders (expected delivery passed, not received).
     *
     * @return list of overdue purchase orders
     */
    List<PurchaseOrderResponseDto> getOverduePurchaseOrders();

    // ════════════════════════════════════════════════════════════════════
    // Update
    // ════════════════════════════════════════════════════════════════════

    /**
     * Updates an existing purchase order.
     * <p>Only provided fields in the DTO will be updated.</p>
     *
     * @param id  the purchase order ID
     * @param dto the update data
     * @return the updated purchase order
     */
    PurchaseOrderResponseDto updatePurchaseOrder(Long id, PurchaseOrderUpdateDto dto);

    // ════════════════════════════════════════════════════════════════════
    // Status Transitions
    // ════════════════════════════════════════════════════════════════════

    /**
     * Approves a purchase order (PENDING → APPROVED).
     *
     * @param id the purchase order ID
     * @return the updated purchase order
     */
    PurchaseOrderResponseDto approveOrder(Long id);

    /**
     * Marks a purchase order as sent to supplier (APPROVED → SENT).
     *
     * @param id the purchase order ID
     * @return the updated purchase order
     */
    PurchaseOrderResponseDto sendOrder(Long id);

    /**
     * Marks partial receipt of a purchase order (SENT → PARTIAL).
     * <p>Stock quantities are partially updated.</p>
     *
     * @param id the purchase order ID
     * @return the updated purchase order
     */
    PurchaseOrderResponseDto partialReceiveOrder(Long id);

    /**
     * Marks complete receipt of a purchase order.
     * <p>Stock quantities are fully updated.</p>
     *
     * @param id the purchase order ID
     * @return the updated purchase order
     */
    PurchaseOrderResponseDto completeOrder(Long id);

    /**
     * Cancels a purchase order (any status → CANCELLED).
     *
     * @param id the purchase order ID
     * @return the updated purchase order
     */
    PurchaseOrderResponseDto cancelOrder(Long id);

    // ════════════════════════════════════════════════════════════════════
    // Delete
    // ════════════════════════════════════════════════════════════════════

    /**
     * Soft-deletes a purchase order.
     *
     * @param id the purchase order ID
     */
    void deletePurchaseOrder(Long id);
}
