package hospital.inventoryservice.repository;

import hospital.inventoryservice.repository.BaseEntityRepository;

import hospital.inventoryservice.model.Stock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Stock entity.
 * 
 * <p><strong>Query Methods:</strong></p>
 * <ul>
 *   <li>{@code findByDrugId(Long)} - All batches of a drug</li>
 *   <li>{@code findByExpiryDateBefore(LocalDate)} - Expired stock batches</li>
 *   <li>{@code findByQuantityLessThanEqual(Integer)} - Low stock alerts</li>
 *   <li>{@code findByLocationContainingIgnoreCase(String)} - Search by location</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface StockRepository extends BaseEntityRepository<Stock, Long> {
    
    /**
     * Finds all stock batches for a drug.
     *
     * @param drugId the drug ID
     * @return list of stock batches
     */
    List<Stock> findByDrugId(Long drugId);
    
    /**
     * Finds expired stock batches.
     *
     * @param date the date to check (usually today)
     * @return list of expired batches
     */
    List<Stock> findByExpiryDateBefore(LocalDate date);
    
    /**
     * Finds stock batches with quantity below threshold.
     *
     * @param threshold the quantity threshold
     * @return list of low-stock batches
     */
    List<Stock> findByQuantityLessThanEqual(Integer threshold);
    
    /**
     * Finds stock by location (case-insensitive, partial match).
     *
     * @param location the location to search
     * @return list of stock in the location
     */
    List<Stock> findByLocationContainingIgnoreCase(String location);
    
    /**
     * Finds stock batches expiring within a date range.
     *
     * @param startDate start of the range
     * @param endDate end of the range
     * @return list of batches expiring in the range
     */
    List<Stock> findByExpiryDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Finds total quantity of a drug across all batches.
     *
     * @param drugId the drug ID
     * @return total quantity
     */
    @Query("SELECT SUM(s.quantity) FROM Stock s WHERE s.drug.id = :drugId")
    Integer findTotalQuantityByDrugId(@Param("drugId") Long drugId);
    
    /**
     * Finds stock batches with batch number.
     *
     * @param batchNumber the batch number
     * @return list of matching batches
     */
    List<Stock> findByBatchNumberContainingIgnoreCase(String batchNumber);
}
