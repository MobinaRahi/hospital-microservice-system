package hospital.authservice.exception;

import hospital.authservice.dto.response.ApiResponse;
import hospital.authservice.exception.auth.InvalidTokenException;
import hospital.authservice.exception.permition.DuplicatePermissionException;
import hospital.authservice.exception.permition.PermissionNotFoundException;
import hospital.authservice.exception.role.DuplicateRoleException;
import hospital.authservice.exception.role.RoleNotFoundException;
import hospital.authservice.exception.user.DuplicateEmailException;
import hospital.authservice.exception.user.DuplicateUsernameException;
import hospital.authservice.exception.user.InvalidPasswordException;
import hospital.authservice.exception.user.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==================== 404 - Not Found ====================

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFound(UserNotFoundException ex) {
        log.error("User not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleRoleNotFound(RoleNotFoundException ex) {
        log.error("Role not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PermissionNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlePermissionNotFound(PermissionNotFoundException ex) {
        log.error("Permission not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleJpaEntityNotFound(EntityNotFoundException ex) {
        log.error("Entity not found (JPA): {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // ==================== 400 - Bad Request (Business logic violations) ====================

    @ExceptionHandler({
            InvalidPasswordException.class,
            DuplicateUsernameException.class,
            DuplicateEmailException.class,
            DuplicateRoleException.class,
            DuplicatePermissionException.class,
    })
    public ResponseEntity<ApiResponse<Void>> handleBusinessRuleViolation(RuntimeException ex) {
        log.error("Business rule violation: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // ==================== 401 - Authentication errors ====================

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationError(Exception ex) {
        log.error("Authentication error: {}", ex.getMessage());
        return buildErrorResponse("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidToken(InvalidTokenException ex) {
        log.error("Invalid token: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // ==================== 403 - Forbidden ====================

    @ExceptionHandler({AccessDeniedException.class, AccessDeniedException.class})
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(Exception ex) {
        log.error("Access denied: {}", ex.getMessage());
        return buildErrorResponse("Access denied. You don't have permission.", HttpStatus.FORBIDDEN);
    }

    // ==================== 409 - Conflict (Data integrity) ====================

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Data integrity violation: {}", ex.getMessage());
        String message = "Database constraint violation";
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("Unique") || ex.getMessage().contains("duplicate")) {
                message = "Duplicate entry. This value already exists.";
            } else if (ex.getMessage().contains("foreign key") || ex.getMessage().contains("referential")) {
                message = "This record is referenced by other records and cannot be modified.";
            }
        }
        return buildErrorResponse(message, HttpStatus.CONFLICT);
    }

    // ==================== 400 - Validation errors from @Valid ====================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error("Validation failed: {}", errors);

        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .success(false)
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .data(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error("Constraint violation: {}", errors);

        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .success(false)
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .data(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ==================== 400 - Other request issues ====================

    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(Exception ex) {
        log.error("Bad request: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // ==================== 500 - Fallback for any unhandled exception ====================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return buildErrorResponse("An internal error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ==================== Helper method to build error response ====================

    private ResponseEntity<ApiResponse<Void>> buildErrorResponse(String message, HttpStatus status) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .status(status.value())
                .message(message)
                .data(null)
                .build();
        return ResponseEntity.status(status).body(response);
    }
}