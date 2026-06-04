package hospital.coreservice.controller;

import hospital.coreservice.dto.api.ApiResponse;
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

@RestController
@RequestMapping("/api/v1/shift")
@RequiredArgsConstructor
@Tag(name = "Shift Management", description = "Shift CRUD and management APIs")
public class ShiftApi {

    private final ShiftService shiftService;

    // ========== Core Operations ==========

    @PostMapping
    @Operation(summary = "Create a new shift")
    public ResponseEntity<ApiResponse> createShift(@Valid @RequestBody ShiftCreateDto shiftCreateDto) {
        ShiftResponseDto created = shiftService.createShift(shiftCreateDto);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED.value())
                .message("Shift created successfully")
                .data(created)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a shift by ID")
    public ResponseEntity<ApiResponse> updateShift(@PathVariable Long id, @Valid @RequestBody ShiftUpdateDto updateDto) {
        ShiftResponseDto updated = shiftService.updateShift(id, updateDto);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Shift updated successfully")
                .data(updated)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/deactivate/{id}")
    @Operation(summary = "Deactivate a shift (soft delete)")
    public ResponseEntity<ApiResponse> deactivateShift(@PathVariable Long id) {
        shiftService.deactivateShift(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Shift deactivated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/activate/{id}")
    @Operation(summary = "Activate a shift")
    public ResponseEntity<ApiResponse> activateShift(@PathVariable Long id) {
        shiftService.activateShift(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Shift activated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Basic Retrieval ==========

    @GetMapping("/{id}")
    @Operation(summary = "Get shift by ID")
    public ResponseEntity<ApiResponse> getShiftById(@PathVariable Long id) {
        ShiftResponseDto shift = shiftService.getShiftById(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Shift retrieved successfully")
                .data(shift)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-name")
    @Operation(summary = "Get shift by name")
    public ResponseEntity<ApiResponse> getShiftByName(@RequestParam String name) {
        ShiftResponseDto shift = shiftService.getShiftByName(name);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Shift retrieved by name successfully")
                .data(shift)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all shifts")
    public ResponseEntity<ApiResponse> getAllShifts() {
        List<ShiftResponseDto> shiftList = shiftService.getAllShifts();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("All shifts retrieved successfully")
                .data(shiftList)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Filters: By Type, Extra Pay, Active/Inactive ==========

    @GetMapping("/by-type")
    @Operation(summary = "Get shifts by type (night shift or not)")
    public ResponseEntity<ApiResponse> getShiftsByType(@RequestParam boolean nightShift) {
        List<ShiftResponseDto> shiftList = shiftService.getShiftsByType(nightShift);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Shifts filtered by type retrieved successfully")
                .data(shiftList)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/by-type")
    @Operation(summary = "Get active shifts by type")
    public ResponseEntity<ApiResponse> getActiveShiftsByType(@RequestParam boolean nightShift) {
        List<ShiftResponseDto> shiftList = shiftService.getActiveShiftsByType(nightShift);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active shifts filtered by type retrieved successfully")
                .data(shiftList)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-extra-pay")
    @Operation(summary = "Get shifts that have extra pay")
    public ResponseEntity<ApiResponse> getShiftsWithExtraPay() {
        List<ShiftResponseDto> shiftList = shiftService.getShiftsWithExtraPay();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Shifts with extra pay retrieved successfully")
                .data(shiftList)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/by-extra-pay")
    @Operation(summary = "Get active shifts that have extra pay")
    public ResponseEntity<ApiResponse> getActiveShiftsWithExtraPay() {
        List<ShiftResponseDto> shiftList = shiftService.getActiveShiftsWithExtraPay();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active shifts with extra pay retrieved successfully")
                .data(shiftList)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active shifts")
    public ResponseEntity<ApiResponse> getActiveShifts() {
        List<ShiftResponseDto> shiftList = shiftService.getActiveShifts();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active shifts retrieved successfully")
                .data(shiftList)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inactive")
    @Operation(summary = "Get all inactive shifts")
    public ResponseEntity<ApiResponse> getInactiveShifts() {
        List<ShiftResponseDto> shiftList = shiftService.getInactiveShifts();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Inactive shifts retrieved successfully")
                .data(shiftList)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Statistics (Counts) ==========

    @GetMapping("/count")
    @Operation(summary = "Count total shifts")
    public ResponseEntity<ApiResponse> countAllShifts() {
        Long count = shiftService.countAllShifts();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Total shifts count retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/count")
    @Operation(summary = "Count active shifts")
    public ResponseEntity<ApiResponse> countActiveShifts() {
        Long count = shiftService.countActiveShifts();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active shifts count retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inactive/count")
    @Operation(summary = "Count inactive shifts")
    public ResponseEntity<ApiResponse> countInactiveShifts() {
        Long count = shiftService.countInactiveShifts();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Inactive shifts count retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }
}