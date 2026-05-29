package hospital.coreservice.repository;

import hospital.coreservice.model.Nurse;
import hospital.coreservice.model.enums.NursePosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Nurse entity.
 * Provides database operations for nurse management.
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> {

    // ==========  Basic Find Methods (Unique Fields) ==========

    /**
     * Finds a nurse by user ID (reference to Auth Service).
     *
     * @param userId the user ID from Auth Service
     * @return Optional containing the nurse if found
     */
    Optional<Nurse> findByUserId(@Param("userId") Long userId);

    /**
     * Finds a nurse by national ID (unique).
     *
     * @param nationalId the 10-digit national ID
     * @return Optional containing the nurse if found
     */
    Optional<Nurse> findByNationalId(@Param("nationalId") String nationalId);

    /**
     * Finds a nurse by unique employee code.
     *
     * @param nurseCode the nurse's employee code (e.g., "NUR-001")
     * @return Optional containing the nurse if found
     */
    Optional<Nurse> findByNurseCode(@Param("nurseCode") String nurseCode);

    /**
     * Finds a nurse by phone number (unique).
     *
     * @param phoneNumber the 11-digit mobile number
     * @return Optional containing the nurse if found
     */
    Optional<Nurse> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    // ==========  Search Methods (Partial Match, Case-Insensitive) ==========

    /**
     * Searches for nurses by first name (case-insensitive, partial match).
     *
     * @param firstName the first name to search for
     * @return list of nurses with matching first name
     */
    List<Nurse> findByFirstNameContainingIgnoreCase(@Param("firstName") String firstName);

    /**
     * Searches for nurses by last name (case-insensitive, partial match).
     *
     * @param lastName the last name to search for
     * @return list of nurses with matching last name
     */
    List<Nurse> findByLastNameContainingIgnoreCase(@Param("lastName") String lastName);

    // ==========  Find NursesShifts ==========

    /**
     * Finds a nurse with their shifts loaded eagerly to avoid LazyInitializationException.
     *
     * @param nurseId the ID of the nurse
     * @return Optional containing the nurse with shifts
     */
    @Query("SELECT n FROM nurseEntity n LEFT JOIN FETCH n.shiftPreferenceList WHERE n.id = :nurseId")
    Optional<Nurse> findByIdWithShifts(@Param("nurseId") Long nurseId);

    // ==========  Find by Position ==========

    /**
     * Finds nurses by their position (HEAD_NURSE, SENIOR_NURSE, STAFF_NURSE, etc.).
     *
     * @param position the nurse position
     * @return list of nurses with the given position
     */
    List<Nurse> findByPosition(@Param("position") NursePosition position);

    // ==========  Find by Department ==========

    /**
     * Finds nurses working in a specific department.
     * Note: Nurses can work in multiple departments (Many-to-Many),
     * so this returns nurses assigned to the given department.
     *
     * @param departmentId the department ID
     * @return list of nurses in the department
     */
    List<Nurse> findByDepartmentId(@Param("departmentId") Long departmentId);

    // ==========  Status Based Methods ==========

    /**
     * Finds all active nurses (isActive = true).
     *
     * @return list of active nurses
     */
    @Query("SELECT n FROM nurseEntity n WHERE n.isActive = true")
    List<Nurse> findAllActiveNurses();

    /**
     * Finds all inactive nurses (isActive = false).
     *
     * @return list of inactive nurses
     */
    @Query("SELECT n FROM nurseEntity n WHERE n.isActive = false")
    List<Nurse> findAllInactiveNurses();

    // ==========  Update Operations (Status Management) ==========

    /**
     * Deactivates a nurse (sets isActive = false).
     *
     * @param nurseId the ID of the nurse to deactivate
     */
    @Modifying
    @Query("UPDATE nurseEntity n SET n.isActive = false WHERE n.id = :nurseId")
    void deactivate(@Param("nurseId") Long nurseId);

    /**
     * Activates a nurse (sets isActive = true).
     *
     * @param nurseId the ID of the nurse to activate
     */
    @Modifying
    @Query("UPDATE nurseEntity n SET n.isActive = true WHERE n.id = :nurseId")
    void activate(@Param("nurseId") Long nurseId);

    // ==========  Existence Checks ==========

    /**
     * Checks if a nurse exists with the given national ID.
     *
     * @param nationalId the national ID to check
     * @return true if a nurse with this national ID exists
     */
    boolean existsByNationalId(@Param("nationalId") String nationalId);

    /**
     * Checks if a nurse exists with the given employee code.
     *
     * @param nurseCode the nurse code to check
     * @return true if a nurse with this nurse code exists
     */
    boolean existsByNurseCode(@Param("nurseCode") String nurseCode);

    Long countByDepartmentId(Long departmentId);
}
