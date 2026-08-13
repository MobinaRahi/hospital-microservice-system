package hospital.notificationservice.exception.global;

import hospital.notificationservice.exception.emaillog.EmailCancelException;
import hospital.notificationservice.exception.emaillog.EmailLogNotFoundException;
import hospital.notificationservice.exception.inappnotification.AlreadyReadNotificationException;
import hospital.notificationservice.exception.inappnotification.InAppNotificationNotFoundException;
import hospital.notificationservice.exception.notificationtemplate.DuplicateTemplateNameException;
import hospital.notificationservice.exception.notificationtemplate.InactiveTemplateException;
import hospital.notificationservice.exception.notificationtemplate.NotificationTemplateNotFoundException;
import hospital.notificationservice.exception.smsgateway.SmsCancelException;
import hospital.notificationservice.exception.smsgateway.SMSGatewayNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for NotificationService.
 *
 * <p><strong>Maps exceptions to HTTP status codes:</strong></p>
 * <ul>
 *   <li>400 Bad Request — Validation errors, business rule violations</li>
 *   <li>404 Not Found — Resource not found</li>
 *   <li>409 Conflict — Duplicate resources</li>
 *   <li>401 Unauthorized — Authentication errors</li>
 *   <li>403 Forbidden — Authorization errors</li>
 *   <li>500 Internal Server Error — Unexpected errors</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==================== 404 - Not Found ====================

    @ExceptionHandler({
            SMSGatewayNotFoundException.class,
            EmailLogNotFoundException.class,
            InAppNotificationNotFoundException.class,
            NotificationTemplateNotFoundException.class,
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // ==================== 409 - Conflict (Duplicates) ====================

    @ExceptionHandler({
            DuplicateTemplateNameException.class,
    })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex) {
        log.error("Duplicate resource: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // ==================== 400 - Bad Request (Business Rules) ====================

    @ExceptionHandler({
            SmsCancelException.class,
            EmailCancelException.class,
            AlreadyReadNotificationException.class,
            InactiveTemplateException.class,
            IllegalStateException.class,
            IllegalArgumentException.class,
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex) {
        log.error("Bad request: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ==================== 400 - Validation ====================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error("Validation failed: {}", errors);

        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .errors(errors)
                .timestamp(java.time.LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ==================== 401 - Authentication ====================

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        log.error("Bad credentials: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid username or password");
    }

    // ==================== 403 - Forbidden ====================

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        log.error("Access denied: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Access denied. You don't have permission.");
    }

    // ==================== 500 - Internal Server Error ====================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later.");
    }

    // ==================== Helper ====================

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .status(status.value())
                .message(message)
                .timestamp(java.time.LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(response);
    }
}
