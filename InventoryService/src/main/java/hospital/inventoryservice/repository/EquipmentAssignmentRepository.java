package hospital.inventoryservice.repository;

import hospital.inventoryservice.repository.BaseEntityRepository;

import hospital.inventoryservice.model.EquipmentAssignment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for EquipmentAssignment entity.
 * 
 * <p><strong>Query Methods:</strong></p>
 * <ul>
 *   <li>{@code findByEquipmentIdAndActualReturnDateIsNull(Long)} - Active assignments for an equipment</li>
 *   <li>{@code findByPatientId(Long)} - All assignments for a patient</li>
 *   <li>{@code findByDepartmentId(Long)} - All assignments for a department</li>
 *   <li>{@code findByExpectedReturnDateBefore(LocalDateTime)} - Overdue returns</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface EquipmentAssignmentRepository extends BaseEntityRepository<EquipmentAssignment, Long> {
    
    /**
     * Finds active assignments for an equipment (not yet returned).
     *
     * @param equipmentId the equipment ID
     * @return list of active assignments
     */
    List<EquipmentAssignment> findByEquipmentIdAndActualReturnDateIsNull(Long equipmentId);
    
    /**
     * Finds all assignments for a patient.
     *
     * @param patientId the patient ID
     * @return list of assignments for the patient
     */
    List<EquipmentAssignment> findByPatientId(Long patientId);
    
    /**
     * Finds all assignments for a department.
     *
     * @param departmentId the department ID
     * @return list of assignments for the department
     */
    List<EquipmentAssignment> findByDepartmentId(Long departmentId);
    
    /**
     * Finds overdue assignments (expected return date passed but not returned).
     *
     * @param date the date to check (usually now)
     * @return list of overdue assignments
     */
    List<EquipmentAssignment> findByExpectedReturnDateBeforeAndActualReturnDateIsNull(LocalDateTime date);
    
    /**
     * Finds assignments by assigned user.
     *
     * @param assignedBy the user ID who assigned
     * @return list of assignments by the user
     */
    List<EquipmentAssignment> findByAssignedBy(Long assignedBy);
    
    /**
     * Finds assignments in a date range.
     *
     * @param startDate start of the range
     * @param endDate end of the range
     * @return list of assignments in the range
     */
    List<EquipmentAssignment> findByAssignedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Custom query to get assignment statistics by department.
     *
     * @return list of department and assignment count
     */
    @Query("SELECT ea.departmentId, COUNT(ea) FROM EquipmentAssignment ea WHERE ea.departmentId IS NOT NULL GROUP BY ea.departmentId")
    List<Object[]> countAssignmentsByDepartment();
    
    /**
     * Custom query to get assignment statistics by equipment type.
     *
     * @return list of equipment type and assignment count
     */
    @Query("SELECT e.type, COUNT(ea) FROM EquipmentAssignment ea JOIN ea.equipment e GROUP BY e.type")
    List<Object[]> countAssignmentsByEquipmentType();
}
