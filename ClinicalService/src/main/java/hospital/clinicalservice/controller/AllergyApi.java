package hospital.clinicalservice.controller;

import hospital.clinicalservice.dto.allergy.AllergyCreateDto;
import hospital.clinicalservice.dto.allergy.AllergyResponseDto;
import hospital.clinicalservice.dto.allergy.AllergyUpdateDto;
import hospital.clinicalservice.dto.response.ApiResponse;
import hospital.clinicalservice.model.enums.AllergyType;
import hospital.clinicalservice.service.AllergyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Allergy management.
 * Manages patient allergies (drug, food, environmental).
 *
 * @author Mobina
 */
@RestController
@RequestMapping("/api/v1/allergies")
@RequiredArgsConstructor
@Tag(name = "Allergy Management", description = "Allergy CRUD and management APIs")
public class AllergyApi {

    private final AllergyService allergyService;

    @PostMapping
    @Operation(summary = "Create a new allergy")
    public ResponseEntity<ApiResponse<AllergyResponseDto>> createAllergy(@Valid @RequestBody AllergyCreateDto createDto) {
        AllergyResponseDto created = allergyService.createAllergy(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Allergy created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an allergy by ID")
    public ResponseEntity<ApiResponse<AllergyResponseDto>> updateAllergy(@PathVariable Long id, @Valid @RequestBody AllergyUpdateDto updateDto) {
        AllergyResponseDto updated = allergyService.updateAllergy(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Allergy updated successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an allergy by ID")
    public ResponseEntity<ApiResponse<Void>> deleteAllergy(@PathVariable Long id) {
        allergyService.deleteAllergy(id);
        return ResponseEntity.ok(ApiResponse.success("Allergy deleted successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get allergy by ID")
    public ResponseEntity<ApiResponse<AllergyResponseDto>> getAllergyById(@PathVariable Long id) {
        AllergyResponseDto allergy = allergyService.getAllergyById(id);
        return ResponseEntity.ok(ApiResponse.success(allergy, "Allergy retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get allergies by patient ID")
    public ResponseEntity<ApiResponse<List<AllergyResponseDto>>> getAllergiesByPatientId(@PathVariable Long patientId) {
        List<AllergyResponseDto> allergies = allergyService.getAllergiesByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(allergies, "Allergies retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}/active")
    @Operation(summary = "Get active allergies by patient ID")
    public ResponseEntity<ApiResponse<List<AllergyResponseDto>>> getActiveAllergiesByPatientId(@PathVariable Long patientId) {
        List<AllergyResponseDto> allergies = allergyService.getActiveAllergiesByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(allergies, "Active allergies retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}/severe")
    @Operation(summary = "Get severe allergies by patient ID")
    public ResponseEntity<ApiResponse<List<AllergyResponseDto>>> getSevereAllergiesByPatientId(@PathVariable Long patientId) {
        List<AllergyResponseDto> allergies = allergyService.getSevereAllergiesByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(allergies, "Severe allergies retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get allergies by type")
    public ResponseEntity<ApiResponse<List<AllergyResponseDto>>> getAllergiesByType(@PathVariable AllergyType type) {
        List<AllergyResponseDto> allergies = allergyService.getAllergiesByType(type);
        return ResponseEntity.ok(ApiResponse.success(allergies, "Allergies retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/search")
    @Operation(summary = "Search allergies by allergen name")
    public ResponseEntity<ApiResponse<List<AllergyResponseDto>>> searchAllergies(@RequestParam String query) {
        List<AllergyResponseDto> allergies = allergyService.searchAllergiesByAllergenName(query);
        return ResponseEntity.ok(ApiResponse.success(allergies, "Allergies found successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/patient/{patientId}")
    @Operation(summary = "Count allergies by patient ID")
    public ResponseEntity<ApiResponse<Long>> countAllergiesByPatientId(@PathVariable Long patientId) {
        Long count = allergyService.countAllergiesByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(count, "Allergies count retrieved successfully", HttpStatus.OK.value()));
    }
}
