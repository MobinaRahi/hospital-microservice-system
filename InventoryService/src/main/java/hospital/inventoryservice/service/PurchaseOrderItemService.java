package hospital.inventoryservice.service;

import hospital.inventoryservice.dto.purchaseorderitem.PurchaseOrderItemCreateDto;
import hospital.inventoryservice.dto.purchaseorderitem.PurchaseOrderItemResponseDto;
import hospital.inventoryservice.dto.purchaseorderitem.PurchaseOrderItemUpdateDto;

import java.util.List;

/**
 * Service interface for PurchaseOrderItem management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Items belong to a purchase order</li>
 *   <li>receivedQuantity cannot exceed ordered quantity</li>
 *   <li>Receiving items auto-updates stock</li>
 *   <li>Subtotal is auto-calculated (unitPrice × receivedQuantity)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface PurchaseOrderItemService {

    // ════════════════════════════════════════════════════════════════════
    // Create
    // ════════════════════════════════════════════════════════════════════

    /**
     * Adds a new item to an existing purchase order.
     *
     * @param purchaseOrderId the purchase order ID
     * @param dto             the item creation data
     * @return the created item
     */
    PurchaseOrderItemResponseDto addItemToOrder(Long purchaseOrderId, PurchaseOrderItemCreateDto dto);

    // ════════════════════════════════════════════════════════════════════
    // Read
    // ════════════════════════════════════════════════════════════════════

    /**
     * Gets an item by its ID.
     *
     * @param id the item ID
     * @return the item
     */
    PurchaseOrderItemResponseDto getItemById(Long id);

    /**
     * Gets all items in a purchase order.
     *
     * @param purchaseOrderId the purchase order ID
     * @return list of items
     */
    List<PurchaseOrderItemResponseDto> getItemsByOrder(Long purchaseOrderId);

    /**
     * Gets all partially received items.
     *
     * @return list of items not fully received
     */
    List<PurchaseOrderItemResponseDto> getPartiallyReceivedItems();

    // ════════════════════════════════════════════════════════════════════
    // Update
    // ════════════════════════════════════════════════════════════════════

    /**
     * Updates an existing item.
     * <p>Only provided fields in the DTO will be updated.</p>
     *
     * @param id  the item ID
     * @param dto the update data
     * @return the updated item
     */
    PurchaseOrderItemResponseDto updateItem(Long id, PurchaseOrderItemUpdateDto dto);

    /**
     * Records received quantity for an item.
     * <p>receivedQuantity cannot exceed ordered quantity.</p>
     * <p>Stock is automatically updated.</p>
     *
     * @param id               the item ID
     * @param receivedQuantity the quantity received
     * @return the updated item
     */
    PurchaseOrderItemResponseDto recordReceived(Long id, int receivedQuantity);

    // ════════════════════════════════════════════════════════════════════
    // Delete
    // ════════════════════════════════════════════════════════════════════

    /**
     * Removes an item from a purchase order.
     *
     * @param id the item ID
     */
    void deleteItem(Long id);
}
