package hospital.coreservice.controller;

import hospital.coreservice.dto.response.ApiResponse;
import hospital.coreservice.dto.shift.ShiftCreateDto;
import hospital.coreservice.dto.shift.ShiftResponseDto;
import hospital.coreservice.dto.shift.ShiftUpdateDto;
import hospital.coreservice.service.ShiftService;
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
 * Handles CRUD for work shifts.
 *
 * @author Mobina
 */
@RestController
@RequestMapping("/api/v1/shift")
@RequiredArgsConstructor
@Tag(name = "Shift Management", description = "Shift CRUD and management APIs")
public class ShiftApi {

    private final ShiftService shiftService;

    // ========== Core Operations ==========

    @PostMapping
    @Operation(summary = "Create a new shift")
    public ResponseEntity<ApiResponse<ShiftResponseDto>> createShift(@Valid @RequestBody ShiftCreateDto shiftCreateDto) {
        ShiftResponseDto created = shiftService.createShift(shiftCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Shift created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a shift by ID")
    public ResponseEntity<ApiResponse<ShiftResponseDto>> updateShift(@PathVariable Long id, @Valid @RequestBody ShiftUpdateDto updateDto) {
        ShiftResponseDto updated = shiftService.updateShift(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Shift updated successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/deactivate/{id}")
    @Operation(summary = "Deactivate a shift (soft delete)")
    public ResponseEntity<ApiResponse<Void>> deactivateShift(@PathVariable Long id) {
        shiftService.deactivateShift(id);
        return ResponseEntity.ok(ApiResponse.success("Shift deactivated successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/activate/{id}")
    @Operation(summary = "Activate a shift")
    public ResponseEntity<ApiResponse<Void>> activateShift(@PathVariable Long id) {
        shiftService.activateShift(id);
        return ResponseEntity.ok(ApiResponse.success("Shift activated successfully", HttpStatus.OK.value()));
    }

    // ========== Basic Retrieval ==========

    @GetMapping("/{id}")
    @Operation(summary = "Get shift by ID")
    public ResponseEntity<ApiResponse<ShiftResponseDto>> getShiftById(@PathVariable Long id) {
        ShiftResponseDto shift = shiftService.getShiftById(id);
        return ResponseEntity.ok(ApiResponse.success(shift, "Shift retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-name")
    @Operation(summary = "Get shift by name")
    public ResponseEntity<ApiResponse<ShiftResponseDto>> getShiftByName(@RequestParam String name) {
        ShiftResponseDto shift = shiftService.getShiftByName(name);
        return ResponseEntity.ok(ApiResponse.success(shift, "Shift retrieved by name successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all shifts")
    public ResponseEntity<ApiResponse<List<ShiftResponseDto>>> getAllShifts() {
        List<ShiftResponseDto> shiftList = shiftService.getAllShifts();
        return ResponseEntity.ok(ApiResponse.success(shiftList, "All shifts retrieved successfully", HttpStatus.OK.value()));
    }

    // ========== Filters: By Type, Extra Pay, Active/Inactive ==========

    @GetMapping("/by-type")
    @Operation(summary = "Get shifts by type (night shift or not)")
    public ResponseEntity<ApiResponse<List<ShiftResponseDto>>> getShiftsByType(@RequestParam boolean nightShift) {
        List<ShiftResponseDto> shiftList = shiftService.getShiftsByType(nightShift);
        return ResponseEntity.ok(ApiResponse.success(shiftList, "Shifts filtered by type retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/by-type")
    @Operation(summary = "Get active shifts by type")
    public ResponseEntity<ApiResponse<List<ShiftResponseDto>>> getActiveShiftsByType(@RequestParam boolean nightShift) {
        List<ShiftResponseDto> shiftList = shiftService.getActiveShiftsByType(nightShift);
        return ResponseEntity.ok(ApiResponse.success(shiftList, "Active shifts filtered by type retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-extra-pay")
    @Operation(summary = "Get shifts that have extra pay")
    public ResponseEntity<ApiResponse<List<ShiftResponseDto>>> getShiftsWithExtraPay() {
        List<ShiftResponseDto> shiftList = shiftService.getShiftsWithExtraPay();
        return ResponseEntity.ok(ApiResponse.success(shiftList, "Shifts with extra pay retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/by-extra-pay")
    @Operation(summary = "Get active shifts that have extra pay")
    public ResponseEntity<ApiResponse<List<ShiftResponseDto>>> getActiveShiftsWithExtraPay() {
        List<ShiftResponseDto> shiftList = shiftService.getActiveShiftsWithExtraPay();
        return ResponseEntity.ok(ApiResponse.success(shiftList, "Active shifts with extra pay retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active shifts")
    public ResponseEntity<ApiResponse<List<ShiftResponseDto>>> getActiveShifts() {
        List<ShiftResponseDto> shiftList = shiftService.getActiveShifts();
        return ResponseEntity.ok(ApiResponse.success(shiftList, "Active shifts retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/inactive")
    @Operation(summary = "Get all inactive shifts")
    public ResponseEntity<ApiResponse<List<ShiftResponseDto>>> getInactiveShifts() {
        List<ShiftResponseDto> shiftList = shiftService.getInactiveShifts();
        return ResponseEntity.ok(ApiResponse.success(shiftList, "Inactive shifts retrieved successfully", HttpStatus.OK.value()));
    }

    // ========== Statistics (Counts) ==========

    @GetMapping("/count")
    @Operation(summary = "Count total shifts")
    public ResponseEntity<ApiResponse<Long>> countAllShifts() {
        Long count = shiftService.countAllShifts();
        return ResponseEntity.ok(ApiResponse.success(count, "Total shifts count retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/count")
    @Operation(summary = "Count active shifts")
    public ResponseEntity<ApiResponse<Long>> countActiveShifts() {
        Long count = shiftService.countActiveShifts();
        return ResponseEntity.ok(ApiResponse.success(count, "Active shifts count retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/inactive/count")
    @Operation(summary = "Count inactive shifts")
    public ResponseEntity<ApiResponse<Long>> countInactiveShifts() {
        Long count = shiftService.countInactiveShifts();
        return ResponseEntity.ok(ApiResponse.success(count, "Inactive shifts count retrieved successfully", HttpStatus.OK.value()));
    }
}