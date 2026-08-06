package hospital.inventoryservice.repository;

import hospital.inventoryservice.repository.BaseEntityRepository;

import hospital.inventoryservice.model.Drug;
import hospital.inventoryservice.model.enums.DrugForm;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Drug entity.
 * 
 * <p><strong>Query Methods:</strong></p>
 * <ul>
 *   <li>{@code findByBarcode(String)} - Find by unique barcode</li>
 *   <li>{@code findByGenericNameContainingIgnoreCase(String)} - Fuzzy search by generic name</li>
 *   <li>{@code findByCategoryId(Long)} - Drugs in a specific category</li>
 *   <li>{@code findByForm(DrugForm)} - Drugs by pharmaceutical form</li>
 *   <li>{@code findByRequiresPrescriptionTrue()} - Prescription-only drugs</li>
 *   <li>{@code findByIsActiveTrue()} - Active drugs only</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface DrugRepository extends BaseEntityRepository<Drug, Long> {
    
    /**
     * Finds drug by unique barcode.
     *
     * @param barcode the barcode to search
     * @return drug if found
     */
    Optional<Drug> findByBarcode(String barcode);
    
    /**
     * Finds drugs by generic name (case-insensitive, partial match).
     *
     * @param genericName the generic name to search
     * @return list of matching drugs
     */
    List<Drug> findByGenericNameContainingIgnoreCase(String genericName);
    
    /**
     * Finds drugs in a specific category.
     *
     * @param categoryId the category ID
     * @return list of drugs in the category
     */
    List<Drug> findByCategoryId(Long categoryId);
    
    /**
     * Finds drugs by pharmaceutical form.
     *
     * @param form the drug form (tablet, capsule, etc.)
     * @return list of drugs with the form
     */
    List<Drug> findByForm(DrugForm form);
    
    /**
     * Finds prescription-only drugs.
     *
     * @return list of prescription drugs
     */
    List<Drug> findByRequiresPrescriptionTrue();
    
    /**
     * Finds active drugs only.
     *
     * @return list of active drugs
     */
    List<Drug> findByIsActiveTrue();
    
    /**
     * Checks if a barcode already exists.
     *
     * @param barcode the barcode to check
     * @return true if exists
     */
    boolean existsByBarcode(String barcode);
    
    /**
     * Finds drugs by brand name (case-insensitive, partial match).
     *
     * @param brandName the brand name to search
     * @return list of matching drugs
     */
    List<Drug> findByBrandNameContainingIgnoreCase(String brandName);
    
    /**
     * Custom query to find drugs with low stock.
     *
     * @return list of drugs with low stock
     */
    @Query("SELECT DISTINCT d FROM Drug d JOIN Stock s ON s.drug = d WHERE s.quantity <= s.minStockLevel")
    List<Drug> findDrugsWithLowStock();
    
    /**
     * Custom query to find drugs with expiring stock.
     *
     * @param expiryDate number of days before expiry
     * @return list of drugs with expiring stock
     */
    @Query("SELECT DISTINCT d FROM Drug d JOIN Stock s ON s.drug = d WHERE s.expiryDate <= :expiryDate")
    List<Drug> findDrugsWithExpiringStock(@Param("expiryDate") java.time.LocalDate expiryDate);
}
