package hospital.inventoryservice.service;

import hospital.inventoryservice.dto.equipmentassignment.EquipmentAssignmentCreateDto;
import hospital.inventoryservice.dto.equipmentassignment.EquipmentAssignmentResponseDto;
import hospital.inventoryservice.dto.equipmentassignment.EquipmentAssignmentUpdateDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for EquipmentAssignment management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>An equipment can only be assigned if its status is AVAILABLE</li>
 *   <li>An equipment cannot be assigned to both patient and department simultaneously</li>
 *   <li>Assigning an equipment changes its status to IN_USE</li>
 *   <li>Returning an equipment changes its status back to AVAILABLE</li>
 *   <li>Overdue assignments are tracked</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface EquipmentAssignmentService {

    // ════════════════════════════════════════════════════════════════════
    // Create
    // ════════════════════════════════════════════════════════════════════

    /**
     * Assigns an equipment to a patient or department.
     *
     * @param dto the assignment creation data
     * @return the created assignment
     */
    EquipmentAssignmentResponseDto createAssignment(EquipmentAssignmentCreateDto dto);

    // ═══════════════════════════════════════════════════════════════════
    // Read
    // ════════════════════════════════════════════════════════════════════

    /**
     * Gets an assignment by its ID.
     *
     * @param id the assignment ID
     * @return the assignment
     */
    EquipmentAssignmentResponseDto getAssignmentById(Long id);

    /**
     * Gets all active assignments (not returned).
     *
     * @return list of active assignments
     */
    List<EquipmentAssignmentResponseDto> getActiveAssignments();

    /**
     * Gets all assignments.
     *
     * @return list of all assignments
     */
    List<EquipmentAssignmentResponseDto> getAllAssignments();

    /**
     * Gets assignments for a specific equipment.
     *
     * @param equipmentId the equipment ID
     * @return list of assignments
     */
    List<EquipmentAssignmentResponseDto> getAssignmentsByEquipment(Long equipmentId);

    /**
     * Gets assignments for a specific patient.
     *
     * @param patientId the patient ID
     * @return list of assignments
     */
    List<EquipmentAssignmentResponseDto> getAssignmentsByPatient(Long patientId);

    /**
     * Gets assignments for a specific department.
     *
     * @param departmentId the department ID
     * @return list of assignments
     */
    List<EquipmentAssignmentResponseDto> getAssignmentsByDepartment(Long departmentId);

    /**
     * Gets overdue assignments (expected return passed, not returned).
     *
     * @return list of overdue assignments
     */
    List<EquipmentAssignmentResponseDto> getOverdueAssignments();

    // ════════════════════════════════════════════════════════════════════
    // Update
    // ════════════════════════════════════════════════════════════════════

    /**
     * Updates an existing assignment.
     * <p>Only provided fields in the DTO will be updated.</p>
     *
     * @param id  the assignment ID
     * @param dto the update data
     * @return the updated assignment
     */
    EquipmentAssignmentResponseDto updateAssignment(Long id, EquipmentAssignmentUpdateDto dto);

    /**
     * Returns an equipment (sets actualReturnDate).
     * <p>Equipment status is changed back to AVAILABLE.</p>
     *
     * @param id the assignment ID
     * @return the updated assignment
     */
    EquipmentAssignmentResponseDto returnEquipment(Long id);

    /**
     * Returns an equipment with a specific return date.
     *
     * @param id         the assignment ID
     * @param returnDate the actual return date
     * @return the updated assignment
     */
    EquipmentAssignmentResponseDto returnEquipment(Long id, LocalDateTime returnDate);

    // ════════════════════════════════════════════════════════════════════
    // Delete
    // ════════════════════════════════════════════════════════════════════

    /**
     * Soft-deletes an assignment.
     *
     * @param id the assignment ID
     */
    void deleteAssignment(Long id);
}
