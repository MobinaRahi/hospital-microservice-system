package hospital.coreservice.repository;

import hospital.coreservice.model.Nurse;
import hospital.coreservice.model.enums.NursePosition;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NurseRepository extends BaseEntityRepository<Nurse, Long> {

    Optional<Nurse> findByUserId(Long userId);

    Optional<Nurse> findByNationalId(String nationalId);

    Optional<Nurse> findByNurseCode(String nurseCode);

    Optional<Nurse> findByPhoneNumber(String phoneNumber);

    List<Nurse> findByFirstNameContainingIgnoreCase(String firstName);
    @Query("SELECT n FROM nurseEntity n WHERE LOWER(n.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) AND n.isActive = true")
    List<Nurse> findByFirstNameContainingIgnoreCaseAndActiveTrue(@Param("firstName") String firstName);

    List<Nurse> findByLastNameContainingIgnoreCase(String lastName);
    @Query("SELECT n FROM nurseEntity n WHERE LOWER(n.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')) AND n.isActive = true")
    List<Nurse> findByLastNameContainingIgnoreCaseAndActiveTrue(@Param("lastName") String lastName);

    List<Nurse> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);
    @Query("SELECT n FROM nurseEntity n WHERE LOWER(n.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) AND LOWER(n.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')) AND n.isActive = true")
    List<Nurse> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndActiveTrue(@Param("firstName") String firstName, @Param("lastName") String lastName);    // ========== Find by Experience ==========

    List<Nurse> findByYearsOfExperienceBetween(int start, int end);
    @Query("SELECT n FROM nurseEntity n WHERE n.yearsOfExperience BETWEEN :start AND :end AND n.isActive = true")
    List<Nurse> findByYearsOfExperienceBetweenAndActiveTrue(@Param("start") int start, @Param("end") int end);

    List<Nurse> findByPosition(NursePosition position);
    @Query("SELECT n FROM nurseEntity n WHERE n.position = :position AND n.isActive = true")
    List<Nurse> findByPositionAndActiveTrue(@Param("position") NursePosition position);

    @Query("SELECT n FROM nurseEntity n JOIN n.departmentList d WHERE d.id = :departmentId")
    List<Nurse> findByDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT n FROM nurseEntity n JOIN n.departmentList d WHERE d.id = :departmentId AND n.isActive = true")
    List<Nurse> findActiveNursesByDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT n FROM nurseEntity n LEFT JOIN FETCH n.shiftPreferenceList WHERE n.id = :nurseId")
    Optional<Nurse> findByIdWithShifts(@Param("nurseId") Long nurseId);

    boolean existsByNationalId(String nationalId);

    boolean existsByUserId(Long userId);

    boolean existsByNurseCode(String nurseCode);

    @Query("SELECT COUNT(n) FROM nurseEntity n JOIN n.departmentList d WHERE d.id = :departmentId")
    Long countByDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT COUNT(n) FROM nurseEntity n WHERE n.position = :position")
    Long countByPosition(@Param("position") NursePosition position);

    @Query("SELECT n FROM nurseEntity n WHERE n.id = :id AND n.isActive = true")
    Optional<Nurse> findActiveById(@Param("id") Long id);

    @Query("SELECT n FROM nurseEntity n WHERE n.isActive = true")
    List<Nurse> findAllActive();

    @Query("SELECT n FROM nurseEntity n WHERE n.isActive = false")
    List<Nurse> findAllInactive();

    @Modifying
    @Query("UPDATE nurseEntity n SET n.isActive = false WHERE n.id = :id")
    void deactivate(@Param("id") Long id);

    @Modifying
    @Query("UPDATE nurseEntity n SET n.isActive = true WHERE n.id = :id")
    void activate(@Param("id") Long id);

    @Query("select count(n) from nurseEntity n where n.isActive=true ")
    Long countActive();

    @Query("select count(n) from nurseEntity n where n.isActive=false ")
    Long countInactive();
}
