package hospital.coreservice.repository;

import hospital.coreservice.model.Doctor;
import hospital.coreservice.model.enums.Speciality;
import hospital.coreservice.model.enums.SubSpeciality;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Doctor entity.
 * Provides query methods by specialty, department, and license.
 *
 * @author Mobina
 */
@Repository
public interface DoctorRepository extends BaseEntityRepository<Doctor, Long> {

    Optional<Doctor> findByUserId(Long userId);

    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    List<Doctor> findByFirstNameContainingIgnoreCase(String firstName);


    List<Doctor> findByLastNameContainingIgnoreCase(String lastName);

    List<Doctor> findBySpeciality(Speciality speciality);

    List<Doctor> findByDepartmentId(Long departmentId);

    List<Doctor> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);

    @Query("SELECT d FROM doctorEntity d JOIN d.subSpecialities s WHERE s = :subSpeciality")
    List<Doctor> findBySubSpeciality(@Param("subSpeciality") SubSpeciality subSpeciality);

    List<Doctor> findByYearsOfExperienceBetween(int startYears, int endYears);

    @Query("SELECT d FROM doctorEntity d WHERE d.speciality = :speciality AND d.isActive = true")
    List<Doctor> findActiveBySpeciality(@Param("speciality") Speciality speciality);

    @Query("SELECT d FROM doctorEntity d JOIN d.subSpecialities s WHERE s = :subSpeciality AND d.isActive = true")
    List<Doctor> findActiveBySubSpeciality(@Param("subSpeciality") SubSpeciality subSpeciality);

    @Query("SELECT d FROM doctorEntity d WHERE d.department.id = :departmentId AND d.isActive = true")
    List<Doctor> findActiveByDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT d FROM doctorEntity d JOIN d.subSpecialities s WHERE s = :subSpeciality AND d.speciality = :speciality AND d.isActive = true")
    List<Doctor> findActiveDoctorsBySpecialityAndSubSpeciality(@Param("speciality") Speciality speciality, @Param("subSpeciality") SubSpeciality subSpeciality);

    @Query("SELECT d FROM doctorEntity d JOIN d.subSpecialities s WHERE s = :subSpeciality AND d.speciality = :speciality")
    List<Doctor> findBySpecialityAndSubSpeciality(@Param("speciality") Speciality speciality, @Param("subSpeciality") SubSpeciality subSpeciality);

    @Query("SELECT COUNT(d) FROM doctorEntity d WHERE d.department.id = :departmentId")
    Long countByDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT COUNT(d) FROM doctorEntity d WHERE d.speciality = :speciality")
    Long countBySpeciality(@Param("speciality") Speciality speciality);

    @Query("SELECT COUNT(d) FROM doctorEntity d WHERE d.isActive = true AND :subSpeciality MEMBER OF d.subSpecialities")
    Long countActiveBySubSpeciality(@Param("subSpeciality") SubSpeciality subSpeciality);

    boolean existsByLicenseNumber(String licenseNumber);

    @Query("SELECT d FROM doctorEntity d WHERE d.id = :id AND d.isActive = true")
    Optional<Doctor> findActiveById(@Param("id") Long id);

    @Query("SELECT d FROM doctorEntity d WHERE d.isActive = true")
    List<Doctor> findAllActive();

    @Query("SELECT d FROM doctorEntity d WHERE d.isActive = false")
    List<Doctor> findAllInactive();

    @Modifying
    @Query("UPDATE doctorEntity d SET d.isActive = false WHERE d.id = :id")
    void deactivate(@Param("id") Long id);

    @Modifying
    @Query("UPDATE doctorEntity d SET d.isActive = true WHERE d.id = :id")
    void activate(@Param("id") Long id);

    @Query("select count(d) from doctorEntity d where d.isActive=true ")
    Long countActive();

    @Query("select count(d) from doctorEntity d where d.isActive=false ")
    Long countInactive();
}