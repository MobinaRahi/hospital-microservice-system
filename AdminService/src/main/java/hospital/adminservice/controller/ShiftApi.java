package hospital.adminservice.controller;

import hospital.adminservice.dto.shift.ShiftCreateDto;
import hospital.adminservice.dto.shift.ShiftResponseDto;
import hospital.adminservice.dto.shift.ShiftUpdateDto;
import hospital.adminservice.dto.response.ApiResponse;
import hospital.adminservice.service.ShiftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Shift management.
 *
 * <p><strong>Access Control:</strong></p>
 * <ul>
 *   <li>Admin/HR: Full CRUD access</li>
 *   <li>Employee: Read access</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/admin/shifts")
@RequiredArgsConstructor
@Tag(name = "Shift Management", description = "Shift definition CRUD APIs")
public class ShiftApi {

    private final ShiftService shiftService;

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ══════════════════════════════════════════════════════════════════

    @PostMapping
    @Operation(summary = "Create a new shift")
    public ResponseEntity<ApiResponse<ShiftResponseDto>> createShift(@Valid @RequestBody ShiftCreateDto createDto) {
        ShiftResponseDto created = shiftService.createShift(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Shift created successfully", HttpStatus.CREATED.value()));
    }

    // ══════════════════════════════════════════════════════════════════
    // Read
    // ══════════════════════════════════════════════════════════════════

    @GetMapping("/{id}")
    @Operation(summary = "Get shift by ID")
    public ResponseEntity<ApiResponse<ShiftResponseDto>> getShiftById(@PathVariable Long id) {
        ShiftResponseDto shift = shiftService.getShiftById(id);
        return ResponseEntity.ok(ApiResponse.success(shift, "Shift retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get shift by code")
    public ResponseEntity<ApiResponse<ShiftResponseDto>> getShiftByCode(@PathVariable String code) {
        ShiftResponseDto shift = shiftService.getShiftByCode(code);
        return ResponseEntity.ok(ApiResponse.success(shift, "Shift retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active shifts")
    public ResponseEntity<ApiResponse<List<ShiftResponseDto>>> getAllActiveShifts() {
        List<ShiftResponseDto> shifts = shiftService.getAllActiveShifts();
        return ResponseEntity.ok(ApiResponse.success(shifts, "Active shifts retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping
    @Operation(summary = "Get all shifts")
    public ResponseEntity<ApiResponse<List<ShiftResponseDto>>> getAllShifts() {
        List<ShiftResponseDto> shifts = shiftService.getAllShifts();
        return ResponseEntity.ok(ApiResponse.success(shifts, "Shifts retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/night")
    @Operation(summary = "Get all night shifts")
    public ResponseEntity<ApiResponse<List<ShiftResponseDto>>> getNightShifts() {
        List<ShiftResponseDto> shifts = shiftService.getNightShifts();
        return ResponseEntity.ok(ApiResponse.success(shifts, "Night shifts retrieved successfully", HttpStatus.OK.value()));
    }

    // ═══════════════════════════════════════════════════════════════════
    // Update
    // ═══════════════════════════════════════════════════════════════════

    @PutMapping("/{id}")
    @Operation(summary = "Update a shift by ID")
    public ResponseEntity<ApiResponse<ShiftResponseDto>> updateShift(
            @PathVariable Long id,
            @Valid @RequestBody ShiftUpdateDto updateDto) {
        ShiftResponseDto updated = shiftService.updateShift(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Shift updated successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/toggle-active")
    @Operation(summary = "Toggle shift active status")
    public ResponseEntity<ApiResponse<ShiftResponseDto>> toggleActive(@PathVariable Long id) {
        ShiftResponseDto toggled = shiftService.toggleActive(id);
        return ResponseEntity.ok(ApiResponse.success(toggled, "Shift status toggled successfully", HttpStatus.OK.value()));
    }

    // ══════════════════════════════════════════════════════════════════
    // Delete
    // ═══════════════════════════════════════════════════════════════════

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a shift")
    public ResponseEntity<ApiResponse<Void>> deleteShift(@PathVariable Long id) {
        shiftService.deleteShift(id);
        return ResponseEntity.ok(ApiResponse.success("Shift deleted successfully", HttpStatus.OK.value()));
    }
}
