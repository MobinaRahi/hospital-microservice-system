package hospital.inventoryservice.repository;

import hospital.inventoryservice.repository.BaseEntityRepository;

import hospital.inventoryservice.model.Supplier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Supplier entity.
 * 
 * <p><strong>Query Methods:</strong></p>
 * <ul>
 *   <li>{@code findByEmail(String)} - Find supplier by email</li>
 *   <li>{@code findByNameContainingIgnoreCase(String)} - Fuzzy search by name</li>
 *   <li>{@code findByIsActiveTrue()} - Active suppliers only</li>
 *   <li>{@code findByPhoneOrMobile(String)} - Find by phone or mobile</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface SupplierRepository extends BaseEntityRepository<Supplier, Long> {
    
    /**
     * Finds supplier by email.
     *
     * @param email the email to search
     * @return supplier if found
     */
    Optional<Supplier> findByEmail(String email);
    
    /**
     * Finds suppliers by name (case-insensitive, partial match).
     *
     * @param name the name to search
     * @return list of matching suppliers
     */
    List<Supplier> findByNameContainingIgnoreCase(String name);
    
    /**
     * Finds active suppliers only.
     *
     * @return list of active suppliers
     */
    List<Supplier> findByIsActiveTrue();
    
    /**
     * Finds supplier by phone or mobile number.
     *
     * @param phone phone or mobile number
     * @return supplier if found
     */
    Optional<Supplier> findByPhoneOrMobile(String phone, String mobile);
    
    /**
     * Finds suppliers by contact person name.
     *
     * @param contactPerson the contact person name
     * @return list of matching suppliers
     */
    List<Supplier> findByContactPersonContainingIgnoreCase(String contactPerson);
    
    /**
     * Checks if an email already exists.
     *
     * @param email the email to check
     * @return true if exists
     */
    boolean existsByEmail(String email);
}
