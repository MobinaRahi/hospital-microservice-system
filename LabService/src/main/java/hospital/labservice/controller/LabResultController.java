package hospital.labservice.controller;

import hospital.labservice.dto.labresult.LabResultCreateDto;
import hospital.labservice.dto.labresult.LabResultResponseDto;
import hospital.labservice.model.enums.ResultFlag;
import hospital.labservice.service.LabResultService;
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
 * REST Controller for LabResult management.
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/lab/results")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Lab Results", description = "Laboratory test results and verification")
public class LabResultController {

    private final LabResultService labResultService;

    @PostMapping
    @Operation(summary = "Register a new lab result")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Result created"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<LabResultResponseDto> createResult(@Valid @RequestBody LabResultCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(labResultService.createResult(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a lab result by ID")
    public ResponseEntity<LabResultResponseDto> getResultById(@PathVariable Long id) {
        return ResponseEntity.ok(labResultService.getResultById(id));
    }

    @GetMapping("/request-item/{requestItemId}")
    @Operation(summary = "Get result for a specific request item")
    public ResponseEntity<LabResultResponseDto> getResultByRequestItem(@PathVariable Long requestItemId) {
        return ResponseEntity.ok(labResultService.getResultByRequestItem(requestItemId));
    }

    @GetMapping
    @Operation(summary = "Get results with optional filters")
    public ResponseEntity<List<LabResultResponseDto>> getResults(
            @Parameter(description = "Filter by result flag") @RequestParam(required = false) ResultFlag flag,
            @Parameter(description = "Show unverified only") @RequestParam(required = false, defaultValue = "false") Boolean unverified,
            @Parameter(description = "Show critical only") @RequestParam(required = false, defaultValue = "false") Boolean critical,
            @Parameter(description = "Show abnormal only") @RequestParam(required = false, defaultValue = "false") Boolean abnormal) {

        if (Boolean.TRUE.equals(unverified)) {
            return ResponseEntity.ok(labResultService.getUnverifiedResults());
        }
        if (Boolean.TRUE.equals(critical)) {
            return ResponseEntity.ok(labResultService.getCriticalResults());
        }
        if (Boolean.TRUE.equals(abnormal)) {
            return ResponseEntity.ok(labResultService.getAbnormalResults());
        }
        if (flag != null) {
            return ResponseEntity.ok(labResultService.getResultsByFlag(flag));
        }
        return ResponseEntity.ok(List.of());
    }

    @PutMapping("/{id}/verify")
    @Operation(summary = "Verify a lab result")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Result verified"),
            @ApiResponse(responseCode = "404", description = "Result not found")
    })
    public ResponseEntity<LabResultResponseDto> verifyResult(
            @PathVariable Long id,
            @Parameter(description = "User ID verifying the result") @RequestParam Long verifiedBy) {
        return ResponseEntity.ok(labResultService.verifyResult(id, verifiedBy));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a lab result")
    public ResponseEntity<Void> deleteResult(@PathVariable Long id) {
        labResultService.deleteResult(id);
        return ResponseEntity.noContent().build();
    }
}
