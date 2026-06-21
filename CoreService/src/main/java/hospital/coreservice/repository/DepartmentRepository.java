package hospital.coreservice.repository;

import hospital.coreservice.model.Department;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DepartmentRepository extends BaseEntityRepository<Department, Long> {

    // ========== Find Methods ==========

    List<Department> findByDepartmentName(String departmentName);

    @Query("SELECT d FROM departmentEntity d WHERE d.departmentName=:departmentName AND d.isActive=true")
    List<Department> findByDepartmentNameAndIsActiveTrue(@Param("departmentName") String departmentName);

    List<Department> findByDepartmentNameContainingIgnoreCase(String departmentName);

    @Query("SELECT d FROM departmentEntity d WHERE LOWER(d.departmentName) LIKE LOWER(CONCAT('%', :name, '%')) AND d.isActive = true")
    List<Department> findByDepartmentNameContainingIgnoreCaseAndIsActiveTrue(@Param("name") String departmentName);

    Optional<Department> findByDepartmentCode(String departmentCode);

    @Query("SELECT d FROM departmentEntity d WHERE d.departmentCode=:departmentCode AND d.isActive=true")
    Optional<Department> findByDepartmentCodeAndIsActiveTrue(@Param("departmentCode") String departmentCode);

    List<Department> findByLocation(String location);

    @Query("SELECT d FROM departmentEntity d WHERE d.location=:location AND d.isActive=true")
    List<Department> findByLocationAndIsActiveTrue(@Param("location") String location);

    List<Department> findByLocationContainingIgnoreCase(String location);

    @Query("SELECT d FROM departmentEntity d WHERE LOWER(d.location) LIKE LOWER(CONCAT('%', :location, '%')) AND d.isActive = true")
    List<Department> findByLocationContainingIgnoreCaseAndIsActiveTrue(@Param("location") String location);

    // ========== Existence Checks ==========

    boolean existsByDepartmentCode(String code);

    boolean existsByDepartmentName(String name);

    @Query("SELECT d FROM departmentEntity d WHERE d.id = :id AND d.isActive = true")
    Optional<Department> findActiveById(@Param("id") Long id);

    @Query("SELECT d FROM departmentEntity d WHERE d.isActive = true")
    List<Department> findAllActive();

    @Query("SELECT d FROM departmentEntity d WHERE d.isActive = false")
    List<Department> findAllInactive();

    @Modifying
    @Query("UPDATE departmentEntity d SET d.isActive = false WHERE d.id = :id")
    void deactivate(@Param("id") Long id);

    @Modifying
    @Query("UPDATE departmentEntity d SET d.isActive = true WHERE d.id = :id")
    void activate(@Param("id") Long id);

    @Query("select count(d) from departmentEntity d where d.isActive=true ")
    Long countActive();

    @Query("select count(d) from departmentEntity d where d.isActive=false ")
    Long countInactive();
}
