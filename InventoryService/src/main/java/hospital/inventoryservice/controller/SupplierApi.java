package hospital.inventoryservice.controller;

import hospital.inventoryservice.dto.response.ApiResponse;
import hospital.inventoryservice.dto.supplier.SupplierCreateDto;
import hospital.inventoryservice.dto.supplier.SupplierResponseDto;
import hospital.inventoryservice.dto.supplier.SupplierUpdateDto;
import hospital.inventoryservice.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Supplier management.
 * Provides CRUD operations for pharmaceutical and equipment suppliers.
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/inventory/suppliers")
@RequiredArgsConstructor
@Tag(name = "Supplier Management", description = "Supplier CRUD and search APIs")
public class SupplierApi {

    // ==================== Dependencies ====================

    private final SupplierService supplierService;

    // ==================== Create ====================

    /**
     * Creates a new supplier.
     *
     * <p><strong>Required fields:</strong> name</p>
     * <p><strong>Optional fields:</strong> phone, mobile, email, address, contactPerson, paymentTerms</p>
     *
     * <p><strong>Validations:</strong></p>
     * <ul>
     *   <li>Email must be valid format if provided</li>
     *   <li>Email must be unique across suppliers</li>
     * </ul>
     */
    @PostMapping
    @Operation(summary = "Create a new supplier")
    public ResponseEntity<ApiResponse<SupplierResponseDto>> createSupplier(
            @Valid @RequestBody SupplierCreateDto createDto) {
        SupplierResponseDto created = supplierService.createSupplier(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Supplier created successfully", HttpStatus.CREATED.value()));
    }

    // ==================== Update ====================

    /**
     * Updates an existing supplier.
     * <p>Only provided fields will be updated.</p>
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a supplier by ID")
    public ResponseEntity<ApiResponse<SupplierResponseDto>> updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody SupplierUpdateDto updateDto) {
        SupplierResponseDto updated = supplierService.updateSupplier(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Supplier updated successfully", HttpStatus.OK.value()));
    }

    /**
     * Toggles the active status of a supplier.
     * Inactive suppliers are hidden from selection lists.
     */
    @PatchMapping("/{id}/toggle-active")
    @Operation(summary = "Toggle supplier active status")
    public ResponseEntity<ApiResponse<SupplierResponseDto>> toggleActive(@PathVariable Long id) {
        SupplierResponseDto toggled = supplierService.toggleActive(id);
        return ResponseEntity.ok(ApiResponse.success(toggled, "Supplier status toggled successfully", HttpStatus.OK.value()));
    }

    // ==================== Read ====================

    /**
     * Gets a supplier by its ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get supplier by ID")
    public ResponseEntity<ApiResponse<SupplierResponseDto>> getSupplierById(@PathVariable Long id) {
        SupplierResponseDto supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(ApiResponse.success(supplier, "Supplier retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets a supplier by email address.
     */
    @GetMapping("/email/{email}")
    @Operation(summary = "Get supplier by email")
    public ResponseEntity<ApiResponse<SupplierResponseDto>> getSupplierByEmail(@PathVariable String email) {
        SupplierResponseDto supplier = supplierService.getSupplierByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(supplier, "Supplier retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets all active suppliers.
     * Used for supplier selection dropdowns.
     */
    @GetMapping("/active")
    @Operation(summary = "Get all active suppliers")
    public ResponseEntity<ApiResponse<List<SupplierResponseDto>>> getAllActiveSuppliers() {
        List<SupplierResponseDto> suppliers = supplierService.getAllActiveSuppliers();
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Active suppliers retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets all suppliers (including inactive).
     * Used for admin views.
     */
    @GetMapping
    @Operation(summary = "Get all suppliers")
    public ResponseEntity<ApiResponse<List<SupplierResponseDto>>> getAllSuppliers() {
        List<SupplierResponseDto> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Searches suppliers by name (case-insensitive, partial match).
     */
    @GetMapping("/search")
    @Operation(summary = "Search suppliers by name")
    public ResponseEntity<ApiResponse<List<SupplierResponseDto>>> searchSuppliers(@RequestParam String name) {
        List<SupplierResponseDto> suppliers = supplierService.searchByName(name);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers retrieved successfully", HttpStatus.OK.value()));
    }

    // ==================== Delete ====================

    /**
     * Soft-deletes a supplier.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a supplier")
    public ResponseEntity<ApiResponse<Void>> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier deleted successfully", HttpStatus.OK.value()));
    }
}
