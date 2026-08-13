package hospital.labservice.controller;

import hospital.labservice.dto.sample.SampleCreateDto;
import hospital.labservice.dto.sample.SampleResponseDto;
import hospital.labservice.dto.sample.SampleUpdateDto;
import hospital.labservice.model.enums.SampleQuality;
import hospital.labservice.model.enums.SampleType;
import hospital.labservice.service.SampleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Sample management.
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/lab/samples")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Samples", description = "Biological sample collection and tracking")
public class SampleController {

    private final SampleService sampleService;

    @PostMapping
    @Operation(summary = "Register a new sample")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sample created"),
            @ApiResponse(responseCode = "409", description = "Sample number already exists")
    })
    public ResponseEntity<SampleResponseDto> createSample(@Valid @RequestBody SampleCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sampleService.createSample(dto));
    }

    @GetMapping
    @Operation(summary = "Get samples with optional filters")
    public ResponseEntity<List<SampleResponseDto>> getAllSamples(
            @Parameter(description = "Filter by request ID") @RequestParam(required = false) Long requestId,
            @Parameter(description = "Filter by sample type") @RequestParam(required = false) SampleType type,
            @Parameter(description = "Show unreceived only") @RequestParam(required = false, defaultValue = "false") Boolean unreceived,
            @Parameter(description = "Show quality issues only") @RequestParam(required = false, defaultValue = "false") Boolean qualityIssues) {

        if (Boolean.TRUE.equals(unreceived)) {
            return ResponseEntity.ok(sampleService.getUnreceivedSamples());
        }
        if (Boolean.TRUE.equals(qualityIssues)) {
            return ResponseEntity.ok(sampleService.getSamplesWithQualityIssues());
        }
        if (requestId != null) {
            return ResponseEntity.ok(sampleService.getSamplesByRequest(requestId));
        }
        if (type != null) {
            return ResponseEntity.ok(sampleService.getSamplesByType(type));
        }
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a sample by ID")
    public ResponseEntity<SampleResponseDto> getSampleById(@PathVariable Long id) {
        return ResponseEntity.ok(sampleService.getSampleById(id));
    }

    @GetMapping("/number/{sampleNumber}")
    @Operation(summary = "Get a sample by unique number")
    public ResponseEntity<SampleResponseDto> getSampleByNumber(@PathVariable String sampleNumber) {
        return ResponseEntity.ok(sampleService.getSampleByNumber(sampleNumber));
    }

    @PutMapping("/{id}/receive")
    @Operation(summary = "Receive a sample at the laboratory")
    public ResponseEntity<SampleResponseDto> receiveSample(
            @PathVariable Long id,
            @Parameter(description = "User ID receiving the sample") @RequestParam Long receivedBy) {
        return ResponseEntity.ok(sampleService.receiveSample(id, receivedBy));
    }

    @PutMapping("/{id}/quality")
    @Operation(summary = "Update sample quality assessment")
    public ResponseEntity<SampleResponseDto> updateQuality(
            @PathVariable Long id,
            @RequestParam SampleQuality quality) {
        return ResponseEntity.ok(sampleService.updateSampleQuality(id, quality));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update sample details")
    public ResponseEntity<SampleResponseDto> updateSample(
            @PathVariable Long id,
            @Valid @RequestBody SampleUpdateDto dto) {
        return ResponseEntity.ok(sampleService.updateSample(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a sample")
    public ResponseEntity<Void> deleteSample(@PathVariable Long id) {
        sampleService.deleteSample(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-number")
    @Operation(summary = "Check if a sample number already exists")
    public ResponseEntity<Boolean> checkNumberExists(@RequestParam String sampleNumber) {
        return ResponseEntity.ok(sampleService.sampleNumberExists(sampleNumber));
    }
}
