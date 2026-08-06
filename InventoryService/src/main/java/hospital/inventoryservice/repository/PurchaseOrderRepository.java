package hospital.inventoryservice.repository;

import hospital.inventoryservice.repository.BaseEntityRepository;

import hospital.inventoryservice.model.PurchaseOrder;
import hospital.inventoryservice.model.enums.PurchaseOrderStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for PurchaseOrder entity.
 * 
 * <p><strong>Query Methods:</strong></p>
 * <ul>
 *   <li>{@code findBySupplierId(Long)} - Orders from a specific supplier</li>
 *   <li>{@code findByStatus(PurchaseOrderStatus)} - Orders with specific status</li>
 *   <li>{@code findByOrderDateBetween(LocalDate, LocalDate)} - Orders in date range</li>
 *   <li>{@code findByExpectedDeliveryDateBefore(LocalDate)} - Overdue orders</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface PurchaseOrderRepository extends BaseEntityRepository<PurchaseOrder, Long> {
    
    /**
     * Finds purchase orders from a specific supplier.
     *
     * @param supplierId the supplier ID
     * @return list of orders from the supplier
     */
    List<PurchaseOrder> findBySupplierId(Long supplierId);
    
    /**
     * Finds purchase orders with specific status.
     *
     * @param status the order status
     * @return list of orders with the status
     */
    List<PurchaseOrder> findByStatus(PurchaseOrderStatus status);
    
    /**
     * Finds purchase orders in a date range.
     *
     * @param startDate start of the range
     * @param endDate end of the range
     * @return list of orders in the range
     */
    List<PurchaseOrder> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Finds overdue orders (expected delivery date passed but not received).
     *
     * @param date the date to check (usually today)
     * @return list of overdue orders
     */
    List<PurchaseOrder> findByExpectedDeliveryDateBeforeAndStatusNot(LocalDate date, PurchaseOrderStatus status);
    
    /**
     * Finds orders created by a specific user.
     *
     * @param createdByUser the user ID
     * @return list of orders created by the user
     */
    List<PurchaseOrder> findByCreatedByUser(Long createdByUser);
    
    /**
     * Finds orders with total amount above a threshold.
     *
     * @param minAmount the minimum amount
     * @return list of high-value orders
     */
    @Query("SELECT po FROM PurchaseOrder po WHERE po.totalAmount >= :minAmount")
    List<PurchaseOrder> findByTotalAmountGreaterThanEqual(@Param("minAmount") java.math.BigDecimal minAmount);
    
    /**
     * Custom query to get order statistics by status.
     *
     * @return list of status and count
     */
    @Query("SELECT po.status, COUNT(po) FROM PurchaseOrder po GROUP BY po.status")
    List<Object[]> countOrdersByStatus();
}
