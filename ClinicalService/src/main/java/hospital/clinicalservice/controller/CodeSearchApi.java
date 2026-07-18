package hospital.clinicalservice.controller;

import hospital.clinicalservice.dto.response.ApiResponse;
import hospital.clinicalservice.service.CodeSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for ICD-10 and LOINC code search.
 * Provides autocomplete for diagnosis and observation forms.
 *
 * <p><strong>Public endpoints (no auth required):</strong></p>
 * <ul>
 *   <li>GET /api/v1/codes/icd/search?query=فشار → Search ICD-10</li>
 *   <li>GET /api/v1/codes/loinc/search?query=blood → Search LOINC</li>
 * </ul>
 *
 * @author Mobina
 */
@RestController
@RequestMapping("/api/v1/codes")
@RequiredArgsConstructor
@Tag(name = "Code Search", description = "Search ICD-10 and LOINC standard codes")
public class CodeSearchApi {

    private final CodeSearchService codeSearchService;

    // ==================== ICD-10 Endpoints ====================

    /**
     * Searches ICD-10 codes by query.
     * Used for: autocomplete in diagnosis form.
     * Example: /api/v1/codes/icd/search?query=فشار
     */
    @GetMapping("/icd/search")
    @Operation(summary = "Search ICD-10 codes by query")
    public ResponseEntity<ApiResponse<List<CodeSearchService.IcdCode>>> searchIcd(@RequestParam String query) {
        List<CodeSearchService.IcdCode> results = codeSearchService.searchIcd(query);
        return ResponseEntity.ok(ApiResponse.success(results, "ICD-10 codes found", HttpStatus.OK.value()));
    }

    /**
     * Gets ICD-10 code details by exact code.
     * Example: /api/v1/codes/icd/I10
     */
    @GetMapping("/icd/{code}")
    @Operation(summary = "Get ICD-10 code details")
    public ResponseEntity<ApiResponse<CodeSearchService.IcdCode>> getIcd(@PathVariable String code) {
        CodeSearchService.IcdCode result = codeSearchService.getIcdByCode(code);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("ICD-10 code not found", HttpStatus.NOT_FOUND.value()));
        }
        return ResponseEntity.ok(ApiResponse.success(result, "ICD-10 code found", HttpStatus.OK.value()));
    }

    /**
     * Gets all ICD-10 codes.
     * Used for: dropdown list or initial load.
     */
    @GetMapping("/icd/all")
    @Operation(summary = "Get all ICD-10 codes")
    public ResponseEntity<ApiResponse<List<CodeSearchService.IcdCode>>> allIcd() {
        return ResponseEntity.ok(ApiResponse.success(codeSearchService.searchIcd(null), "All ICD-10 codes", HttpStatus.OK.value()));
    }

    // ==================== LOINC Endpoints ====================

    /**
     * Searches LOINC codes by query.
     * Used for: autocomplete in observation form.
     * Example: /api/v1/codes/loinc/search?query=pressure
     */
    @GetMapping("/loinc/search")
    @Operation(summary = "Search LOINC codes by query")
    public ResponseEntity<ApiResponse<List<CodeSearchService.LoincCode>>> searchLoinc(@RequestParam String query) {
        List<CodeSearchService.LoincCode> results = codeSearchService.searchLoinc(query);
        return ResponseEntity.ok(ApiResponse.success(results, "LOINC codes found", HttpStatus.OK.value()));
    }

    /**
     * Gets LOINC code details by exact code.
     * Example: /api/v1/codes/loinc/8480-6
     */
    @GetMapping("/loinc/{code}")
    @Operation(summary = "Get LOINC code details")
    public ResponseEntity<ApiResponse<CodeSearchService.LoincCode>> getLoinc(@PathVariable String code) {
        CodeSearchService.LoincCode result = codeSearchService.getLoincByCode(code);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("LOINC code not found", HttpStatus.NOT_FOUND.value()));
        }
        return ResponseEntity.ok(ApiResponse.success(result, "LOINC code found", HttpStatus.OK.value()));
    }

    /**
     * Gets all LOINC codes.
     * Used for: dropdown list or initial load.
     */
    @GetMapping("/loinc/all")
    @Operation(summary = "Get all LOINC codes")
    public ResponseEntity<ApiResponse<List<CodeSearchService.LoincCode>>> allLoinc() {
        return ResponseEntity.ok(ApiResponse.success(codeSearchService.searchLoinc(null), "All LOINC codes", HttpStatus.OK.value()));
    }
}
