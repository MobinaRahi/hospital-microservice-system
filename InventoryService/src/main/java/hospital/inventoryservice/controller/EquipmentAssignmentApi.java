package hospital.inventoryservice.controller;

import hospital.inventoryservice.dto.equipmentassignment.EquipmentAssignmentCreateDto;
import hospital.inventoryservice.dto.equipmentassignment.EquipmentAssignmentResponseDto;
import hospital.inventoryservice.dto.equipmentassignment.EquipmentAssignmentUpdateDto;
import hospital.inventoryservice.dto.response.ApiResponse;
import hospital.inventoryservice.service.EquipmentAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for EquipmentAssignment management.
 * Provides APIs for assigning and returning medical equipment to patients/departments.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>An equipment can only be assigned if its status is AVAILABLE</li>
 *   <li>Assigning automatically changes equipment status to IN_USE</li>
 *   <li>Returning automatically changes equipment status back to AVAILABLE</li>
 *   <li>Overdue assignments are tracked</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/inventory/equipment-assignments")
@RequiredArgsConstructor
@Tag(name = "Equipment Assignment Management", description = "Equipment assignment and return APIs")
public class EquipmentAssignmentApi {

    // ==================== Dependencies ====================

    private final EquipmentAssignmentService assignmentService;

    // ==================== Create ====================

    /**
     * Assigns an equipment to a patient or department.
     *
     * <p><strong>Required fields:</strong> equipmentId, assignedDate</p>
     * <p><strong>Optional fields:</strong> patientId, departmentId, expectedReturnDate, notes</p>
     *
     * <p><strong>Rules:</strong></p>
     * <ul>
 *   <li>Either patientId or departmentId must be provided</li>
     *   <li>Equipment must be in AVAILABLE status</li>
     *   <li>Equipment status automatically changes to IN_USE</li>
     * </ul>
     */
    @PostMapping
    @Operation(summary = "Assign equipment to patient or department")
    public ResponseEntity<ApiResponse<EquipmentAssignmentResponseDto>> createAssignment(
            @Valid @RequestBody EquipmentAssignmentCreateDto createDto) {
        EquipmentAssignmentResponseDto created = assignmentService.createAssignment(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Equipment assigned successfully", HttpStatus.CREATED.value()));
    }

    // ==================== Update ====================

    /**
     * Updates an existing equipment assignment.
     * <p>Only provided fields will be updated.</p>
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an equipment assignment")
    public ResponseEntity<ApiResponse<EquipmentAssignmentResponseDto>> updateAssignment(
            @PathVariable Long id,
            @Valid @RequestBody EquipmentAssignmentUpdateDto updateDto) {
        EquipmentAssignmentResponseDto updated = assignmentService.updateAssignment(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Assignment updated successfully", HttpStatus.OK.value()));
    }

    // ==================== Return ====================

    /**
     * Returns an equipment (marks as returned with current timestamp).
     * <p>Equipment status automatically changes back to AVAILABLE.</p>
     */
    @PatchMapping("/{id}/return")
    @Operation(summary = "Return equipment (mark assignment as complete)")
    public ResponseEntity<ApiResponse<EquipmentAssignmentResponseDto>> returnEquipment(@PathVariable Long id) {
        EquipmentAssignmentResponseDto returned = assignmentService.returnEquipment(id);
        return ResponseEntity.ok(ApiResponse.success(returned, "Equipment returned successfully", HttpStatus.OK.value()));
    }

    /**
     * Returns an equipment with a specific return date.
     * Used for backdating returns.
     */
    @PatchMapping("/{id}/return/{returnDate}")
    @Operation(summary = "Return equipment with specific return date")
    public ResponseEntity<ApiResponse<EquipmentAssignmentResponseDto>> returnEquipmentWithDate(
            @PathVariable Long id,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime returnDate) {
        EquipmentAssignmentResponseDto returned = assignmentService.returnEquipment(id, returnDate);
        return ResponseEntity.ok(ApiResponse.success(returned, "Equipment returned successfully", HttpStatus.OK.value()));
    }

    // ==================== Read ====================

    /**
     * Gets an assignment by its ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get equipment assignment by ID")
    public ResponseEntity<ApiResponse<EquipmentAssignmentResponseDto>> getAssignmentById(@PathVariable Long id) {
        EquipmentAssignmentResponseDto assignment = assignmentService.getAssignmentById(id);
        return ResponseEntity.ok(ApiResponse.success(assignment, "Assignment retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets all active assignments (equipment not yet returned).
     * Used for tracking currently deployed equipment.
     */
    @GetMapping("/active")
    @Operation(summary = "Get all active assignments")
    public ResponseEntity<ApiResponse<List<EquipmentAssignmentResponseDto>>> getActiveAssignments() {
        List<EquipmentAssignmentResponseDto> assignments = assignmentService.getActiveAssignments();
        return ResponseEntity.ok(ApiResponse.success(assignments, "Active assignments retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets all assignments (including returned).
     * Used for audit and history.
     */
    @GetMapping
    @Operation(summary = "Get all equipment assignments")
    public ResponseEntity<ApiResponse<List<EquipmentAssignmentResponseDto>>> getAllAssignments() {
        List<EquipmentAssignmentResponseDto> assignments = assignmentService.getAllAssignments();
        return ResponseEntity.ok(ApiResponse.success(assignments, "Assignments retrieved successfully", HttpStatus.OK.value()));
    }

    // ==================== Filters ====================

    /**
     * Gets assignments for a specific equipment.
     */
    @GetMapping("/equipment/{equipmentId}")
    @Operation(summary = "Get assignments by equipment")
    public ResponseEntity<ApiResponse<List<EquipmentAssignmentResponseDto>>> getByEquipment(@PathVariable Long equipmentId) {
        List<EquipmentAssignmentResponseDto> assignments = assignmentService.getAssignmentsByEquipment(equipmentId);
        return ResponseEntity.ok(ApiResponse.success(assignments, "Assignments retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets assignments for a specific patient.
     * Used to see all equipment currently assigned to a patient.
     */
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get assignments by patient")
    public ResponseEntity<ApiResponse<List<EquipmentAssignmentResponseDto>>> getByPatient(@PathVariable Long patientId) {
        List<EquipmentAssignmentResponseDto> assignments = assignmentService.getAssignmentsByPatient(patientId);
        return ResponseEntity.ok(ApiResponse.success(assignments, "Assignments retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets assignments for a specific department.
     * Used for department-level equipment tracking.
     */
    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get assignments by department")
    public ResponseEntity<ApiResponse<List<EquipmentAssignmentResponseDto>>> getByDepartment(@PathVariable Long departmentId) {
        List<EquipmentAssignmentResponseDto> assignments = assignmentService.getAssignmentsByDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.success(assignments, "Assignments retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets overdue assignments.
     * Assignments where expected return date has passed but not returned.
     * Used for follow-up alerts.
     */
    @GetMapping("/overdue")
    @Operation(summary = "Get overdue assignments")
    public ResponseEntity<ApiResponse<List<EquipmentAssignmentResponseDto>>> getOverdueAssignments() {
        List<EquipmentAssignmentResponseDto> assignments = assignmentService.getOverdueAssignments();
        return ResponseEntity.ok(ApiResponse.success(assignments, "Overdue assignments retrieved successfully", HttpStatus.OK.value()));
    }

    // ==================== Delete ====================

    /**
     * Soft-deletes an assignment.
     * <p>Only allowed for returned assignments.</p>
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete an equipment assignment")
    public ResponseEntity<ApiResponse<Void>> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.ok(ApiResponse.success("Assignment deleted successfully", HttpStatus.OK.value()));
    }
}
