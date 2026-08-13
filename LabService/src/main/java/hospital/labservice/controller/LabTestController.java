package hospital.labservice.controller;

import hospital.labservice.dto.labtest.LabTestCreateDto;
import hospital.labservice.dto.labtest.LabTestResponseDto;
import hospital.labservice.dto.labtest.LabTestUpdateDto;
import hospital.labservice.model.enums.TestCategory;
import hospital.labservice.service.LabTestService;
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
 * REST Controller for LabTest management.
 *
 * <p><strong>Endpoints:</strong></p>
 * <ul>
 *   <li>POST   /api/v1/lab/tests — Create new test</li>
 *   <li>GET    /api/v1/lab/tests — Get all / filter by category/active</li>
 *   <li>GET    /api/v1/lab/tests/{id} — Get by ID</li>
 *   <li>GET    /api/v1/lab/tests/code/{code} — Get by code</li>
 *   <li>GET    /api/v1/lab/tests/search — Search by name</li>
 *   <li>PUT    /api/v1/lab/tests/{id} — Update</li>
 *   <li>DELETE /api/v1/lab/tests/{id} — Soft delete</li>
 *   <li>GET    /api/v1/lab/tests/check-code — Check code existence</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/lab/tests")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Lab Tests", description = "Laboratory test definitions management")
public class LabTestController {

    private final LabTestService labTestService;

    @PostMapping
    @Operation(summary = "Create a new lab test")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Test created successfully"),
            @ApiResponse(responseCode = "409", description = "Test code already exists"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<LabTestResponseDto> createLabTest(@Valid @RequestBody LabTestCreateDto dto) {
        LabTestResponseDto response = labTestService.createLabTest(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all lab tests with optional filters")
    public ResponseEntity<List<LabTestResponseDto>> getAllLabTests(
            @Parameter(description = "Filter by category") @RequestParam(required = false) TestCategory category,
            @Parameter(description = "Filter by active status") @RequestParam(required = false) Boolean active) {

        List<LabTestResponseDto> tests;
        if (category != null) {
            tests = active != null
                    ? labTestService.getLabTestsByCategory(category).stream()
                        .filter(t -> active.equals(t.getIsActive()))
                        .toList()
                    : labTestService.getLabTestsByCategory(category);
        } else if (Boolean.TRUE.equals(active)) {
            tests = labTestService.getActiveLabTests();
        } else {
            tests = labTestService.getAllLabTests();
        }
        return ResponseEntity.ok(tests);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a lab test by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Test found"),
            @ApiResponse(responseCode = "404", description = "Test not found")
    })
    public ResponseEntity<LabTestResponseDto> getLabTestById(@PathVariable Long id) {
        return ResponseEntity.ok(labTestService.getLabTestById(id));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get a lab test by unique code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Test found"),
            @ApiResponse(responseCode = "404", description = "Test not found")
    })
    public ResponseEntity<LabTestResponseDto> getLabTestByCode(@PathVariable String code) {
        return ResponseEntity.ok(labTestService.getLabTestByCode(code));
    }

    @GetMapping("/search")
    @Operation(summary = "Search lab tests by name")
    public ResponseEntity<List<LabTestResponseDto>> searchByName(
            @Parameter(description = "Name search pattern") @RequestParam String name) {
        return ResponseEntity.ok(labTestService.searchByName(name));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing lab test")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Test updated"),
            @ApiResponse(responseCode = "404", description = "Test not found")
    })
    public ResponseEntity<LabTestResponseDto> updateLabTest(
            @PathVariable Long id,
            @Valid @RequestBody LabTestUpdateDto dto) {
        return ResponseEntity.ok(labTestService.updateLabTest(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a lab test")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Test deleted"),
            @ApiResponse(responseCode = "404", description = "Test not found")
    })
    public ResponseEntity<Void> deleteLabTest(@PathVariable Long id) {
        labTestService.deleteLabTest(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-code")
    @Operation(summary = "Check if a test code already exists")
    public ResponseEntity<Boolean> checkCodeExists(
            @Parameter(description = "Test code to check") @RequestParam String code) {
        return ResponseEntity.ok(labTestService.codeExists(code));
    }
}
