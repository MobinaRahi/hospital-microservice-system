package hospital.labservice.controller;

import hospital.labservice.dto.labtechnician.LabTechnicianCreateDto;
import hospital.labservice.dto.labtechnician.LabTechnicianResponseDto;
import hospital.labservice.dto.labtechnician.LabTechnicianUpdateDto;
import hospital.labservice.model.enums.LabShift;
import hospital.labservice.service.LabTechnicianService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for LabTechnician management.
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/lab/technicians")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Lab Technicians", description = "Laboratory technician management")
public class LabTechnicianController {

    private final LabTechnicianService labTechnicianService;

    @PostMapping
    @Operation(summary = "Register a new lab technician")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Technician created"),
            @ApiResponse(responseCode = "409", description = "User ID or employee code already exists")
    })
    public ResponseEntity<LabTechnicianResponseDto> createTechnician(@Valid @RequestBody LabTechnicianCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(labTechnicianService.createTechnician(dto));
    }

    @GetMapping
    @Operation(summary = "Get all technicians with optional filters")
    public ResponseEntity<List<LabTechnicianResponseDto>> getAllTechnicians(
            @Parameter(description = "Filter by shift") @RequestParam(required = false) LabShift shift,
            @Parameter(description = "Show active only") @RequestParam(required = false, defaultValue = "false") Boolean activeOnly,
            @Parameter(description = "Filter by specialization") @RequestParam(required = false) String specialization) {

        if (shift != null && Boolean.TRUE.equals(activeOnly)) {
            return ResponseEntity.ok(labTechnicianService.getActiveTechniciansByShift(shift));
        }
        if (shift != null) {
            return ResponseEntity.ok(labTechnicianService.getTechniciansByShift(shift));
        }
        if (Boolean.TRUE.equals(activeOnly)) {
            return ResponseEntity.ok(labTechnicianService.getActiveTechnicians());
        }
        if (specialization != null) {
            return ResponseEntity.ok(labTechnicianService.getTechniciansBySpecialization(specialization));
        }
        return ResponseEntity.ok(labTechnicianService.getAllTechnicians());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a technician by ID")
    public ResponseEntity<LabTechnicianResponseDto> getTechnicianById(@PathVariable Long id) {
        return ResponseEntity.ok(labTechnicianService.getTechnicianById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get a technician by AuthService user ID")
    public ResponseEntity<LabTechnicianResponseDto> getTechnicianByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(labTechnicianService.getTechnicianByUserId(userId));
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate a technician")
    public ResponseEntity<LabTechnicianResponseDto> activateTechnician(@PathVariable Long id) {
        return ResponseEntity.ok(labTechnicianService.activateTechnician(id));
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a technician")
    public ResponseEntity<LabTechnicianResponseDto> deactivateTechnician(@PathVariable Long id) {
        return ResponseEntity.ok(labTechnicianService.deactivateTechnician(id));
    }

    @PutMapping("/{id}/shift")
    @Operation(summary = "Change technician's shift assignment")
    public ResponseEntity<LabTechnicianResponseDto> changeShift(
            @PathVariable Long id,
            @RequestParam LabShift newShift) {
        return ResponseEntity.ok(labTechnicianService.changeShift(id, newShift));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update technician details")
    public ResponseEntity<LabTechnicianResponseDto> updateTechnician(
            @PathVariable Long id,
            @Valid @RequestBody LabTechnicianUpdateDto dto) {
        return ResponseEntity.ok(labTechnicianService.updateTechnician(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a technician")
    public ResponseEntity<Void> deleteTechnician(@PathVariable Long id) {
        labTechnicianService.deleteTechnician(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-user")
    @Operation(summary = "Check if a user ID is already linked to a technician")
    public ResponseEntity<Boolean> checkUserExists(@RequestParam Long userId) {
        return ResponseEntity.ok(labTechnicianService.userIdExists(userId));
    }

    @GetMapping("/check-employee-code")
    @Operation(summary = "Check if an employee code already exists")
    public ResponseEntity<Boolean> checkEmployeeCodeExists(@RequestParam String employeeCode) {
        return ResponseEntity.ok(labTechnicianService.employeeCodeExists(employeeCode));
    }
}
