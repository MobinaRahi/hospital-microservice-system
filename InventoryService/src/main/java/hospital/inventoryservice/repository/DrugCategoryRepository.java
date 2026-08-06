package hospital.inventoryservice.repository;

import hospital.inventoryservice.repository.BaseEntityRepository;

import hospital.inventoryservice.model.DrugCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for DrugCategory entity.
 * 
 * <p><strong>Query Methods:</strong></p>
 * <ul>
 *   <li>{@code findByParentIsNull()} - Root categories (level 1)</li>
 *   <li>{@code findByParentId(Long)} - Child categories of a parent</li>
 *   <li>{@code findByNameContainingIgnoreCase(String)} - Fuzzy search by name</li>
 *   <li>{@code findByCode(String)} - Find by unique code</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface DrugCategoryRepository extends BaseEntityRepository<DrugCategory, Long> {
    
    /**
     * Finds root categories (no parent).
     *
     * @return list of root categories
     */
    List<DrugCategory> findByParentIsNull();
    
    /**
     * Finds child categories of a parent.
     *
     * @param parentId the parent category ID
     * @return list of child categories
     */
    List<DrugCategory> findByParentId(Long parentId);
    
    /**
     * Finds categories by name (case-insensitive, partial match).
     *
     * @param name the name to search
     * @return list of matching categories
     */
    List<DrugCategory> findByNameContainingIgnoreCase(String name);
    
    /**
     * Finds category by unique code.
     *
     * @param code the category code
     * @return category if found
     */
    Optional<DrugCategory> findByCode(String code);
    
    /**
     * Checks if a category name exists under a specific parent.
     *
     * @param name the category name
     * @param parentId the parent ID (null for root)
     * @return true if exists
     */
    boolean existsByNameAndParentId(String name, Long parentId);
    
    /**
     * Custom query to find categories with their drug count.
     *
     * @return list of categories with drug count
     */
    @Query("SELECT c, COUNT(d) FROM DrugCategory c LEFT JOIN Drug d ON d.category = c GROUP BY c")
    List<Object[]> findAllWithDrugCount();
}
