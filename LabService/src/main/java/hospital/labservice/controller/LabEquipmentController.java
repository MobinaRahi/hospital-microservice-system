package hospital.labservice.controller;

import hospital.labservice.dto.labequipment.LabEquipmentCreateDto;
import hospital.labservice.dto.labequipment.LabEquipmentResponseDto;
import hospital.labservice.dto.labequipment.LabEquipmentUpdateDto;
import hospital.labservice.model.enums.EquipmentStatus;
import hospital.labservice.service.LabEquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for LabEquipment management.
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/lab/equipment")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Lab Equipment", description = "Laboratory equipment tracking and maintenance")
public class LabEquipmentController {

    private final LabEquipmentService labEquipmentService;

    @PostMapping
    @Operation(summary = "Register new lab equipment")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Equipment created"),
            @ApiResponse(responseCode = "409", description = "Serial number already exists")
    })
    public ResponseEntity<LabEquipmentResponseDto> createEquipment(@Valid @RequestBody LabEquipmentCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(labEquipmentService.createEquipment(dto));
    }

    @GetMapping
    @Operation(summary = "Get all equipment with optional filters")
    public ResponseEntity<List<LabEquipmentResponseDto>> getAllEquipment(
            @Parameter(description = "Filter by status") @RequestParam(required = false) EquipmentStatus status,
            @Parameter(description = "Show needs calibration") @RequestParam(required = false, defaultValue = "false") Boolean needsCalibration,
            @Parameter(description = "Show available only") @RequestParam(required = false, defaultValue = "false") Boolean available) {

        if (Boolean.TRUE.equals(needsCalibration)) {
            return ResponseEntity.ok(labEquipmentService.getEquipmentNeedingCalibration());
        }
        if (Boolean.TRUE.equals(available)) {
            return ResponseEntity.ok(labEquipmentService.getAvailableEquipment());
        }
        if (status != null) {
            return ResponseEntity.ok(labEquipmentService.getEquipmentByStatus(status));
        }
        return ResponseEntity.ok(labEquipmentService.getAllEquipment());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get equipment by ID")
    public ResponseEntity<LabEquipmentResponseDto> getEquipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(labEquipmentService.getEquipmentById(id));
    }

    @GetMapping("/serial/{serialNumber}")
    @Operation(summary = "Get equipment by serial number")
    public ResponseEntity<LabEquipmentResponseDto> getEquipmentBySerialNumber(@PathVariable String serialNumber) {
        return ResponseEntity.ok(labEquipmentService.getEquipmentBySerialNumber(serialNumber));
    }

    @PutMapping("/{id}/calibrate")
    @Operation(summary = "Schedule calibration for equipment")
    public ResponseEntity<LabEquipmentResponseDto> scheduleCalibration(
            @PathVariable Long id,
            @Parameter(description = "Next calibration date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate nextDate) {
        return ResponseEntity.ok(labEquipmentService.scheduleCalibration(id, nextDate));
    }

    @PutMapping("/{id}/maintenance")
    @Operation(summary = "Mark equipment as under maintenance")
    public ResponseEntity<LabEquipmentResponseDto> markUnderMaintenance(@PathVariable Long id) {
        return ResponseEntity.ok(labEquipmentService.markUnderMaintenance(id));
    }

    @PutMapping("/{id}/operational")
    @Operation(summary = "Mark equipment as operational")
    public ResponseEntity<LabEquipmentResponseDto> markOperational(@PathVariable Long id) {
        return ResponseEntity.ok(labEquipmentService.markOperational(id));
    }

    @PutMapping("/{id}/broken")
    @Operation(summary = "Mark equipment as broken")
    public ResponseEntity<LabEquipmentResponseDto> markBroken(@PathVariable Long id) {
        return ResponseEntity.ok(labEquipmentService.markBroken(id));
    }

    @PutMapping("/{id}/decommission")
    @Operation(summary = "Decommission equipment (retire from service)")
    public ResponseEntity<LabEquipmentResponseDto> decommission(@PathVariable Long id) {
        return ResponseEntity.ok(labEquipmentService.decommission(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update equipment details")
    public ResponseEntity<LabEquipmentResponseDto> updateEquipment(
            @PathVariable Long id,
            @Valid @RequestBody LabEquipmentUpdateDto dto) {
        return ResponseEntity.ok(labEquipmentService.updateEquipment(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete equipment")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        labEquipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-serial")
    @Operation(summary = "Check if a serial number already exists")
    public ResponseEntity<Boolean> checkSerialExists(@RequestParam String serialNumber) {
        return ResponseEntity.ok(labEquipmentService.serialNumberExists(serialNumber));
    }
}
