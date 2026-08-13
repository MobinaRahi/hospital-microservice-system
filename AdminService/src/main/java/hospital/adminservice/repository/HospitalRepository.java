package hospital.adminservice.repository;

import hospital.adminservice.model.Hospital;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Hospital entity.
 *
 * <p><strong>Custom Queries:</strong></p>
 * <ul>
 *   <li>findByCode - Find hospital by unique code</li>
 *   <li>existsByCode - Check if code exists</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface HospitalRepository extends BaseEntityRepository<Hospital, Long> {

    /**
     * Finds a hospital by its unique code.
     *
     * @param code the hospital code
     * @return hospital if found
     */
    Optional<Hospital> findByCode(String code);

    /**
     * Checks if a hospital code already exists.
     *
     * @param code the code to check
     * @return true if exists
     */
    boolean existsByCode(String code);
}
