package hospital.adminservice.controller;

import hospital.adminservice.dto.holiday.HolidayCreateDto;
import hospital.adminservice.dto.holiday.HolidayResponseDto;
import hospital.adminservice.dto.holiday.HolidayUpdateDto;
import hospital.adminservice.dto.response.ApiResponse;
import hospital.adminservice.service.HolidayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for Holiday management.
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
@RequestMapping("/api/v1/admin/holidays")
@RequiredArgsConstructor
@Tag(name = "Holiday Management", description = "Holiday CRUD and date checking APIs")
public class HolidayApi {

    private final HolidayService holidayService;

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ═════════════════════════════════════════════════════════════════

    @PostMapping
    @Operation(summary = "Create a new holiday")
    public ResponseEntity<ApiResponse<HolidayResponseDto>> createHoliday(@Valid @RequestBody HolidayCreateDto createDto) {
        HolidayResponseDto created = holidayService.createHoliday(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Holiday created successfully", HttpStatus.CREATED.value()));
    }

    // ══════════════════════════════════════════════════════════════════
    // Read
    // ══════════════════════════════════════════════════════════════════

    @GetMapping("/{id}")
    @Operation(summary = "Get holiday by ID")
    public ResponseEntity<ApiResponse<HolidayResponseDto>> getHolidayById(@PathVariable Long id) {
        HolidayResponseDto holiday = holidayService.getHolidayById(id);
        return ResponseEntity.ok(ApiResponse.success(holiday, "Holiday retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/year/{year}")
    @Operation(summary = "Get holidays by year")
    public ResponseEntity<ApiResponse<List<HolidayResponseDto>>> getHolidaysByYear(@PathVariable Integer year) {
        List<HolidayResponseDto> holidays = holidayService.getHolidaysByYear(year);
        return ResponseEntity.ok(ApiResponse.success(holidays, "Holidays retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active holidays")
    public ResponseEntity<ApiResponse<List<HolidayResponseDto>>> getActiveHolidays() {
        List<HolidayResponseDto> holidays = holidayService.getActiveHolidays();
        return ResponseEntity.ok(ApiResponse.success(holidays, "Active holidays retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/recurring")
    @Operation(summary = "Get all recurring holidays")
    public ResponseEntity<ApiResponse<List<HolidayResponseDto>>> getRecurringHolidays() {
        List<HolidayResponseDto> holidays = holidayService.getRecurringHolidays();
        return ResponseEntity.ok(ApiResponse.success(holidays, "Recurring holidays retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get holidays on a specific date")
    public ResponseEntity<ApiResponse<List<HolidayResponseDto>>> getHolidaysOnDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<HolidayResponseDto> holidays = holidayService.getHolidaysOnDate(date);
        return ResponseEntity.ok(ApiResponse.success(holidays, "Holidays retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/check/{date}")
    @Operation(summary = "Check if a date is a holiday")
    public ResponseEntity<ApiResponse<Boolean>> isHoliday(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        boolean isHoliday = holidayService.isHoliday(date);
        return ResponseEntity.ok(ApiResponse.success(isHoliday, "Holiday check completed", HttpStatus.OK.value()));
    }

    @GetMapping("/range")
    @Operation(summary = "Get holidays in a date range")
    public ResponseEntity<ApiResponse<List<HolidayResponseDto>>> getHolidaysByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<HolidayResponseDto> holidays = holidayService.getHolidaysByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(holidays, "Holidays retrieved successfully", HttpStatus.OK.value()));
    }

    // ══════════════════════════════════════════════════════════════════
    // Update
    // ═══════════════════════════════════════════════════════════════════

    @PutMapping("/{id}")
    @Operation(summary = "Update a holiday by ID")
    public ResponseEntity<ApiResponse<HolidayResponseDto>> updateHoliday(
            @PathVariable Long id,
            @Valid @RequestBody HolidayUpdateDto updateDto) {
        HolidayResponseDto updated = holidayService.updateHoliday(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Holiday updated successfully", HttpStatus.OK.value()));
    }

    // ══════════════════════════════════════════════════════════════════
    // Delete
    // ═════════════════════════════════════════════════════════════════

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a holiday")
    public ResponseEntity<ApiResponse<Void>> deleteHoliday(@PathVariable Long id) {
        holidayService.deleteHoliday(id);
        return ResponseEntity.ok(ApiResponse.success("Holiday deleted successfully", HttpStatus.OK.value()));
    }
}
