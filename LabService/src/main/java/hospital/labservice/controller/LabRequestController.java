package hospital.labservice.controller;

import hospital.labservice.dto.labrequest.LabRequestCreateDto;
import hospital.labservice.dto.labrequest.LabRequestResponseDto;
import hospital.labservice.dto.labrequest.LabRequestUpdateDto;
import hospital.labservice.model.enums.RequestStatus;
import hospital.labservice.service.LabRequestService;
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
 * REST Controller for LabRequest management.
 *
 * <p><strong>Endpoints:</strong></p>
 * <ul>
 *   <li>POST   /api/v1/lab/requests — Create new request</li>
 *   <li>GET    /api/v1/lab/requests — Get all / filter by patient/doctor/status</li>
 *   <li>GET    /api/v1/lab/requests/{id} — Get by ID</li>
 *   <li>GET    /api/v1/lab/requests/number/{number} — Get by number</li>
 *   <li>GET    /api/v1/lab/requests/urgent — Get urgent pending requests</li>
 *   <li>PUT    /api/v1/lab/requests/{id} — Update</li>
 *   <li>PUT    /api/v1/lab/requests/{id}/approve — Approve</li>
 *   <li>PUT    /api/v1/lab/requests/{id}/reject — Reject</li>
 *   <li>PUT    /api/v1/lab/requests/{id}/collect-sample — Mark sample collected</li>
 *   <li>PUT    /api/v1/lab/requests/{id}/process — Start processing</li>
 *   <li>PUT    /api/v1/lab/requests/{id}/complete — Complete</li>
 *   <li>PUT    /api/v1/lab/requests/{id}/cancel — Cancel</li>
 *   <li>DELETE /api/v1/lab/requests/{id} — Soft delete</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/lab/requests")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Lab Requests", description = "Laboratory test request management with workflow")
public class LabRequestController {

    private final LabRequestService labRequestService;

    @PostMapping
    @Operation(summary = "Create a new lab request with test items")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Request created"),
            @ApiResponse(responseCode = "409", description = "Request number already exists"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<LabRequestResponseDto> createLabRequest(@Valid @RequestBody LabRequestCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(labRequestService.createLabRequest(dto));
    }

    @GetMapping
    @Operation(summary = "Get all lab requests with optional filters")
    public ResponseEntity<List<LabRequestResponseDto>> getAllLabRequests(
            @Parameter(description = "Filter by patient ID") @RequestParam(required = false) Long patientId,
            @Parameter(description = "Filter by doctor ID") @RequestParam(required = false) Long doctorId,
            @Parameter(description = "Filter by status") @RequestParam(required = false) RequestStatus status) {

        List<LabRequestResponseDto> requests;
        if (patientId != null && status != null) {
            requests = labRequestService.getRequestsByStatus(status).stream()
                    .filter(r -> patientId.equals(r.getPatientId()))
                    .toList();
        } else if (patientId != null) {
            requests = labRequestService.getRequestsByPatient(patientId);
        } else if (doctorId != null) {
            requests = labRequestService.getRequestsByDoctor(doctorId);
        } else if (status != null) {
            requests = labRequestService.getRequestsByStatus(status);
        } else {
            requests = labRequestService.getAllLabRequests();
        }
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a lab request by ID")
    public ResponseEntity<LabRequestResponseDto> getLabRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(labRequestService.getLabRequestById(id));
    }

    @GetMapping("/number/{number}")
    @Operation(summary = "Get a lab request by unique number")
    public ResponseEntity<LabRequestResponseDto> getLabRequestByNumber(@PathVariable String number) {
        return ResponseEntity.ok(labRequestService.getLabRequestByNumber(number));
    }

    @GetMapping("/urgent")
    @Operation(summary = "Get urgent pending requests (STAT and URGENT priority)")
    public ResponseEntity<List<LabRequestResponseDto>> getUrgentPendingRequests() {
        return ResponseEntity.ok(labRequestService.getUrgentPendingRequests());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update lab request (priority and clinical notes)")
    public ResponseEntity<LabRequestResponseDto> updateLabRequest(
            @PathVariable Long id,
            @Valid @RequestBody LabRequestUpdateDto dto) {
        return ResponseEntity.ok(labRequestService.updateLabRequest(id, dto));
    }

    // ═══════════════════════════════════════════════════════════════
    // Status Transitions
    // ═══════════════════════════════════════════════════════════════

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve a pending lab request")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Request approved"),
            @ApiResponse(responseCode = "400", description = "Cannot approve in current status")
    })
    public ResponseEntity<LabRequestResponseDto> approveRequest(@PathVariable Long id) {
        return ResponseEntity.ok(labRequestService.approveRequest(id));
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject a pending lab request")
    public ResponseEntity<LabRequestResponseDto> rejectRequest(@PathVariable Long id) {
        return ResponseEntity.ok(labRequestService.rejectRequest(id));
    }

    @PutMapping("/{id}/collect-sample")
    @Operation(summary = "Mark sample as collected")
    public ResponseEntity<LabRequestResponseDto> markSampleCollected(@PathVariable Long id) {
        return ResponseEntity.ok(labRequestService.markSampleCollected(id));
    }

    @PutMapping("/{id}/process")
    @Operation(summary = "Start processing the request")
    public ResponseEntity<LabRequestResponseDto> startProcessing(@PathVariable Long id) {
        return ResponseEntity.ok(labRequestService.startProcessing(id));
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Complete the request")
    public ResponseEntity<LabRequestResponseDto> completeRequest(@PathVariable Long id) {
        return ResponseEntity.ok(labRequestService.completeRequest(id));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel the request")
    public ResponseEntity<LabRequestResponseDto> cancelRequest(@PathVariable Long id) {
        return ResponseEntity.ok(labRequestService.cancelRequest(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a lab request")
    public ResponseEntity<Void> deleteLabRequest(@PathVariable Long id) {
        labRequestService.deleteLabRequest(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-number")
    @Operation(summary = "Check if a request number already exists")
    public ResponseEntity<Boolean> checkNumberExists(
            @RequestParam String requestNumber) {
        return ResponseEntity.ok(labRequestService.requestNumberExists(requestNumber));
    }
}
