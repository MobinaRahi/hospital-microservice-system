package hospital.coreservice.repository;

import com.hospital.coreService.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Department entity.
 *
 * @author Mobina
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // ========== Update Operations ==========

    @Modifying
    @Query("UPDATE departmentEntity d SET d.isActive = false WHERE d.id = :departmentId")
    void deactivate(@Param("departmentId") Long departmentId);

    @Modifying
    @Query("UPDATE departmentEntity d SET d.isActive = true WHERE d.id = :departmentId")
    void activate(@Param("departmentId") Long departmentId);

    // ========== Find Methods ==========

    List<Department> findByDepartmentName(String departmentName);

    List<Department> findByDepartmentNameContainingIgnoreCase(String departmentName);

    Optional<Department> findByDepartmentCode(String departmentCode);

    List<Department> findByLocation(String location);

    List<Department> findByLocationContainingIgnoreCase(String location);

    // ========== Status Based ==========

    List<Department> findByIsActiveTrue();

    List<Department> findByIsActiveFalse();

    // ========== Count Methods ==========

    long countByIsActiveTrue();

    long countByIsActiveFalse();

    // ========== Existence Checks ==========

    boolean existsByDepartmentCode(String code);

    boolean existsByDepartmentName(String name);
}