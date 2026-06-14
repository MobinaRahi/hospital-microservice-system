package hospital.coreservice.exception;

import hospital.coreservice.dto.api.ApiResponse;
import hospital.coreservice.exception.appointment.*;
import hospital.coreservice.exception.common.InvalidSearchParameterException;
import hospital.coreservice.exception.department.*;
import hospital.coreservice.exception.doctor.DoctorNotAvailableException;
import hospital.coreservice.exception.doctor.DoctorNotFoundException;
import hospital.coreservice.exception.doctor_schedule.DoctorScheduleNotFoundException;
import hospital.coreservice.exception.doctor_schedule.DuplicateDoctorScheduleException;
import hospital.coreservice.exception.nurse.NurseAlreadyExistsException;
import hospital.coreservice.exception.nurse.NurseNotFoundException;
import hospital.coreservice.exception.patient.*;
import hospital.coreservice.exception.room.*;
import hospital.coreservice.exception.shift.DuplicateShiftNameException;
import hospital.coreservice.exception.shift.ShiftNotFoundException;
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

/**
 * Global exception handler for CoreService microservice.
 * Converts domain-specific exceptions to standardized HTTP responses.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==================== 404 - Not Found ====================

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<ApiResponse> handleAppointmentNotFound(AppointmentNotFoundException ex) {
        log.error("Appointment not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ApiResponse> handleDepartmentNotFound(DepartmentNotFoundException ex) {
        log.error("Department not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<ApiResponse> handleDoctorNotFound(DoctorNotFoundException ex) {
        log.error("Doctor not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DoctorScheduleNotFoundException.class)
    public ResponseEntity<ApiResponse> handleDoctorScheduleNotFound(DoctorScheduleNotFoundException ex) {
        log.error("Doctor schedule not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NurseNotFoundException.class)
    public ResponseEntity<ApiResponse> handleNurseNotFound(NurseNotFoundException ex) {
        log.error("Nurse not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ApiResponse> handlePatientNotFound(PatientNotFoundException ex) {
        log.error("Patient not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ApiResponse> handleRoomNotFound(RoomNotFoundException ex) {
        log.error("Room not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ShiftNotFoundException.class)
    public ResponseEntity<ApiResponse> handleShiftNotFound(ShiftNotFoundException ex) {
        log.error("Shift not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    // ==================== 400 - Bad Request (Business logic violations) ====================

    @ExceptionHandler({
            InvalidAppointmentStateException.class,
            InvalidCancelStateException.class,
            InvalidCheckInStateException.class,
            InvalidCompleteStateException.class,
            InvalidSearchParameterException.class,
            DoctorNotAvailableException.class,
            PatientAppointmentConflictException.class,
            PatientAlreadyActiveException.class,
            PatientNotInRoomException.class,
            PatientAlreadyHasRoomException.class,
            RoomFullException.class,
            RoomNotAvailableException.class,
            DuplicateDoctorScheduleException.class,
            DuplicateShiftNameException.class,
            DuplicateNationalIdException.class,
            DuplicatePhoneNumberException.class,
            DuplicateRoomNumberException.class,
            NurseAlreadyExistsException.class,
            DepartmentHasNoHeadDoctorException.class,
            DepartmentHasNoHeadNurseException.class
    })
    public ResponseEntity<ApiResponse> handleBusinessRuleViolation(RuntimeException ex) {
        log.error("Business rule violation: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // ==================== 409 - Conflict (Data integrity) ====================

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Data integrity violation: {}", ex.getMessage());
        String message = "Database constraint violation";
        if (ex.getMessage() != null && (ex.getMessage().contains("Unique") || ex.getMessage().contains("duplicate"))) {
            message = "Duplicate entry. This value already exists.";
        }
        return buildErrorResponse(message, HttpStatus.CONFLICT);
    }

    // ==================== 400 - Validation errors from @Valid ====================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error("Validation failed: {}", errors);
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .data(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error("Constraint violation: {}", errors);
        ApiResponse response = ApiResponse.builder()
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
    public ResponseEntity<ApiResponse> handleBadRequest(Exception ex) {
        log.error("Bad request: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // ==================== 401 - Authentication errors ====================

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiResponse> handleAuthenticationError(Exception ex) {
        log.error("Authentication error: {}", ex.getMessage());
        return buildErrorResponse("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }

    // ==================== 403 - Forbidden ====================

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDenied(AccessDeniedException ex) {
        log.error("Access denied: {}", ex.getMessage());
        return buildErrorResponse("Access denied. You don't have permission.", HttpStatus.FORBIDDEN);
    }

    // ==================== 500 - Fallback for any unhandled exception ====================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return buildErrorResponse("An internal error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> handleJpaEntityNotFound(jakarta.persistence.EntityNotFoundException ex) {
        log.error("Entity not found (JPA): {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // ==================== Helper method to build error response ====================

    private ResponseEntity<ApiResponse> buildErrorResponse(String message, HttpStatus status) {
        ApiResponse response = ApiResponse.builder()
                .success(false)
                .status(status.value())
                .message(message)
                .data(null)
                .build();
        return ResponseEntity.status(status).body(response);
    }
}