package hospital.clinicalservice.controller;

import hospital.clinicalservice.dto.response.ApiResponse;
import hospital.clinicalservice.dto.triage.TriageCreateDto;
import hospital.clinicalservice.dto.triage.TriageResponseDto;
import hospital.clinicalservice.dto.triage.TriageUpdateDto;
import hospital.clinicalservice.model.enums.TriageLevel;
import hospital.clinicalservice.service.TriageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Triage management.
 * Manages emergency triage assessments.
 *
 * @author Mobina
 */
@RestController
@RequestMapping("/api/v1/triages")
@RequiredArgsConstructor
@Tag(name = "Triage Management", description = "Triage (Emergency) CRUD and management APIs")
public class TriageApi {

    private final TriageService triageService;

    @PostMapping
    @Operation(summary = "Create a new triage")
    public ResponseEntity<ApiResponse<TriageResponseDto>> createTriage(@Valid @RequestBody TriageCreateDto createDto) {
        TriageResponseDto created = triageService.createTriage(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Triage created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a triage by ID")
    public ResponseEntity<ApiResponse<TriageResponseDto>> updateTriage(@PathVariable Long id, @Valid @RequestBody TriageUpdateDto updateDto) {
        TriageResponseDto updated = triageService.updateTriage(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Triage updated successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a triage by ID")
    public ResponseEntity<ApiResponse<Void>> deleteTriage(@PathVariable Long id) {
        triageService.deleteTriage(id);
        return ResponseEntity.ok(ApiResponse.success("Triage deleted successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get triage by ID")
    public ResponseEntity<ApiResponse<TriageResponseDto>> getTriageById(@PathVariable Long id) {
        TriageResponseDto triage = triageService.getTriageById(id);
        return ResponseEntity.ok(ApiResponse.success(triage, "Triage retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get triages by patient ID")
    public ResponseEntity<ApiResponse<List<TriageResponseDto>>> getTriagesByPatientId(@PathVariable Long patientId) {
        List<TriageResponseDto> triages = triageService.getTriagesByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(triages, "Triages retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}/latest")
    @Operation(summary = "Get latest triage by patient ID")
    public ResponseEntity<ApiResponse<TriageResponseDto>> getLatestTriageByPatientId(@PathVariable Long patientId) {
        TriageResponseDto triage = triageService.getLatestTriageByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(triage, "Latest triage retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/level/{level}")
    @Operation(summary = "Get triages by level")
    public ResponseEntity<ApiResponse<List<TriageResponseDto>>> getTriagesByLevel(@PathVariable TriageLevel level) {
        List<TriageResponseDto> triages = triageService.getTriagesByLevel(level);
        return ResponseEntity.ok(ApiResponse.success(triages, "Triages retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/critical")
    @Operation(summary = "Get critical cases (Level 1 & 2)")
    public ResponseEntity<ApiResponse<List<TriageResponseDto>>> getCriticalCases() {
        List<TriageResponseDto> triages = triageService.getCriticalCases();
        return ResponseEntity.ok(ApiResponse.success(triages, "Critical cases retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/critical")
    @Operation(summary = "Count critical cases")
    public ResponseEntity<ApiResponse<Long>> countCriticalCases() {
        Long count = triageService.countCriticalCases();
        return ResponseEntity.ok(ApiResponse.success(count, "Critical cases count retrieved successfully", HttpStatus.OK.value()));
    }
}
