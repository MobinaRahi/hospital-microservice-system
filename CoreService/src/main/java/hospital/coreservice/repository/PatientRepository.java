package hospital.coreservice.repository;

import hospital.coreservice.model.Patient;
import hospital.coreservice.model.enums.BloodType;
import hospital.coreservice.model.enums.Gender;
import hospital.coreservice.model.enums.PatientStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Patient entity.
 * Provides database operations for patient management.
 * <p>
 * This interface extends JpaRepository to provide standard CRUD operations
 * and defines custom queries for patient-specific business requirements.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // ========== 1. Basic Find Methods (Exact Match, Unique Fields) ==========

    /**
     * Finds a patient by their national ID.
     * <p>National ID is a 10-digit unique identifier.</p>
     * <p>Stored as String to preserve leading zeros (e.g., "0123456789").</p>
     *
     * @param nationalId the 10-digit national ID
     * @return Optional containing the patient if found, or empty if not found
     */
    Optional<Patient> findByNationalId(String nationalId);

    /**
     * Finds a patient by their phone number.
     * <p>Phone number is an 11-digit unique identifier.</p>
     * <p>Example format: "09123456789"</p>
     *
     * @param phoneNumber the 11-digit mobile number
     * @return Optional containing the patient if found, or empty if not found
     */
    Optional<Patient> findByPhoneNumber(String phoneNumber);

    // ========== 2. Search Methods (Partial Match, Case-Insensitive) ==========

    /**
     * Searches for patients by first name.
     * <p>Search is case-insensitive and supports partial matching.</p>
     * <p>Example: "ali" matches "Ali", "ALI", "Alireza", "Alibaba"</p>
     *
     * @param firstName the first name to search for (partial match allowed)
     * @return List of patients with first name containing the search term
     */
    List<Patient> findByFirstNameContainingIgnoreCase(String firstName);

    /**
     * Searches for patients by last name.
     * <p>Search is case-insensitive and supports partial matching.</p>
     *
     * @param lastName the last name to search for (partial match allowed)
     * @return List of patients with last name containing the search term
     */
    List<Patient> findByLastNameContainingIgnoreCase(String lastName);

    /**
     * Searches for patients by both first name and last name.
     * <p>Both conditions are applied with AND logic.</p>
     * <p>Search is case-insensitive and supports partial matching on both fields.</p>
     *
     * @param firstName the first name to search for (partial match allowed)
     * @param lastName  the last name to search for (partial match allowed)
     * @return List of patients matching both name fields
     */
    List<Patient> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);

    // ========== 3. Advanced Search with Multiple Filters ==========

    /**
     * Advanced search with multiple optional filters.
     * <p>
     * All parameters are optional. Only non-null parameters are applied.
     * This provides a flexible search experience for the front-end.
     * </p>
     * <p>
     * Filter behavior:
     * <ul>
     *     <li>nationalId - exact match</li>
     *     <li>firstName - case-insensitive, partial match (LIKE %value%)</li>
     *     <li>lastName - case-insensitive, partial match (LIKE %value%)</li>
     *     <li>status - exact match (ACTIVE, ARCHIVED, DECEASED, TRANSFERRED)</li>
     * </ul>
     * </p>
     *
     * @param nationalId filter by national ID (optional, exact match)
     * @param firstName  filter by first name (optional, partial match)
     * @param lastName   filter by last name (optional, partial match)
     * @param status     filter by patient status (optional)
     * @return List of patients matching all provided filters
     */
    @Query("SELECT p FROM patientEntity p WHERE " +
            "(:nationalId IS NULL OR p.nationalId = :nationalId) AND " +
            "(:firstName IS NULL OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
            "(:lastName IS NULL OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
            "(:status IS NULL OR p.status = :status)")
    List<Patient> searchPatients(@Param("nationalId") String nationalId,
                                 @Param("firstName") String firstName,
                                 @Param("lastName") String lastName,
                                 @Param("status") PatientStatus status);

    // ========== 4. Room Assignment Queries ==========

    /**
     * Finds all patients currently assigned to a specific room.
     * <p>
     * Use cases:
     * <ul>
     *     <li>Getting the list of patients in a shared ward (multiple beds per room)</li>
     *     <li>Checking which patient is in a specific bed (single-bed room)</li>
     *     <li>Room occupancy management and reporting</li>
     * </ul>
     * </p>
     * <p>
     * Note: For single-bed rooms, the returned list contains at most one patient.
     * For shared wards, the list may contain multiple patients (up to capacity).
     * </p>
     *
     * @param roomId the ID of the room to query
     * @return list of patients currently assigned to the specified room,
     *         or empty list if the room is empty
     */
    List<Patient> findByCurrentRoomId(@Param("roomId") Long roomId);

    // ========== 5. Filter by Attributes ==========

    /**
     * Finds patients by their gender.
     * <p>
     * Possible gender values: MALE, FEMALE, OTHER, UNKNOWN
     * </p>
     *
     * @param gender the gender to filter by
     * @return list of patients with the specified gender
     */
    List<Patient> findByGender(Gender gender);

    /**
     * Finds patients by their blood type.
     * <p>
     * Possible blood types: A_POSITIVE, A_NEGATIVE, B_POSITIVE, B_NEGATIVE,
     * AB_POSITIVE, AB_NEGATIVE, O_POSITIVE, O_NEGATIVE, UNKNOWN
     * </p>
     *
     * @param bloodType the blood type to filter by
     * @return list of patients with the specified blood type
     */
    List<Patient> findByBloodType(BloodType bloodType);

    /**
     * Finds patients by their status without pagination.
     * <p>
     * Patient statuses: ACTIVE, ARCHIVED, DECEASED, TRANSFERRED
     * </p>
     *
     * @param status the patient status to filter by
     * @return list of patients with the specified status
     */
    List<Patient> findByStatus(PatientStatus status);

    // ========== 6. Status Based Methods (With Pagination) ==========

    /**
     * Finds patients by their status with pagination support.
     * <p>
     * Patient statuses:
     * <ul>
     *     <li>ACTIVE - Currently active in the system</li>
     *     <li>ARCHIVED - Archived (soft deleted)</li>
     *     <li>DECEASED - Patient has passed away</li>
     *     <li>TRANSFERRED - Transferred to another hospital</li>
     * </ul>
     * </p>
     *
     * @param status   the patient status to filter by
     * @param pageable pagination information (page number, page size, sort)
     * @return Page of patients with the specified status
     */
    Page<Patient> findByStatus(PatientStatus status, Pageable pageable);

    // ========== 7. Date Range Queries ==========

    /**
     * Finds patients born within a specific date range.
     * <p>
     * Useful for demographic reports and birthday notifications.
     * </p>
     *
     * @param start the start date (inclusive)
     * @param end   the end date (inclusive)
     * @return list of patients born between the given dates
     */
    List<Patient> findByBirthDateBetween(LocalDate start, LocalDate end);

    // ========== 8. Update Operations (Status Management) ==========

    /**
     * Updates a patient's status.
     * <p>This is the base method for all status changes.</p>
     *
     * @param patientId the ID of the patient to update
     * @param status    the new status to set
     * @return number of affected rows (should be 1 if successful)
     */
    @Modifying
    @Query("UPDATE patientEntity p SET p.status = :status WHERE p.id = :patientId")
    int updateStatus(@Param("patientId") Long patientId, @Param("status") PatientStatus status);

    /**
     * Convenience method to archive a patient.
     * <p>Sets status to ARCHIVED (soft delete).</p>
     * <p>The patient record remains in the database but is excluded from normal queries.</p>
     *
     * @param patientId the ID of the patient to archive
     * @return number of affected rows (should be 1 if successful)
     */
    default int archivePatient(Long patientId) {
        return updateStatus(patientId, PatientStatus.ARCHIVED);
    }

    /**
     * Convenience method to activate a patient.
     * <p>Sets status to ACTIVE.</p>
     *
     * @param patientId the ID of the patient to activate
     * @return number of affected rows (should be 1 if successful)
     */
    default int activatePatient(Long patientId) {
        return updateStatus(patientId, PatientStatus.ACTIVE);
    }

    default void deactivatePatient(Long patientId) {
        updateStatus(patientId, PatientStatus.INACTIVE);
    }

    // ========== 9. Existence Checks ==========

    /**
     * Checks if a patient exists with the given national ID.
     * <p>Used for validation before creating or updating a patient.</p>
     *
     * @param nationalId the national ID to check
     * @return true if a patient with this national ID exists, false otherwise
     */
    boolean existsByNationalId(String nationalId);

    /**
     * Checks if a patient exists with the given phone number.
     * <p>Used for validation before creating or updating a patient.</p>
     *
     * @param phoneNumber the phone number to check
     * @return true if a patient with this phone number exists, false otherwise
     */
    boolean existsByPhoneNumber(String phoneNumber);

    // ========== 10. Count Methods ==========

    /**
     * Counts the number of patients with a specific status.
     * <p>Useful for dashboard statistics and reporting.</p>
     *
     * @param status the patient status to count
     * @return count of patients with the specified status
     */
    long countByStatus(PatientStatus status);

    /**
     * Counts the number of patients by gender.
     * <p>Useful for demographic reports.</p>
     *
     * @param gender the gender to count
     * @return count of patients with the specified gender
     */
    long countByGender(Gender gender);

    /**
     * Counts the number of patients by blood type.
     * <p>Useful for blood bank reports and emergency preparedness.</p>
     *
     * @param bloodType the blood type to count
     * @return count of patients with the specified blood type
     */
    long countByBloodType(BloodType bloodType);
}
