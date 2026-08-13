package hospital.labservice.exception.global;

import hospital.labservice.exception.labequipment.DuplicateLabEquipmentSerialNumberException;
import hospital.labservice.exception.labequipment.LabEquipmentNotFoundException;
import hospital.labservice.exception.labrequest.DuplicateLabRequestNumberException;
import hospital.labservice.exception.labrequest.IllegalLabRequestStatusException;
import hospital.labservice.exception.labrequest.LabRequestNotFoundException;
import hospital.labservice.exception.labrequestitem.LabRequestItemNotFoundException;
import hospital.labservice.exception.labresult.LabResultNotFoundException;
import hospital.labservice.exception.labtechnician.DuplicateLabTechnicianEmployeeCodeException;
import hospital.labservice.exception.labtechnician.DuplicateLabTechnicianUserIdException;
import hospital.labservice.exception.labtechnician.LabTechnicianNotFoundException;
import hospital.labservice.exception.labtest.DuplicateLabTestCodeException;
import hospital.labservice.exception.labtest.LabTestNotFoundException;
import hospital.labservice.exception.sample.DuplicateSampleNumberException;
import hospital.labservice.exception.sample.SampleNotFoundException;
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
 * Global exception handler for LabService.
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
            LabTestNotFoundException.class,
            LabRequestNotFoundException.class,
            LabRequestItemNotFoundException.class,
            SampleNotFoundException.class,
            LabResultNotFoundException.class,
            LabEquipmentNotFoundException.class,
            LabTechnicianNotFoundException.class,
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // ==================== 409 - Conflict (Duplicates) ====================

    @ExceptionHandler({
            DuplicateLabTestCodeException.class,
            DuplicateLabRequestNumberException.class,
            DuplicateSampleNumberException.class,
            DuplicateLabEquipmentSerialNumberException.class,
            DuplicateLabTechnicianUserIdException.class,
            DuplicateLabTechnicianEmployeeCodeException.class,
    })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex) {
        log.error("Duplicate resource: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // ==================== 400 - Bad Request (Business Rules) ====================

    @ExceptionHandler({
            IllegalLabRequestStatusException.class,
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
