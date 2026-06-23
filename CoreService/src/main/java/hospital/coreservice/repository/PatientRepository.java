package hospital.coreservice.repository;

import hospital.coreservice.model.Patient;
import hospital.coreservice.model.enums.BloodType;
import hospital.coreservice.model.enums.Gender;
import hospital.coreservice.model.enums.PatientStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends BaseEntityRepository<Patient, Long> {

    Optional<Patient> findByNationalId(String nationalId);

    Optional<Patient> findByPhoneNumber(String phoneNumber);

    @Query("select p from patientEntity p where p.user.id=:userId")
    Optional<Patient> findByUserId(@Param("userId") Long userId);

    List<Patient> findByFirstNameContainingIgnoreCase(String firstName);

    List<Patient> findByLastNameContainingIgnoreCase(String lastName);

    List<Patient> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);

    @Query("SELECT p FROM patientEntity p WHERE " +
            "(:nationalId IS NULL OR p.nationalId = :nationalId) AND " +
            "(:firstName IS NULL OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
            "(:lastName IS NULL OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
            "(:status IS NULL OR p.status = :status)")
    List<Patient> searchPatients(@Param("nationalId") String nationalId,
                                 @Param("firstName") String firstName,
                                 @Param("lastName") String lastName,
                                 @Param("status") PatientStatus status);

    List<Patient> findByCurrentRoomId(@Param("roomId") Long roomId);

    List<Patient> findByGender(Gender gender);

    List<Patient> findByBloodType(BloodType bloodType);

    List<Patient> findByStatus(PatientStatus status);

    Page<Patient> findByStatus(PatientStatus status, Pageable pageable);

    List<Patient> findByBirthDateBetween(LocalDate start, LocalDate end);

    @Modifying
    @Query("UPDATE patientEntity p SET p.status = :status WHERE p.id = :patientId")
    int updateStatus(@Param("patientId") Long patientId, @Param("status") PatientStatus status);

    default int archivePatient(Long patientId) {
        return updateStatus(patientId, PatientStatus.ARCHIVED);
    }

    default int activatePatient(Long patientId) {
        return updateStatus(patientId, PatientStatus.ACTIVE);
    }

    default void deactivatePatient(Long patientId) {
        updateStatus(patientId, PatientStatus.INACTIVE);
    }


    boolean existsByNationalId(String nationalId);

    boolean existsByPhoneNumber(String phoneNumber);

    long countByStatus(PatientStatus status);

    long countByGender(Gender gender);

    long countByBloodType(BloodType bloodType);
}