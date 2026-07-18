package hospital.coreservice.exception;

import hospital.coreservice.dto.response.ApiResponse;
import hospital.coreservice.exception.appointment.*;
import hospital.coreservice.exception.common.InvalidSearchParameterException;
import hospital.coreservice.exception.department.DepartmentHasNoHeadDoctorException;
import hospital.coreservice.exception.department.DepartmentHasNoHeadNurseException;
import hospital.coreservice.exception.department.DepartmentNotFoundException;
import hospital.coreservice.exception.doctor.DoctorNotAvailableException;
import hospital.coreservice.exception.doctor.DoctorNotFoundException;
import hospital.coreservice.exception.doctor_schedule.DoctorScheduleNotFoundException;
import hospital.coreservice.exception.doctor_schedule.DuplicateDoctorScheduleException;
import hospital.coreservice.exception.nurse.NurseAlreadyExistsException;
import hospital.coreservice.exception.nurse.NurseNotFoundException;
import hospital.coreservice.exception.patient.*;
import hospital.coreservice.exception.room.DuplicateRoomNumberException;
import hospital.coreservice.exception.room.RoomFullException;
import hospital.coreservice.exception.room.RoomNotAvailableException;
import hospital.coreservice.exception.room.RoomNotFoundException;
import hospital.coreservice.exception.shift.DuplicateShiftNameException;
import hospital.coreservice.exception.shift.ShiftNotFoundException;
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

/**
 * Global exception handler for CoreService microservice.
 * Converts domain-specific exceptions to standardized HTTP responses.
 */
@Slf4j
@RestControllerAdvice
/**
 * Global exception handler for CoreService REST controllers.
 *
 * @author Mobina
 */
public class GlobalExceptionHandler {

    // ==================== 404 - Not Found ====================

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleAppointmentNotFound(AppointmentNotFoundException ex) {
        log.error("Appointment not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleDepartmentNotFound(DepartmentNotFoundException ex) {
        log.error("Department not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleDoctorNotFound(DoctorNotFoundException ex) {
        log.error("Doctor not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DoctorScheduleNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleDoctorScheduleNotFound(DoctorScheduleNotFoundException ex) {
        log.error("Doctor schedule not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NurseNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNurseNotFound(NurseNotFoundException ex) {
        log.error("Nurse not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlePatientNotFound(PatientNotFoundException ex) {
        log.error("Patient not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleRoomNotFound(RoomNotFoundException ex) {
        log.error("Room not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ShiftNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleShiftNotFound(ShiftNotFoundException ex) {
        log.error("Shift not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleJpaEntityNotFound(EntityNotFoundException ex) {
        log.error("Entity not found (JPA): {}", ex.getMessage());
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
            PatientsArchiveException.class,
            RoomFullException.class,
            RoomNotAvailableException.class,
            DuplicateDoctorScheduleException.class,
            DuplicateShiftNameException.class,
            DuplicateNationalIdException.class,
            DuplicatePhoneNumberException.class,
            DuplicateRoomNumberException.class,
            NurseAlreadyExistsException.class,
            DepartmentHasNoHeadDoctorException.class,
            DepartmentHasNoHeadNurseException.class,
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

    // ==================== 403 - Forbidden ====================

    @ExceptionHandler({AccessDeniedException.class, org.springframework.security.access.AccessDeniedException.class})
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