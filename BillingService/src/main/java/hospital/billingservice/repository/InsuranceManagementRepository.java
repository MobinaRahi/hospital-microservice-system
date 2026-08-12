package hospital.billingservice.repository;

import hospital.billingservice.model.InsuranceManagement;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for InsuranceManagement entity.
 *
 * @author MobinaRahi
 */
@Repository
public interface InsuranceManagementRepository extends BaseEntityRepository<InsuranceManagement, Long> {

    /**
     * Finds insurance plan by its unique code.
     *
     * @param code the insurance code
     * @return insurance plan if found
     */
    Optional<InsuranceManagement> findByCode(String code);

    /**
     * Finds all active insurance plans.
     *
     * @return list of active insurance plans
     */
    List<InsuranceManagement> findByIsActiveTrue();

    /**
     * Checks if a code already exists.
     *
     * @param code the code to check
     * @return true if exists
     */
    boolean existsByCode(String code);

    /**
     * Searches insurance plans by name (case-insensitive, partial match).
     *
     * @param name the name to search
     * @return list of matching insurance plans
     */
    List<InsuranceManagement> findByNameContainingIgnoreCase(String name);
}
