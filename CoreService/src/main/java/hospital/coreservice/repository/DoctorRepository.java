package hospital.coreservice.repository;

import com.hospital.coreService.model.Doctor;
import com.hospital.coreService.model.enums.Speciality;
import com.hospital.coreService.model.enums.SubSpeciality;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // ========== 1. Basic Find Methods (Exact Match) ==========

    /**
     * Find doctors by first name (exact match).
     *
     * @param firstName the first name to search for
     * @return list of doctors with the given first name
     */
    List<Doctor> findByFirstName(String firstName);

    /**
     * Find doctors by last name (exact match).
     *
     * @param lastName the last name to search for
     * @return list of doctors with the given last name
     */
    List<Doctor> findByLastName(String lastName);

    /**
     * Find a doctor by license number (unique).
     *
     * @param licenseNumber the medical council license number
     * @return Optional containing the doctor if found
     */
    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    /**
     * Find doctors by their primary specialty.
     *
     * @param speciality the medical specialty (e.g., CARDIOLOGY)
     * @return list of doctors with the given specialty
     */
    List<Doctor> findBySpeciality(Speciality speciality);

    // ========== 2. Advanced Search Methods (Partial Match, Join) ==========

    /**
     * Search for doctors by sub-specialty (e.g., INTERVENTIONAL_CARDIOLOGY).
     * Uses JOIN query on the doctor_sub_specialities collection table.
     *
     * @param subSpeciality the sub-specialty to search for
     * @return list of doctors having this sub-specialty
     */
    @Query("SELECT d FROM doctorEntity d JOIN d.subSpecialities s WHERE s = :subSpeciality")
    List<Doctor> findBySubSpeciality(@Param("subSpeciality") SubSpeciality subSpeciality);

    /**
     * Find doctors by department ID.
     *
     * @param departmentId the department identifier
     * @return list of doctors working in the specified department
     */
    List<Doctor> findByDepartmentId(Long departmentId);

    /**
     * Search for doctors by first name and last name (case-insensitive, partial match).
     * Useful for search boxes in UI.
     *
     * @param firstName the first name (partial match allowed)
     * @param lastName the last name (partial match allowed)
     * @return list of doctors matching both name fields
     */
    List<Doctor> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);

    // ========== 3. Status Based Methods ==========

    /**
     * Find all active doctors (isActive = true).
     *
     * @return list of active doctors
     */
    @Query("SELECT d FROM doctorEntity d WHERE d.isActive = true")
    List<Doctor> findAllActiveDoctors();

    /**
     * Find all inactive doctors (isActive = false).
     *
     * @return list of inactive doctors
     */
    @Query("SELECT d FROM doctorEntity d WHERE d.isActive = false")
    List<Doctor> findAllInactiveDoctors();

    // ========== 4. Pagination Methods ==========

    /**
     * Get all doctors with pagination support.
     * Useful for large datasets to avoid memory issues.
     *
     * @param pageable pagination information (page number, page size, sort)
     * @return a page of doctors
     */
    Page<Doctor> findAll(Pageable pageable);

    /**
     * Get active doctors with pagination support.
     *
     * @param pageable pagination information
     * @return a page of active doctors
     */
    Page<Doctor> findByIsActiveTrue(Pageable pageable);

    // ========== 5. Update Operations (Status Management) ==========

    /**
     * Deactivate a doctor (sets isActive = false).
     *
     * @param doctorId the ID of the doctorSchedule to deactivate
     */
    @Modifying
    @Query("UPDATE doctorEntity d SET d.isActive = false WHERE d.id = :doctorId")
    void deactivate(@Param("doctorId") Long doctorId);

    /**
     * Activate a doctor (sets isActive = true).
     *
     * @param doctorId the ID of the doctor to activate
     */
    @Modifying
    @Query("UPDATE doctorEntity d SET d.isActive = true WHERE d.id = :doctorId")
    void activate(@Param("doctorId") Long doctorId);

    // ========== 6. Existence Checks ==========

    /**
     * Check if a doctor exists with the given license number.
     *
     * @param licenseNumber the license number to check
     * @return true if a doctor with this license number exists
     */
    boolean existsByLicenseNumber(String licenseNumber);


    Long countByDepartmentId(Long departmentId);
}
