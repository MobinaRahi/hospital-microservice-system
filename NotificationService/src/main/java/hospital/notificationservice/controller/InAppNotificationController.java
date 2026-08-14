package hospital.notificationservice.controller;

import hospital.notificationservice.dto.inappnotification.InAppNotificationCreateDto;
import hospital.notificationservice.dto.inappnotification.InAppNotificationResponseDto;
import hospital.notificationservice.dto.inappnotification.InAppNotificationUpdateDto;
import hospital.notificationservice.model.enums.NotificationType;
import hospital.notificationservice.service.InAppNotificationService;
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
 * REST Controller for In-App Notification management.
 *
 * <p><strong>Endpoints:</strong></p>
 * <ul>
 *   <li>POST   /api/v1/notification/in-app — Create new notification</li>
 *   <li>GET    /api/v1/notification/in-app — Get all / filter by user/type</li>
 *   <li>GET    /api/v1/notification/in-app/{id} — Get by ID</li>
 *   <li>GET    /api/v1/notification/in-app/user/{userId} — Get by user</li>
 *   <li>GET    /api/v1/notification/in-app/user/{userId}/unread — Get unread by user</li>
 *   <li>GET    /api/v1/notification/in-app/type/{type} — Get by type</li>
 *   <li>GET    /api/v1/notification/in-app/related/{relatedId} — Get by related entity</li>
 *   <li>PUT    /api/v1/notification/in-app/{id} — Update notification</li>
 *   <li>PUT    /api/v1/notification/in-app/{id}/read — Mark as read</li>
 *   <li>DELETE /api/v1/notification/in-app/{id} — Soft delete</li>
 *   <li>GET    /api/v1/notification/in-app/user/{userId}/count — Count unread</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/notification/in-app")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "In-App Notifications", description = "In-app notification management for users")
public class InAppNotificationController {

    private final InAppNotificationService inAppNotificationService;

    @PostMapping
    @Operation(summary = "Create a new in-app notification")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Notification created"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<InAppNotificationResponseDto> createNotification(
            @Valid @RequestBody InAppNotificationCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inAppNotificationService.createNotification(dto));
    }

    @GetMapping
    @Operation(summary = "Get all notifications")
    public ResponseEntity<List<InAppNotificationResponseDto>> getAllNotifications() {
        return ResponseEntity.ok(inAppNotificationService.getAllNotifications());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notification by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notification found"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    public ResponseEntity<InAppNotificationResponseDto> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(inAppNotificationService.getNotificationById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get notifications for a specific user")
    public ResponseEntity<List<InAppNotificationResponseDto>> getNotificationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(inAppNotificationService.getNotificationsByUser(userId));
    }

    @GetMapping("/user/{userId}/unread")
    @Operation(summary = "Get unread notifications for a user")
    public ResponseEntity<List<InAppNotificationResponseDto>> getUnreadNotificationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(inAppNotificationService.getUnreadNotificationsByUser(userId));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get notifications by type")
    public ResponseEntity<List<InAppNotificationResponseDto>> getNotificationsByType(@PathVariable NotificationType type) {
        return ResponseEntity.ok(inAppNotificationService.getNotificationsByType(type));
    }

    @GetMapping("/user/{userId}/type/{type}")
    @Operation(summary = "Get notifications for a user filtered by type")
    public ResponseEntity<List<InAppNotificationResponseDto>> getNotificationsByUserAndType(
            @PathVariable Long userId, @PathVariable NotificationType type) {
        return ResponseEntity.ok(inAppNotificationService.getNotificationsByUserAndType(userId, type));
    }

    @GetMapping("/related/{relatedId}")
    @Operation(summary = "Get notifications related to a specific entity")
    public ResponseEntity<List<InAppNotificationResponseDto>> getNotificationsByRelatedId(@PathVariable Long relatedId) {
        return ResponseEntity.ok(inAppNotificationService.getNotificationsByRelatedId(relatedId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing notification")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notification updated"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    public ResponseEntity<InAppNotificationResponseDto> updateNotification(
            @PathVariable Long id,
            @Valid @RequestBody InAppNotificationUpdateDto dto) {
        return ResponseEntity.ok(inAppNotificationService.updateNotification(id, dto));
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Mark a notification as read")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notification marked as read"),
            @ApiResponse(responseCode = "400", description = "Already read"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    public ResponseEntity<InAppNotificationResponseDto> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(inAppNotificationService.markAsRead(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a notification")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        inAppNotificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/count")
    @Operation(summary = "Count unread notifications for a user")
    public ResponseEntity<Long> countUnreadNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(inAppNotificationService.countUnreadNotificationsByUser(userId));
    }
}
