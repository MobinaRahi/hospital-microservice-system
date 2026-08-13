package hospital.notificationservice.controller;

import hospital.notificationservice.dto.notificationtemplate.NotificationTemplateCreateDto;
import hospital.notificationservice.dto.notificationtemplate.NotificationTemplateResponseDto;
import hospital.notificationservice.dto.notificationtemplate.NotificationTemplateUpdateDto;
import hospital.notificationservice.model.enums.TemplateType;
import hospital.notificationservice.service.NotificationTemplateService;
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
 * REST Controller for Notification Template management.
 *
 * <p><strong>Endpoints:</strong></p>
 * <ul>
 *   <li>POST   /api/v1/notification/templates — Create new template</li>
 *   <li>GET    /api/v1/notification/templates — Get all / filter by type/active</li>
 *   <li>GET    /api/v1/notification/templates/{id} — Get by ID</li>
 *   <li>GET    /api/v1/notification/templates/name/{name} — Get by name</li>
 *   <li>GET    /api/v1/notification/templates/type/{type} — Get by type</li>
 *   <li>GET    /api/v1/notification/templates/active — Get active templates</li>
 *   <li>GET    /api/v1/notification/templates/active/type/{type} — Get active by type</li>
 *   <li>GET    /api/v1/notification/templates/search — Search by name</li>
 *   <li>PUT    /api/v1/notification/templates/{id} — Update template</li>
 *   <li>PUT    /api/v1/notification/templates/{id}/activate — Activate template</li>
 *   <li>PUT    /api/v1/notification/templates/{id}/deactivate — Deactivate template</li>
 *   <li>DELETE /api/v1/notification/templates/{id} — Soft delete</li>
 *   <li>GET    /api/v1/notification/templates/check-name — Check name existence</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/notification/templates")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notification Templates", description = "Notification template management for SMS and Email")
public class NotificationTemplateController {

    private final NotificationTemplateService notificationTemplateService;

    @PostMapping
    @Operation(summary = "Create a new notification template")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Template created"),
            @ApiResponse(responseCode = "409", description = "Template name already exists"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<NotificationTemplateResponseDto> createTemplate(
            @Valid @RequestBody NotificationTemplateCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationTemplateService.createTemplate(dto));
    }

    @GetMapping
    @Operation(summary = "Get all templates with optional filters")
    public ResponseEntity<List<NotificationTemplateResponseDto>> getAllTemplates(
            @Parameter(description = "Filter by type") @RequestParam(required = false) TemplateType type,
            @Parameter(description = "Filter by active status") @RequestParam(required = false) Boolean active) {

        if (Boolean.TRUE.equals(active) && type != null) {
            return ResponseEntity.ok(notificationTemplateService.getActiveTemplatesByType(type));
        }
        if (Boolean.TRUE.equals(active)) {
            return ResponseEntity.ok(notificationTemplateService.getActiveTemplates());
        }
        if (type != null) {
            return ResponseEntity.ok(notificationTemplateService.getTemplatesByType(type));
        }
        return ResponseEntity.ok(notificationTemplateService.getAllTemplates());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get template by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Template found"),
            @ApiResponse(responseCode = "404", description = "Template not found")
    })
    public ResponseEntity<NotificationTemplateResponseDto> getTemplateById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationTemplateService.getTemplateById(id));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get template by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Template found"),
            @ApiResponse(responseCode = "404", description = "Template not found")
    })
    public ResponseEntity<NotificationTemplateResponseDto> getTemplateByName(@PathVariable String name) {
        return ResponseEntity.ok(notificationTemplateService.getTemplateByName(name));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get templates by type (SMS or Email)")
    public ResponseEntity<List<NotificationTemplateResponseDto>> getTemplatesByType(@PathVariable TemplateType type) {
        return ResponseEntity.ok(notificationTemplateService.getTemplatesByType(type));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active templates")
    public ResponseEntity<List<NotificationTemplateResponseDto>> getActiveTemplates() {
        return ResponseEntity.ok(notificationTemplateService.getActiveTemplates());
    }

    @GetMapping("/active/type/{type}")
    @Operation(summary = "Get active templates by type")
    public ResponseEntity<List<NotificationTemplateResponseDto>> getActiveTemplatesByType(@PathVariable TemplateType type) {
        return ResponseEntity.ok(notificationTemplateService.getActiveTemplatesByType(type));
    }

    @GetMapping("/search")
    @Operation(summary = "Search templates by name pattern")
    public ResponseEntity<List<NotificationTemplateResponseDto>> searchTemplates(
            @Parameter(description = "Name search pattern") @RequestParam String name) {
        return ResponseEntity.ok(notificationTemplateService.searchTemplatesByName(name));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing template")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Template updated"),
            @ApiResponse(responseCode = "404", description = "Template not found")
    })
    public ResponseEntity<NotificationTemplateResponseDto> updateTemplate(
            @PathVariable Long id,
            @Valid @RequestBody NotificationTemplateUpdateDto dto) {
        return ResponseEntity.ok(notificationTemplateService.updateTemplate(id, dto));
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate a template")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Template activated"),
            @ApiResponse(responseCode = "404", description = "Template not found")
    })
    public ResponseEntity<NotificationTemplateResponseDto> activateTemplate(@PathVariable Long id) {
        return ResponseEntity.ok(notificationTemplateService.activateTemplate(id));
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a template")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Template deactivated"),
            @ApiResponse(responseCode = "404", description = "Template not found")
    })
    public ResponseEntity<NotificationTemplateResponseDto> deactivateTemplate(@PathVariable Long id) {
        return ResponseEntity.ok(notificationTemplateService.deactivateTemplate(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a template")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        notificationTemplateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-name")
    @Operation(summary = "Check if a template name already exists")
    public ResponseEntity<Boolean> checkTemplateName(
            @Parameter(description = "Template name to check") @RequestParam String name) {
        return ResponseEntity.ok(notificationTemplateService.templateNameExists(name));
    }
}
