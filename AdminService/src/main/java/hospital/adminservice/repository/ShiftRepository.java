package hospital.adminservice.repository;

import hospital.adminservice.model.Shift;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Shift entity.
 *
 * <p><strong>Custom Queries:</strong></p>
 * <ul>
 *   <li>findByCode - Find shift by unique code</li>
 *   <li>findByIsActiveTrue - Active shifts</li>
 *   <li>findByNightShiftTrue - Night shifts</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface ShiftRepository extends BaseEntityRepository<Shift, Long> {

    /**
     * Finds a shift by its unique code.
     *
     * @param code the shift code
     * @return shift if found
     */
    Optional<Shift> findByCode(String code);

    /**
     * Finds all active shifts.
     *
     * @return list of active shifts
     */
    List<Shift> findByIsActiveTrue();

    /**
     * Finds all night shifts.
     *
     * @return list of night shifts
     */
    List<Shift> findByNightShiftTrue();

    /**
     * Checks if a shift code already exists.
     *
     * @param code the code to check
     * @return true if exists
     */
    boolean existsByCode(String code);
}
