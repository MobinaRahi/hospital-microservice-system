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
 * Repository for Patient entity.
 *
 * @author Mobina
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // ========== Basic Find Methods ==========

    /** Find patient by national ID (unique) */
    Optional<Patient> findByNationalId(String nationalId);

    /** Find patient by phone number (unique) */
    Optional<Patient> findByPhoneNumber(String phoneNumber);

    // ========== Search by Name ==========

    /** Search by first name (partial, case-insensitive) */
    List<Patient> findByFirstNameContainingIgnoreCase(String firstName);

    /** Search by last name (partial, case-insensitive) */
    List<Patient> findByLastNameContainingIgnoreCase(String lastName);

    /** Search by both first and last name */
    List<Patient> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);

    // ========== Advanced Search ==========

    /** Dynamic search with optional filters */
    @Query("SELECT p FROM patientEntity p WHERE " +
            "(:nationalId IS NULL OR p.nationalId = :nationalId) AND " +
            "(:firstName IS NULL OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
            "(:lastName IS NULL OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
            "(:status IS NULL OR p.status = :status)")
    List<Patient> searchPatients(@Param("nationalId") String nationalId,
                                 @Param("firstName") String firstName,
                                 @Param("lastName") String lastName,
                                 @Param("status") PatientStatus status);

    // ========== Room Assignment ==========

    /** Find patients currently in a specific room */
    List<Patient> findByCurrentRoomId(@Param("roomId") Long roomId);

    // ========== Filter by Attributes ==========

    /** Find patients by gender */
    List<Patient> findByGender(Gender gender);

    /** Find patients by blood type */
    List<Patient> findByBloodType(BloodType bloodType);

    /** Find patients by status (no pagination) */
    List<Patient> findByStatus(PatientStatus status);

    /** Find patients by status (with pagination) */
    Page<Patient> findByStatus(PatientStatus status, Pageable pageable);

    // ========== Date Range ==========

    /** Find patients by birthDate range */
    List<Patient> findByBirthDateBetween(LocalDate start, LocalDate end);

    // ========== Update Operations ==========

    /** Update patient status */
    @Modifying
    @Query("UPDATE patientEntity p SET p.status = :status WHERE p.id = :patientId")
    int updateStatus(@Param("patientId") Long patientId, @Param("status") PatientStatus status);

    /** Archive patient (status = ARCHIVED) */
    default int archivePatient(Long patientId) {
        return updateStatus(patientId, PatientStatus.ARCHIVED);
    }

    /** Activate patient (status = ACTIVE) */
    default int activatePatient(Long patientId) {
        return updateStatus(patientId, PatientStatus.ACTIVE);
    }

    /** Deactivate patient (status = INACTIVE) */
    default void deactivatePatient(Long patientId) {
        updateStatus(patientId, PatientStatus.INACTIVE);
    }

    // ========== Existence Checks ==========

    /** Check if national ID exists */
    boolean existsByNationalId(String nationalId);

    /** Check if phone number exists */
    boolean existsByPhoneNumber(String phoneNumber);

    // ========== Count Methods ==========

    /** Count patients by status */
    long countByStatus(PatientStatus status);

    /** Count patients by gender */
    long countByGender(Gender gender);

    /** Count patients by blood type */
    long countByBloodType(BloodType bloodType);
}