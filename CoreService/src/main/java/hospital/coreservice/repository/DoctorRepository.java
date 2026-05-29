package hospital.coreservice.repository;

import hospital.coreservice.model.Doctor;
import hospital.coreservice.model.enums.Speciality;
import hospital.coreservice.model.enums.SubSpeciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // ========== 1. Basic Find Methods ==========

    /**
     * Find doctor by user ID (reference to Auth Service)
     */
    Optional<Doctor> findByUserId(Long userId);

    /**
     * Find doctor by license number (unique)
     */
    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    /**
     * Find doctors by first name
     */
    List<Doctor> findByFirstNameContainingIgnoreCase(String firstName);

    /**
     * Find doctors by last name
     */
    List<Doctor> findByLastNameContainingIgnoreCase(String lastName);

    /**
     * Find doctors by specialty
     */
    List<Doctor> findBySpeciality(Speciality speciality);

    /**
     * Find doctors by department ID
     */
    List<Doctor> findByDepartmentId(Long departmentId);

    // ========== 2. Search Methods (Partial Match, Case-Insensitive) ==========

    /**
     * Search doctors by first name and last name (partial match, case-insensitive)
     */
    List<Doctor> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);

    /**
     * Search doctors by sub-specialty (uses JOIN on collection table)
     */
    @Query("SELECT d FROM doctorEntity d JOIN d.subSpecialities s WHERE s = :subSpeciality")
    List<Doctor> findBySubSpeciality(@Param("subSpeciality") SubSpeciality subSpeciality);

    /**
     * Search doctors by years of experience range
     */
    List<Doctor> findByYearsOfExperienceBetween(int startYears, int endYears);

    // ========== 3. Active/Inactive Filtering ==========

    /**
     * Get all active doctors
     */
    @Query("SELECT d FROM doctorEntity d WHERE d.isActive = true")
    List<Doctor> findAllActiveDoctors();

    /**
     * Get all inactive doctors
     */
    @Query("SELECT d FROM doctorEntity d WHERE d.isActive = false")
    List<Doctor> findAllInactiveDoctors();

    /**
     * Get active doctors by specialty
     */
    @Query("SELECT d FROM doctorEntity d WHERE d.speciality = :speciality AND d.isActive = true")
    List<Doctor> findActiveBySpeciality(@Param("speciality") Speciality speciality);

    /**
     * Get active doctors by sub-specialty
     */
    @Query("SELECT d FROM doctorEntity d JOIN d.subSpecialities s WHERE s = :subSpeciality AND d.isActive = true")
    List<Doctor> findActiveBySubSpeciality(@Param("subSpeciality") SubSpeciality subSpeciality);

    /**
     * Get active doctors by department ID
     */
    @Query("SELECT d FROM doctorEntity d WHERE d.department.id = :departmentId AND d.isActive = true")
    List<Doctor> findActiveByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * Get active doctors by specialty and sub-specialty together
     */
    @Query("SELECT d FROM doctorEntity d JOIN d.subSpecialities s WHERE s = :subSpeciality AND d.speciality = :speciality AND d.isActive = true")
    List<Doctor> findActiveDoctorsBySpecialityAndSubSpeciality(@Param("speciality") Speciality speciality, @Param("subSpeciality") SubSpeciality subSpeciality);

    // ========== 4. Combined Filters (Without Active Check) ==========

    /**
     * Get doctors by specialty and sub-specialty (all statuses)
     */
    @Query("SELECT d FROM doctorEntity d JOIN d.subSpecialities s WHERE s = :subSpeciality AND d.speciality = :speciality")
    List<Doctor> findBySpecialityAndSubSpeciality(@Param("speciality") Speciality speciality, @Param("subSpeciality") SubSpeciality subSpeciality);

    // ========== 5. Count Methods ==========

    /**
     * Count doctors by department ID
     */
    @Query("SELECT COUNT(d) FROM doctorEntity d WHERE d.department.id = :departmentId")
    Long countByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * Count doctors by specialty
     */
    @Query("SELECT COUNT(d) FROM doctorEntity d WHERE d.speciality = :speciality")
    Long countBySpeciality(@Param("speciality") Speciality speciality);

    /**
     * Count active doctors by sub-specialty
     */
    @Query("SELECT COUNT(d) FROM doctorEntity d WHERE d.isActive = true AND :subSpeciality MEMBER OF d.subSpecialities")
    Long countActiveBySubSpeciality(@Param("subSpeciality") SubSpeciality subSpeciality);

    /**
     * Count all active doctors
     */
    @Query("SELECT COUNT(d) FROM doctorEntity d WHERE d.isActive = true")
    Long countActiveDoctors();

    /**
     * Count all inactive doctors
     */
    @Query("SELECT COUNT(d) FROM doctorEntity d WHERE d.isActive = false")
    Long countInactiveDoctors();

    // ========== 6. Update Operations (Status Management) ==========

    /**
     * Deactivate a doctor (set isActive = false)
     */
    @Modifying
    @Query("UPDATE doctorEntity d SET d.isActive = false WHERE d.id = :doctorId")
    void deactivate(@Param("doctorId") Long doctorId);

    /**
     * Activate a doctor (set isActive = true)
     */
    @Modifying
    @Query("UPDATE doctorEntity d SET d.isActive = true WHERE d.id = :doctorId")
    void activate(@Param("doctorId") Long doctorId);

    // ========== 7. Existence Checks ==========

    /**
     * Check if a license number already exists
     */
    boolean existsByLicenseNumber(String licenseNumber);
}