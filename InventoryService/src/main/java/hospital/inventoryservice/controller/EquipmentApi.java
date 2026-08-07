package hospital.inventoryservice.controller;

import hospital.inventoryservice.dto.equipment.EquipmentCreateDto;
import hospital.inventoryservice.dto.equipment.EquipmentResponseDto;
import hospital.inventoryservice.dto.equipment.EquipmentUpdateDto;
import hospital.inventoryservice.dto.response.ApiResponse;
import hospital.inventoryservice.model.enums.EquipmentStatus;
import hospital.inventoryservice.model.enums.EquipmentType;
import hospital.inventoryservice.service.EquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Equipment management.
 * Provides CRUD operations for medical equipment tracking.
 *
 * <p><strong>Access Control:</strong></p>
 * <ul>
 *   <li>Admin: Full CRUD access</li>
 *   <li>Nurses: Read access for equipment tracking</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/inventory/equipment")
@RequiredArgsConstructor
@Tag(name = "Equipment Management", description = "Medical equipment CRUD and tracking APIs")
public class EquipmentApi {

    // ==================== Dependencies ====================

    private final EquipmentService equipmentService;

    // ==================== Create ====================

    /**
     * Creates a new equipment record.
     *
     * <p><strong>Required fields:</strong> name, type, serialNumber</p>
     * <p><strong>Optional fields:</strong> model, manufacturer, purchaseDate, warrantyExpiry, currentLocation</p>
     *
     * <p><strong>Validations:</strong></p>
     * <ul>
     *   <li>Serial number must be unique</li>
     *   <li>Equipment is created with AVAILABLE status by default</li>
     * </ul>
     */
    @PostMapping
    @Operation(summary = "Create a new equipment")
    public ResponseEntity<ApiResponse<EquipmentResponseDto>> createEquipment(
            @Valid @RequestBody EquipmentCreateDto createDto) {
        EquipmentResponseDto created = equipmentService.createEquipment(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Equipment created successfully", HttpStatus.CREATED.value()));
    }

    // ==================== Update ====================

    /**
     * Updates an existing equipment record.
     * <p>Only provided fields will be updated.</p>
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update equipment by ID")
    public ResponseEntity<ApiResponse<EquipmentResponseDto>> updateEquipment(
            @PathVariable Long id,
            @Valid @RequestBody EquipmentUpdateDto updateDto) {
        EquipmentResponseDto updated = equipmentService.updateEquipment(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Equipment updated successfully", HttpStatus.OK.value()));
    }

    /**
     * Changes the status of an equipment.
     * Used for manual status updates (e.g., marking as under maintenance).
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "Change equipment status")
    public ResponseEntity<ApiResponse<EquipmentResponseDto>> changeStatus(
            @PathVariable Long id,
            @RequestParam EquipmentStatus status) {
        EquipmentResponseDto updated = equipmentService.changeStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(updated, "Equipment status changed successfully", HttpStatus.OK.value()));
    }

    /**
     * Toggles the active status of an equipment.
     * Inactive equipment is hidden from assignment lists.
     */
    @PatchMapping("/{id}/toggle-active")
    @Operation(summary = "Toggle equipment active status")
    public ResponseEntity<ApiResponse<EquipmentResponseDto>> toggleActive(@PathVariable Long id) {
        EquipmentResponseDto toggled = equipmentService.toggleActive(id);
        return ResponseEntity.ok(ApiResponse.success(toggled, "Equipment status toggled successfully", HttpStatus.OK.value()));
    }

    // ==================== Read ====================

    /**
     * Gets an equipment by its ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get equipment by ID")
    public ResponseEntity<ApiResponse<EquipmentResponseDto>> getEquipmentById(@PathVariable Long id) {
        EquipmentResponseDto equipment = equipmentService.getEquipmentById(id);
        return ResponseEntity.ok(ApiResponse.success(equipment, "Equipment retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets an equipment by its serial number.
     * Used for barcode/serial scanning.
     */
    @GetMapping("/serial/{serialNumber}")
    @Operation(summary = "Get equipment by serial number")
    public ResponseEntity<ApiResponse<EquipmentResponseDto>> getEquipmentBySerialNumber(@PathVariable String serialNumber) {
        EquipmentResponseDto equipment = equipmentService.getEquipmentBySerialNumber(serialNumber);
        return ResponseEntity.ok(ApiResponse.success(equipment, "Equipment retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets all active equipment.
     */
    @GetMapping("/active")
    @Operation(summary = "Get all active equipment")
    public ResponseEntity<ApiResponse<List<EquipmentResponseDto>>> getAllActiveEquipment() {
        List<EquipmentResponseDto> equipment = equipmentService.getAllActiveEquipment();
        return ResponseEntity.ok(ApiResponse.success(equipment, "Active equipment retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets all equipment (including inactive).
     * Used for admin views.
     */
    @GetMapping
    @Operation(summary = "Get all equipment")
    public ResponseEntity<ApiResponse<List<EquipmentResponseDto>>> getAllEquipment() {
        List<EquipmentResponseDto> equipment = equipmentService.getAllEquipment();
        return ResponseEntity.ok(ApiResponse.success(equipment, "Equipment retrieved successfully", HttpStatus.OK.value()));
    }

    // ==================== Filters ====================

    /**
     * Gets equipment by type (BED, VENTILATOR, MONITOR, etc.).
     */
    @GetMapping("/type/{type}")
    @Operation(summary = "Get equipment by type")
    public ResponseEntity<ApiResponse<List<EquipmentResponseDto>>> getByType(@PathVariable EquipmentType type) {
        List<EquipmentResponseDto> equipment = equipmentService.getEquipmentByType(type);
        return ResponseEntity.ok(ApiResponse.success(equipment, "Equipment retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets equipment by status (AVAILABLE, IN_USE, MAINTENANCE, etc.).
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get equipment by status")
    public ResponseEntity<ApiResponse<List<EquipmentResponseDto>>> getByStatus(@PathVariable EquipmentStatus status) {
        List<EquipmentResponseDto> equipment = equipmentService.getEquipmentByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(equipment, "Equipment retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets available equipment (ready for assignment).
     * Used for the assignment UI dropdown.
     */
    @GetMapping("/available")
    @Operation(summary = "Get available equipment")
    public ResponseEntity<ApiResponse<List<EquipmentResponseDto>>> getAvailableEquipment() {
        List<EquipmentResponseDto> equipment = equipmentService.getAvailableEquipment();
        return ResponseEntity.ok(ApiResponse.success(equipment, "Available equipment retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets equipment with expired warranty.
     * Used for maintenance and budget planning alerts.
     */
    @GetMapping("/warranty-expired")
    @Operation(summary = "Get equipment with expired warranty")
    public ResponseEntity<ApiResponse<List<EquipmentResponseDto>>> getExpiredWarranty() {
        List<EquipmentResponseDto> equipment = equipmentService.getEquipmentWithExpiredWarranty();
        return ResponseEntity.ok(ApiResponse.success(equipment, "Equipment with expired warranty retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Searches equipment by location.
     */
    @GetMapping("/location/{location}")
    @Operation(summary = "Search equipment by location")
    public ResponseEntity<ApiResponse<List<EquipmentResponseDto>>> searchByLocation(@PathVariable String location) {
        List<EquipmentResponseDto> equipment = equipmentService.searchByLocation(location);
        return ResponseEntity.ok(ApiResponse.success(equipment, "Equipment retrieved successfully", HttpStatus.OK.value()));
    }

    // ==================== Delete ====================

    /**
     * Soft-deletes an equipment.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete equipment")
    public ResponseEntity<ApiResponse<Void>> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.ok(ApiResponse.success("Equipment deleted successfully", HttpStatus.OK.value()));
    }
}
