package hospital.inventoryservice.controller;

import hospital.inventoryservice.dto.drug.DrugCreateDto;
import hospital.inventoryservice.dto.drug.DrugResponseDto;
import hospital.inventoryservice.dto.drug.DrugUpdateDto;
import hospital.inventoryservice.dto.response.ApiResponse;
import hospital.inventoryservice.model.enums.DrugForm;
import hospital.inventoryservice.service.DrugService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Drug management.
 * Provides CRUD operations and search capabilities for the drug catalog.
 *
 * <p><strong>Access Control:</strong></p>
 * <ul>
 *   <li>Pharmacists: Full CRUD access</li>
 *   <li>Doctors: Read access for prescribing</li>
 *   <li>Nurses: Read access</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/inventory/drugs")
@RequiredArgsConstructor
@Tag(name = "Drug Management", description = "Drug catalog CRUD and search APIs")
public class DrugApi {

    // ==================== Dependencies ====================

    private final DrugService drugService;

    // ==================== Create ====================

    /**
     * Creates a new drug in the catalog.
     *
     * <p><strong>Required fields:</strong> genericName, form, categoryId</p>
     * <p><strong>Optional fields:</strong> brandName, strength, price, requiresPrescription, barcode, description</p>
     *
     * <p><strong>Validations:</strong></p>
     * <ul>
     *   <li>Category must exist</li>
     *   <li>Barcode must be unique if provided</li>
     * </ul>
     */
    @PostMapping
    @Operation(summary = "Create a new drug")
    public ResponseEntity<ApiResponse<DrugResponseDto>> createDrug(@Valid @RequestBody DrugCreateDto createDto) {
        DrugResponseDto created = drugService.createDrug(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Drug created successfully", HttpStatus.CREATED.value()));
    }

    // ==================== Update ====================

    /**
     * Updates an existing drug.
     * <p>Only provided fields will be updated. Generic name is immutable.</p>
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a drug by ID")
    public ResponseEntity<ApiResponse<DrugResponseDto>> updateDrug(
            @PathVariable Long id,
            @Valid @RequestBody DrugUpdateDto updateDto) {
        DrugResponseDto updated = drugService.updateDrug(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Drug updated successfully", HttpStatus.OK.value()));
    }

    /**
     * Toggles the active status of a drug.
     * Used to temporarily deactivate a drug without deleting it.
     */
    @PatchMapping("/{id}/toggle-active")
    @Operation(summary = "Toggle drug active status")
    public ResponseEntity<ApiResponse<DrugResponseDto>> toggleActive(@PathVariable Long id) {
        DrugResponseDto toggled = drugService.toggleActive(id);
        return ResponseEntity.ok(ApiResponse.success(toggled, "Drug status toggled successfully", HttpStatus.OK.value()));
    }

    // ==================== Read ====================

    /**
     * Gets a drug by its ID.
     * Returns full drug details including category information.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get drug by ID")
    public ResponseEntity<ApiResponse<DrugResponseDto>> getDrugById(@PathVariable Long id) {
        DrugResponseDto drug = drugService.getDrugById(id);
        return ResponseEntity.ok(ApiResponse.success(drug, "Drug retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets a drug by its barcode.
     * Used for barcode scanning at the pharmacy.
     */
    @GetMapping("/barcode/{barcode}")
    @Operation(summary = "Get drug by barcode")
    public ResponseEntity<ApiResponse<DrugResponseDto>> getDrugByBarcode(@PathVariable String barcode) {
        DrugResponseDto drug = drugService.getDrugByBarcode(barcode);
        return ResponseEntity.ok(ApiResponse.success(drug, "Drug retrieved by barcode successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets all active drugs.
     * Used for dropdowns, catalogs, and general listing.
     */
    @GetMapping
    @Operation(summary = "Get all drugs")
    public ResponseEntity<ApiResponse<List<DrugResponseDto>>> getAllDrugs() {
        List<DrugResponseDto> drugs = drugService.getAllDrugs();
        return ResponseEntity.ok(ApiResponse.success(drugs, "Drugs retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Searches drugs by generic name (case-insensitive, partial match).
     * Used for quick search in the UI.
     */
    @GetMapping("/search")
    @Operation(summary = "Search drugs by generic name")
    public ResponseEntity<ApiResponse<List<DrugResponseDto>>> searchDrugs(@RequestParam String name) {
        List<DrugResponseDto> drugs = drugService.searchByGenericName(name);
        return ResponseEntity.ok(ApiResponse.success(drugs, "Drugs retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets drugs by category.
     * Used for browsing drugs by category in the UI.
     */
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get drugs by category")
    public ResponseEntity<ApiResponse<List<DrugResponseDto>>> getDrugsByCategory(@PathVariable Long categoryId) {
        List<DrugResponseDto> drugs = drugService.getDrugsByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(drugs, "Drugs retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets drugs by pharmaceutical form (tablet, capsule, syrup, etc.).
     * Used for filtering the drug list.
     */
    @GetMapping("/form/{form}")
    @Operation(summary = "Get drugs by form")
    public ResponseEntity<ApiResponse<List<DrugResponseDto>>> getDrugsByForm(@PathVariable DrugForm form) {
        List<DrugResponseDto> drugs = drugService.getDrugsByForm(form);
        return ResponseEntity.ok(ApiResponse.success(drugs, "Drugs retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets prescription-only drugs.
     * Used for flagging drugs that require a doctor's prescription.
     */
    @GetMapping("/prescription-only")
    @Operation(summary = "Get prescription-only drugs")
    public ResponseEntity<ApiResponse<List<DrugResponseDto>>> getPrescriptionDrugs() {
        List<DrugResponseDto> drugs = drugService.getPrescriptionDrugs();
        return ResponseEntity.ok(ApiResponse.success(drugs, "Prescription drugs retrieved successfully", HttpStatus.OK.value()));
    }

    // ==================== Alerts ====================

    /**
     * Gets drugs with low stock.
     * Used for pharmacy alerts and reorder notifications.
     */
    @GetMapping("/alerts/low-stock")
    @Operation(summary = "Get drugs with low stock")
    public ResponseEntity<ApiResponse<List<DrugResponseDto>>> getDrugsWithLowStock() {
        List<DrugResponseDto> drugs = drugService.getDrugsWithLowStock();
        return ResponseEntity.ok(ApiResponse.success(drugs, "Low stock drugs retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets drugs with stock expiring within a number of days.
     * Used for pharmacy alerts to avoid dispensing near-expiry drugs.
     */
    @GetMapping("/alerts/expiring")
    @Operation(summary = "Get drugs with expiring stock")
    public ResponseEntity<ApiResponse<List<DrugResponseDto>>> getDrugsWithExpiringStock(
            @RequestParam(defaultValue = "30") int days) {
        List<DrugResponseDto> drugs = drugService.getDrugsWithExpiringStock(days);
        return ResponseEntity.ok(ApiResponse.success(drugs, "Drugs with expiring stock retrieved successfully", HttpStatus.OK.value()));
    }

    // ==================== Delete ====================

    /**
     * Soft-deletes a drug.
     * The drug is marked as deleted but remains in the database.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a drug")
    public ResponseEntity<ApiResponse<Void>> deleteDrug(@PathVariable Long id) {
        drugService.deleteDrug(id);
        return ResponseEntity.ok(ApiResponse.success("Drug deleted successfully", HttpStatus.OK.value()));
    }
}
