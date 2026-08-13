package hospital.adminservice.controller;

import hospital.adminservice.dto.bed.BedCreateDto;
import hospital.adminservice.dto.bed.BedResponseDto;
import hospital.adminservice.dto.bed.BedUpdateDto;
import hospital.adminservice.dto.response.ApiResponse;
import hospital.adminservice.model.enums.BedStatus;
import hospital.adminservice.model.enums.BedType;
import hospital.adminservice.service.BedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Bed management.
 *
 * <p><strong>Access Control:</strong></p>
 * <ul>
 *   <li>Admin/Nurse: Full CRUD access</li>
 *   <li>Doctor: Read access</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/admin/beds")
@RequiredArgsConstructor
@Tag(name = "Bed Management", description = "Bed CRUD and patient assignment APIs")
public class BedApi {

    private final BedService bedService;

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ══════════════════════════════════════════════════════════════════

    @PostMapping
    @Operation(summary = "Create a new bed")
    public ResponseEntity<ApiResponse<BedResponseDto>> createBed(@Valid @RequestBody BedCreateDto createDto) {
        BedResponseDto created = bedService.createBed(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Bed created successfully", HttpStatus.CREATED.value()));
    }

    // ═══════════════════════════════════════════════════════════════════
    // Read
    // ══════════════════════════════════════════════════════════════════

    @GetMapping("/{id}")
    @Operation(summary = "Get bed by ID")
    public ResponseEntity<ApiResponse<BedResponseDto>> getBedById(@PathVariable Long id) {
        BedResponseDto bed = bedService.getBedById(id);
        return ResponseEntity.ok(ApiResponse.success(bed, "Bed retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping
    @Operation(summary = "Get all beds")
    public ResponseEntity<ApiResponse<List<BedResponseDto>>> getAllBeds() {
        List<BedResponseDto> beds = bedService.getAllBeds();
        return ResponseEntity.ok(ApiResponse.success(beds, "Beds retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get beds by department")
    public ResponseEntity<ApiResponse<List<BedResponseDto>>> getBedsByDepartment(@PathVariable Long departmentId) {
        List<BedResponseDto> beds = bedService.getBedsByDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.success(beds, "Beds retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get beds by status")
    public ResponseEntity<ApiResponse<List<BedResponseDto>>> getBedsByStatus(@PathVariable BedStatus status) {
        List<BedResponseDto> beds = bedService.getBedsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(beds, "Beds retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get beds by type")
    public ResponseEntity<ApiResponse<List<BedResponseDto>>> getBedsByType(@PathVariable BedType type) {
        List<BedResponseDto> beds = bedService.getBedsByType(type);
        return ResponseEntity.ok(ApiResponse.success(beds, "Beds retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/department/{departmentId}/available")
    @Operation(summary = "Get available beds in a department")
    public ResponseEntity<ApiResponse<List<BedResponseDto>>> getAvailableBedsByDepartment(@PathVariable Long departmentId) {
        List<BedResponseDto> beds = bedService.getAvailableBedsByDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.success(beds, "Available beds retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/stats")
    @Operation(summary = "Get bed statistics")
    public ResponseEntity<ApiResponse<String>> getBedStats() {
        long available = bedService.countAvailableBeds();
        long occupied = bedService.countOccupiedBeds();
        String stats = String.format("Available: %d, Occupied: %d", available, occupied);
        return ResponseEntity.ok(ApiResponse.success(stats, "Bed statistics retrieved successfully", HttpStatus.OK.value()));
    }

    // ══════════════════════════════════════════════════════════════════
    // Patient Assignment
    // ══════════════════════════════════════════════════════════════════

    @PostMapping("/{id}/assign")
    @Operation(summary = "Assign bed to a patient")
    public ResponseEntity<ApiResponse<BedResponseDto>> assignToPatient(
            @PathVariable Long id,
            @RequestParam Long patientId,
            @RequestParam Long admissionId) {
        BedResponseDto bed = bedService.assignToPatient(id, patientId, admissionId);
        return ResponseEntity.ok(ApiResponse.success(bed, "Bed assigned to patient successfully", HttpStatus.OK.value()));
    }

    @PostMapping("/{id}/discharge")
    @Operation(summary = "Discharge patient from bed")
    public ResponseEntity<ApiResponse<BedResponseDto>> dischargePatient(@PathVariable Long id) {
        BedResponseDto bed = bedService.dischargePatient(id);
        return ResponseEntity.ok(ApiResponse.success(bed, "Patient discharged from bed successfully", HttpStatus.OK.value()));
    }

    // ═══════════════════════════════════════════════════════════════════
    // Update
    // ══════════════════════════════════════════════════════════════════

    @PutMapping("/{id}")
    @Operation(summary = "Update a bed by ID")
    public ResponseEntity<ApiResponse<BedResponseDto>> updateBed(
            @PathVariable Long id,
            @Valid @RequestBody BedUpdateDto updateDto) {
        BedResponseDto updated = bedService.updateBed(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Bed updated successfully", HttpStatus.OK.value()));
    }

    // ══════════════════════════════════════════════════════════════════
    // Delete
    // ══════════════════════════════════════════════════════════════════

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a bed")
    public ResponseEntity<ApiResponse<Void>> deleteBed(@PathVariable Long id) {
        bedService.deleteBed(id);
        return ResponseEntity.ok(ApiResponse.success("Bed deleted successfully", HttpStatus.OK.value()));
    }
}
