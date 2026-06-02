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
 *
 * @author Mobina
 */
@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> {

    // ========== Basic Find Methods (Unique Fields) ==========

    /**
     * Find nurse by user ID (Auth Service)
     */
    Optional<Nurse> findByUserId(Long userId);

    /**
     * Find nurse by national ID (unique)
     */
    Optional<Nurse> findByNationalId(String nationalId);

    /**
     * Find nurse by employee code (unique)
     */
    Optional<Nurse> findByNurseCode(String nurseCode);

    /**
     * Find nurse by phone number (unique)
     */
    Optional<Nurse> findByPhoneNumber(String phoneNumber);

    // ========== Search Methods (Partial, Case-Insensitive) ==========

    /**
     * Search nurses by first name (partial, case-insensitive)
     */
    List<Nurse> findByFirstNameContainingIgnoreCase(String firstName);
    List<Nurse> findByFirstNameContainingIgnoreCaseAndActiveTrue(String firstName);

    /**
     * Search nurses by last name (partial, case-insensitive)
     */
    List<Nurse> findByLastNameContainingIgnoreCase(String lastName);
    List<Nurse> findByLastNameContainingIgnoreCaseAndActiveTrue(String lastName);

    /**
     * Search nurses by first name and last name together
     */
    List<Nurse> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);
    List<Nurse> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndActiveTrue(String firstName, String lastName);

    // ========== Find by Experience ==========

    /**
     * Find nurses by years of experience range
     */
    List<Nurse> findByYearsOfExperienceBetween(int start, int end);
    List<Nurse> findByYearsOfExperienceBetweenAndActiveTrue(int start, int end);

    // ========== Find by Position ==========

    /**
     * Find nurses by position
     */
    List<Nurse> findByPosition(NursePosition position);
    List<Nurse> findByPositionAndActiveTrue(NursePosition position);

    // ========== Find by Department (Many-to-Many) ==========

    /**
     * Find nurses working in a specific department
     */
    @Query("SELECT n FROM nurseEntity n JOIN n.departmentList d WHERE d.id = :departmentId")
    List<Nurse> findByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * Find active nurses in a specific department
     */
    @Query("SELECT n FROM nurseEntity n JOIN n.departmentList d WHERE d.id = :departmentId AND n.isActive = true")
    List<Nurse> findActiveNursesByDepartmentId(@Param("departmentId") Long departmentId);

    // ========== Eager Loading (Avoid LazyInitializationException) ==========

    /**
     * Find nurse by ID with shift preferences loaded eagerly
     */
    @Query("SELECT n FROM nurseEntity n LEFT JOIN FETCH n.shiftPreferenceList WHERE n.id = :nurseId")
    Optional<Nurse> findByIdWithShifts(@Param("nurseId") Long nurseId);

    // ========== Status Based ==========

    /**
     * Get all active nurses
     */
    @Query("SELECT n FROM nurseEntity n WHERE n.isActive = true")
    List<Nurse> findAllActiveNurses();

    /**
     * Get all inactive nurses
     */
    @Query("SELECT n FROM nurseEntity n WHERE n.isActive = false")
    List<Nurse> findAllInactiveNurses();

    // ========== Update Operations ==========

    /**
     * Deactivate nurse (set isActive = false)
     */
    @Modifying
    @Query("UPDATE nurseEntity n SET n.isActive = false WHERE n.id = :nurseId")
    void deactivate(@Param("nurseId") Long nurseId);

    /**
     * Activate nurse (set isActive = true)
     */
    @Modifying
    @Query("UPDATE nurseEntity n SET n.isActive = true WHERE n.id = :nurseId")
    void activate(@Param("nurseId") Long nurseId);

    // ========== Existence Checks ==========

    /**
     * Check if nurse exists by national ID
     */
    boolean existsByNationalId(String nationalId);

    /**
     * Check if nurse exists by user ID
     */
    boolean existsByUserId(Long userId);

    /**
     * Check if nurse exists by nurse code
     */
    boolean existsByNurseCode(String nurseCode);

    // ========== Count Methods ==========

    /**
     * Count nurses in a specific department
     */
    @Query("SELECT COUNT(n) FROM nurseEntity n JOIN n.departmentList d WHERE d.id = :departmentId")
    Long countByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * Count nurses by position
     */
    @Query("SELECT COUNT(n) FROM nurseEntity n WHERE n.position = :position")
    Long countByPosition(@Param("position") NursePosition position);

    /**
     * Count active nurses
     */
    @Query("SELECT COUNT(n) FROM nurseEntity n WHERE n.isActive = true")
    Long countActiveNurses();

    /**
     * Count inactive nurses
     */
    @Query("SELECT COUNT(n) FROM nurseEntity n WHERE n.isActive = false")
    Long countInactiveNurses();
}