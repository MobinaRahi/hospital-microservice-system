package hospital.notificationservice.controller;

import hospital.notificationservice.dto.emaillog.EmailLogCreateDto;
import hospital.notificationservice.dto.emaillog.EmailLogResponseDto;
import hospital.notificationservice.model.enums.EmailStatus;
import hospital.notificationservice.service.EmailLogService;
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
 * REST Controller for Email Log management.
 *
 * <p><strong>Endpoints:</strong></p>
 * <ul>
 *   <li>POST   /api/v1/notification/emails — Create new email</li>
 *   <li>GET    /api/v1/notification/emails — Get all / filter by status/recipient</li>
 *   <li>GET    /api/v1/notification/emails/{id} — Get by ID</li>
 *   <li>GET    /api/v1/notification/emails/pending — Get pending emails</li>
 *   <li>GET    /api/v1/notification/emails/failed — Get failed emails</li>
 *   <li>GET    /api/v1/notification/emails/unopened — Get unopened emails</li>
 *   <li>PUT    /api/v1/notification/emails/{id}/sent — Mark as sent</li>
 *   <li>PUT    /api/v1/notification/emails/{id}/delivered — Mark as delivered</li>
 *   <li>PUT    /api/v1/notification/emails/{id}/opened — Mark as opened</li>
 *   <li>PUT    /api/v1/notification/emails/{id}/failed — Mark as failed</li>
 *   <li>DELETE /api/v1/notification/emails/{id} — Soft delete</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/notification/emails")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Email Log", description = "Email log management and tracking")
public class EmailLogController {

    private final EmailLogService emailLogService;

    @PostMapping
    @Operation(summary = "Create a new email log entry")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Email created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<EmailLogResponseDto> createEmail(@Valid @RequestBody EmailLogCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emailLogService.createEmail(dto));
    }

    @GetMapping
    @Operation(summary = "Get all email logs with optional filters")
    public ResponseEntity<List<EmailLogResponseDto>> getAllEmails(
            @Parameter(description = "Filter by status") @RequestParam(required = false) EmailStatus status,
            @Parameter(description = "Filter by recipient email") @RequestParam(required = false) String recipient) {

        if (status != null) {
            return ResponseEntity.ok(emailLogService.getEmailsByStatus(status));
        }
        if (recipient != null) {
            return ResponseEntity.ok(emailLogService.getEmailsByRecipient(recipient));
        }
        return ResponseEntity.ok(emailLogService.getAllEmails());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get email log by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email found"),
            @ApiResponse(responseCode = "404", description = "Email not found")
    })
    public ResponseEntity<EmailLogResponseDto> getEmailById(@PathVariable Long id) {
        return ResponseEntity.ok(emailLogService.getEmailById(id));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending emails waiting to be sent")
    public ResponseEntity<List<EmailLogResponseDto>> getPendingEmails() {
        return ResponseEntity.ok(emailLogService.getPendingEmails());
    }

    @GetMapping("/failed")
    @Operation(summary = "Get failed emails")
    public ResponseEntity<List<EmailLogResponseDto>> getFailedEmails() {
        return ResponseEntity.ok(emailLogService.getFailedEmails());
    }

    @GetMapping("/unopened")
    @Operation(summary = "Get unopened emails (sent but not opened)")
    public ResponseEntity<List<EmailLogResponseDto>> getUnopenedEmails() {
        return ResponseEntity.ok(emailLogService.getUnopenedEmails());
    }

    @PutMapping("/{id}/sent")
    @Operation(summary = "Mark email as sent")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email marked as sent"),
            @ApiResponse(responseCode = "404", description = "Email not found")
    })
    public ResponseEntity<EmailLogResponseDto> markAsSent(@PathVariable Long id) {
        return ResponseEntity.ok(emailLogService.markAsSent(id));
    }

    @PutMapping("/{id}/delivered")
    @Operation(summary = "Mark email as delivered")
    public ResponseEntity<EmailLogResponseDto> markAsDelivered(@PathVariable Long id) {
        return ResponseEntity.ok(emailLogService.markAsDelivered(id));
    }

    @PutMapping("/{id}/opened")
    @Operation(summary = "Mark email as opened")
    public ResponseEntity<EmailLogResponseDto> markAsOpened(@PathVariable Long id) {
        return ResponseEntity.ok(emailLogService.markAsOpened(id));
    }

    @PutMapping("/{id}/failed")
    @Operation(summary = "Mark email as failed")
    public ResponseEntity<EmailLogResponseDto> markAsFailed(
            @PathVariable Long id,
            @Parameter(description = "Error message") @RequestParam String errorMessage) {
        return ResponseEntity.ok(emailLogService.markAsFailed(id, errorMessage));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete an email log")
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        emailLogService.deleteEmail(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    @Operation(summary = "Count emails by status")
    public ResponseEntity<Long> countEmailsByStatus(
            @Parameter(description = "Email status") @RequestParam EmailStatus status) {
        return ResponseEntity.ok(emailLogService.countEmailsByStatus(status));
    }
}
