package hospital.billingservice.repository;

import hospital.billingservice.model.ServiceCatalog;
import hospital.billingservice.model.enums.ServiceCategory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for ServiceCatalog entity.
 *
 * @author MobinaRahi
 */
@Repository
public interface ServiceCatalogRepository extends BaseEntityRepository<ServiceCatalog, Long> {

    /**
     * Finds a service by its unique code.
     *
     * @param code the service code
     * @return service catalog entry if found
     */
    Optional<ServiceCatalog> findByCode(String code);

    /**
     * Finds all active services.
     *
     * @return list of active services
     */
    List<ServiceCatalog> findByIsActiveTrue();

    /**
     * Finds services by category.
     *
     * @param category the service category
     * @return list of services in the category
     */
    List<ServiceCatalog> findByCategory(ServiceCategory category);

    /**
     * Finds active services by category.
     *
     * @param category the service category
     * @return list of active services in the category
     */
    List<ServiceCatalog> findByCategoryAndIsActiveTrue(ServiceCategory category);

    /**
     * Checks if a service code already exists.
     *
     * @param code the code to check
     * @return true if exists
     */
    boolean existsByCode(String code);

    /**
     * Searches services by name (case-insensitive, partial match).
     *
     * @param name the name to search
     * @return list of matching services
     */
    List<ServiceCatalog> findByNameContainingIgnoreCase(String name);
}
