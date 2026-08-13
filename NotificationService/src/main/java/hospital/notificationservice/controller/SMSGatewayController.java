package hospital.notificationservice.controller;

import hospital.notificationservice.dto.smsgateway.SMSGatewayCreateDto;
import hospital.notificationservice.dto.smsgateway.SMSGatewayResponseDto;
import hospital.notificationservice.model.enums.SmsStatus;
import hospital.notificationservice.service.SMSGatewayService;
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
 * REST Controller for SMS Gateway management.
 *
 * <p><strong>Endpoints:</strong></p>
 * <ul>
 *   <li>POST   /api/v1/notification/sms — Create new SMS</li>
 *   <li>GET    /api/v1/notification/sms — Get all / filter by status/recipient</li>
 *   <li>GET    /api/v1/notification/sms/{id} — Get by ID</li>
 *   <li>GET    /api/v1/notification/sms/pending — Get pending SMS</li>
 *   <li>GET    /api/v1/notification/sms/failed — Get failed SMS</li>
 *   <li>PUT    /api/v1/notification/sms/{id}/sent — Mark as sent</li>
 *   <li>PUT    /api/v1/notification/sms/{id}/delivered — Mark as delivered</li>
 *   <li>PUT    /api/v1/notification/sms/{id}/failed — Mark as failed</li>
 *   <li>PUT    /api/v1/notification/sms/{id}/cancel — Cancel SMS</li>
 *   <li>DELETE /api/v1/notification/sms/{id} — Soft delete</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/notification/sms")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "SMS Gateway", description = "SMS gateway management and tracking")
public class SMSGatewayController {

    private final SMSGatewayService smsGatewayService;

    @PostMapping
    @Operation(summary = "Create a new SMS message")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "SMS created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<SMSGatewayResponseDto> createSms(@Valid @RequestBody SMSGatewayCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(smsGatewayService.createSms(dto));
    }

    @GetMapping
    @Operation(summary = "Get all SMS messages with optional filters")
    public ResponseEntity<List<SMSGatewayResponseDto>> getAllSms(
            @Parameter(description = "Filter by status") @RequestParam(required = false) SmsStatus status,
            @Parameter(description = "Filter by recipient phone") @RequestParam(required = false) String recipient) {

        if (status != null) {
            return ResponseEntity.ok(smsGatewayService.getSmsByStatus(status));
        }
        if (recipient != null) {
            return ResponseEntity.ok(smsGatewayService.getSmsByRecipient(recipient));
        }
        return ResponseEntity.ok(smsGatewayService.getAllSms());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get SMS by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SMS found"),
            @ApiResponse(responseCode = "404", description = "SMS not found")
    })
    public ResponseEntity<SMSGatewayResponseDto> getSmsById(@PathVariable Long id) {
        return ResponseEntity.ok(smsGatewayService.getSmsById(id));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending SMS messages waiting to be sent")
    public ResponseEntity<List<SMSGatewayResponseDto>> getPendingSms() {
        return ResponseEntity.ok(smsGatewayService.getPendingSms());
    }

    @GetMapping("/failed")
    @Operation(summary = "Get failed SMS messages")
    public ResponseEntity<List<SMSGatewayResponseDto>> getFailedSms() {
        return ResponseEntity.ok(smsGatewayService.getFailedSms());
    }

    @PutMapping("/{id}/sent")
    @Operation(summary = "Mark SMS as sent")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SMS marked as sent"),
            @ApiResponse(responseCode = "404", description = "SMS not found")
    })
    public ResponseEntity<SMSGatewayResponseDto> markAsSent(
            @PathVariable Long id,
            @Parameter(description = "Provider message ID") @RequestParam String providerMessageId) {
        return ResponseEntity.ok(smsGatewayService.markAsSent(id, providerMessageId));
    }

    @PutMapping("/{id}/delivered")
    @Operation(summary = "Mark SMS as delivered")
    public ResponseEntity<SMSGatewayResponseDto> markAsDelivered(@PathVariable Long id) {
        return ResponseEntity.ok(smsGatewayService.markAsDelivered(id));
    }

    @PutMapping("/{id}/failed")
    @Operation(summary = "Mark SMS as failed")
    public ResponseEntity<SMSGatewayResponseDto> markAsFailed(
            @PathVariable Long id,
            @Parameter(description = "Error message") @RequestParam String errorMessage) {
        return ResponseEntity.ok(smsGatewayService.markAsFailed(id, errorMessage));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel a pending SMS")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SMS cancelled"),
            @ApiResponse(responseCode = "400", description = "Cannot cancel in current status")
    })
    public ResponseEntity<SMSGatewayResponseDto> cancelSms(@PathVariable Long id) {
        return ResponseEntity.ok(smsGatewayService.cancelSms(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete an SMS")
    public ResponseEntity<Void> deleteSms(@PathVariable Long id) {
        smsGatewayService.deleteSms(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    @Operation(summary = "Count SMS by status")
    public ResponseEntity<Long> countSmsByStatus(
            @Parameter(description = "SMS status") @RequestParam SmsStatus status) {
        return ResponseEntity.ok(smsGatewayService.countSmsByStatus(status));
    }
}
