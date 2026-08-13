package hospital.labservice.repository;

import hospital.labservice.model.LabTechnician;
import hospital.labservice.model.enums.LabShift;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for LabTechnician entity.
 *
 * <p><strong>Custom Queries:</strong></p>
 * <ul>
 *   <li>findByUserId - Technician linked to AuthService user</li>
 *   <li>findByEmployeeCode - Technician by employee code</li>
 *   <li>findByShift - Technicians on a specific shift</li>
 *   <li>findByIsActive - Active/inactive technicians</li>
 *   <li>findBySpecialization - Technicians by area of expertise</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface LabTechnicianRepository extends BaseEntityRepository<LabTechnician, Long> {

    /**
     * Finds a technician by their AuthService user ID.
     *
     * @param userId the user ID from AuthService
     * @return technician if found
     */
    Optional<LabTechnician> findByUserId(Long userId);

    /**
     * Finds a technician by their unique employee code.
     *
     * @param employeeCode the employee code (e.g., "TECH-001")
     * @return technician if found
     */
    Optional<LabTechnician> findByEmployeeCode(String employeeCode);

    /**
     * Finds technicians by assigned shift.
     *
     * @param shift the work shift (MORNING, EVENING, NIGHT)
     * @return list of technicians on the shift
     */
    List<LabTechnician> findByShift(LabShift shift);

    /**
     * Finds technicians by active status.
     *
     * @param isActive whether the technician is active
     * @return list of active/inactive technicians
     */
    List<LabTechnician> findByIsActive(Boolean isActive);

    /**
     * Finds active technicians on a specific shift.
     * Used for shift assignment and workload balancing.
     *
     * @param shift    the work shift
     * @param isActive whether the technician is active
     * @return list of matching technicians
     */
    List<LabTechnician> findByShiftAndIsActive(LabShift shift, Boolean isActive);

    /**
     * Finds technicians by area of specialization.
     *
     * @param specialization the specialization (e.g., "Hematology")
     * @return list of technicians with the specialization
     */
    List<LabTechnician> findBySpecialization(String specialization);

    /**
     * Checks if a user ID is already linked to a technician.
     *
     * @param userId the user ID to check
     * @return true if a technician with this user ID exists
     */
    boolean existsByUserId(Long userId);

    /**
     * Checks if an employee code already exists.
     *
     * @param employeeCode the employee code to check
     * @return true if a technician with this code exists
     */
    boolean existsByEmployeeCode(String employeeCode);

    /**
     * Finds technicians by first name pattern (case-insensitive).
     *
     * @param firstName the first name pattern
     * @return list of matching technicians
     */
    List<LabTechnician> findByFirstNameContainingIgnoreCase(String firstName);

    /**
     * Finds technicians by last name pattern (case-insensitive).
     *
     * @param lastName the last name pattern
     * @return list of matching technicians
     */
    List<LabTechnician> findByLastNameContainingIgnoreCase(String lastName);

    /**
     * Counts active technicians.
     *
     * @param isActive whether the technician is active
     * @return number of active/inactive technicians
     */
    long countByIsActive(Boolean isActive);
}
