package hospital.clinicalservice.controller;

import hospital.clinicalservice.dto.prescriptionitem.PrescriptionItemCreateDto;
import hospital.clinicalservice.dto.prescriptionitem.PrescriptionItemResponseDto;
import hospital.clinicalservice.dto.prescriptionitem.PrescriptionItemUpdateDto;
import hospital.clinicalservice.dto.response.ApiResponse;
import hospital.clinicalservice.service.PrescriptionItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for PrescriptionItem management.
 * Manages drug items within prescriptions.
 *
 * @author Mobina
 */
@RestController
@RequestMapping("/api/v1/prescription-items")
@RequiredArgsConstructor
@Tag(name = "Prescription Item Management", description = "Prescription Item (Drug) CRUD APIs")
public class PrescriptionItemApi {

    private final PrescriptionItemService prescriptionItemService;

    @PostMapping
    @Operation(summary = "Add a drug to prescription")
    public ResponseEntity<ApiResponse<PrescriptionItemResponseDto>> createPrescriptionItem(@Valid @RequestBody PrescriptionItemCreateDto createDto) {
        PrescriptionItemResponseDto created = prescriptionItemService.createPrescriptionItem(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Prescription item created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a prescription item by ID")
    public ResponseEntity<ApiResponse<PrescriptionItemResponseDto>> updatePrescriptionItem(@PathVariable Long id, @Valid @RequestBody PrescriptionItemUpdateDto updateDto) {
        PrescriptionItemResponseDto updated = prescriptionItemService.updatePrescriptionItem(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Prescription item updated successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a prescription item by ID")
    public ResponseEntity<ApiResponse<Void>> deletePrescriptionItem(@PathVariable Long id) {
        prescriptionItemService.deletePrescriptionItem(id);
        return ResponseEntity.ok(ApiResponse.success("Prescription item deleted successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get prescription item by ID")
    public ResponseEntity<ApiResponse<PrescriptionItemResponseDto>> getPrescriptionItemById(@PathVariable Long id) {
        PrescriptionItemResponseDto item = prescriptionItemService.getPrescriptionItemById(id);
        return ResponseEntity.ok(ApiResponse.success(item, "Prescription item retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/prescription/{prescriptionId}")
    @Operation(summary = "Get items by prescription ID")
    public ResponseEntity<ApiResponse<List<PrescriptionItemResponseDto>>> getItemsByPrescriptionId(@PathVariable Long prescriptionId) {
        List<PrescriptionItemResponseDto> items = prescriptionItemService.getPrescriptionItemsByPrescriptionId(prescriptionId);
        return ResponseEntity.ok(ApiResponse.success(items, "Prescription items retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get items by patient ID")
    public ResponseEntity<ApiResponse<List<PrescriptionItemResponseDto>>> getItemsByPatientId(@PathVariable Long patientId) {
        List<PrescriptionItemResponseDto> items = prescriptionItemService.getPrescriptionItemsByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(items, "Prescription items retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/search")
    @Operation(summary = "Search items by drug name")
    public ResponseEntity<ApiResponse<List<PrescriptionItemResponseDto>>> searchByDrugName(@RequestParam String drugName) {
        List<PrescriptionItemResponseDto> items = prescriptionItemService.searchPrescriptionItemsByDrugName(drugName);
        return ResponseEntity.ok(ApiResponse.success(items, "Prescription items found successfully", HttpStatus.OK.value()));
    }
}
