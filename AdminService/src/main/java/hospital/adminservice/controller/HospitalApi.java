package hospital.adminservice.controller;

import hospital.adminservice.dto.hospital.HospitalCreateDto;
import hospital.adminservice.dto.hospital.HospitalResponseDto;
import hospital.adminservice.dto.hospital.HospitalUpdateDto;
import hospital.adminservice.dto.response.ApiResponse;
import hospital.adminservice.service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Hospital management.
 *
 * <p><strong>Access Control:</strong></p>
 * <ul>
 *   <li>SuperAdmin: Full CRUD access</li>
 *   <li>Admin: Read access</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/admin/hospitals")
@RequiredArgsConstructor
@Tag(name = "Hospital Management", description = "Hospital CRUD and management APIs")
public class HospitalApi {

    private final HospitalService hospitalService;

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Creates a new hospital.
     *
     * @param createDto the hospital creation data
     * @return created hospital
     */
    @PostMapping
    @Operation(summary = "Create a new hospital")
    public ResponseEntity<ApiResponse<HospitalResponseDto>> createHospital(
            @Valid @RequestBody HospitalCreateDto createDto) {
        HospitalResponseDto created = hospitalService.createHospital(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Hospital created successfully", HttpStatus.CREATED.value()));
    }

    // ═══════════════════════════════════════════════════════════════════
    // Read
    // ══════════════════════════════════════════════════════════════════

    /**
     * Gets a hospital by its ID.
     *
     * @param id the hospital ID
     * @return the hospital
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get hospital by ID")
    public ResponseEntity<ApiResponse<HospitalResponseDto>> getHospitalById(@PathVariable Long id) {
        HospitalResponseDto hospital = hospitalService.getHospitalById(id);
        return ResponseEntity.ok(ApiResponse.success(hospital, "Hospital retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets a hospital by its unique code.
     *
     * @param code the hospital code
     * @return the hospital
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "Get hospital by code")
    public ResponseEntity<ApiResponse<HospitalResponseDto>> getHospitalByCode(@PathVariable String code) {
        HospitalResponseDto hospital = hospitalService.getHospitalByCode(code);
        return ResponseEntity.ok(ApiResponse.success(hospital, "Hospital retrieved successfully", HttpStatus.OK.value()));
    }

    /**
     * Gets all hospitals.
     *
     * @return list of all hospitals
     */
    @GetMapping
    @Operation(summary = "Get all hospitals")
    public ResponseEntity<ApiResponse<List<HospitalResponseDto>>> getAllHospitals() {
        List<HospitalResponseDto> hospitals = hospitalService.getAllHospitals();
        return ResponseEntity.ok(ApiResponse.success(hospitals, "Hospitals retrieved successfully", HttpStatus.OK.value()));
    }

    // ═══════════════════════════════════════════════════════════════════
    // Update
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Updates an existing hospital.
     *
     * @param id        the hospital ID
     * @param updateDto the update data
     * @return the updated hospital
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a hospital by ID")
    public ResponseEntity<ApiResponse<HospitalResponseDto>> updateHospital(
            @PathVariable Long id,
            @Valid @RequestBody HospitalUpdateDto updateDto) {
        HospitalResponseDto updated = hospitalService.updateHospital(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Hospital updated successfully", HttpStatus.OK.value()));
    }

    // ═══════════════════════════════════════════════════════════════════
    // Delete
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Soft-deletes a hospital.
     *
     * @param id the hospital ID
     * @return success message
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a hospital")
    public ResponseEntity<ApiResponse<Void>> deleteHospital(@PathVariable Long id) {
        hospitalService.deleteHospital(id);
        return ResponseEntity.ok(ApiResponse.success("Hospital deleted successfully", HttpStatus.OK.value()));
    }
}
