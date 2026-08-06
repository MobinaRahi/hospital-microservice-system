package hospital.inventoryservice.repository;

import hospital.inventoryservice.repository.BaseEntityRepository;

import hospital.inventoryservice.model.PurchaseOrderItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for PurchaseOrderItem entity.
 * 
 * <p><strong>Query Methods:</strong></p>
 * <ul>
 *   <li>{@code findByPurchaseOrderId(Long)} - All items in a purchase order</li>
 *   <li>{@code findByDrugId(Long)} - Order items for a specific drug</li>
 *   <li>{@code findByReceivedQuantityLessThanQuantity()} - Items not fully received</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface PurchaseOrderItemRepository extends BaseEntityRepository<PurchaseOrderItem, Long> {
    
    /**
     * Finds all items in a purchase order.
     *
     * @param purchaseOrderId the purchase order ID
     * @return list of items in the order
     */
    List<PurchaseOrderItem> findByPurchaseOrderId(Long purchaseOrderId);
    
    /**
     * Finds order items for a specific drug.
     *
     * @param drugId the drug ID
     * @return list of order items for the drug
     */
    List<PurchaseOrderItem> findByDrugId(Long drugId);
    
    /**
     * Finds items that haven't been fully received.
     *
     * @return list of partially received items
     */
    List<PurchaseOrderItem> findByReceivedQuantityLessThanQuantity();
    
    /**
     * Finds total quantity ordered for a drug across all orders.
     *
     * @param drugId the drug ID
     * @return total quantity ordered
     */
    @Query("SELECT SUM(poi.quantity) FROM PurchaseOrderItem poi WHERE poi.drug.id = :drugId")
    Integer findTotalQuantityOrderedByDrugId(@Param("drugId") Long drugId);
    
    /**
     * Finds total quantity received for a drug across all orders.
     *
     * @param drugId the drug ID
     * @return total quantity received
     */
    @Query("SELECT SUM(poi.receivedQuantity) FROM PurchaseOrderItem poi WHERE poi.drug.id = :drugId")
    Integer findTotalQuantityReceivedByDrugId(@Param("drugId") Long drugId);
}
