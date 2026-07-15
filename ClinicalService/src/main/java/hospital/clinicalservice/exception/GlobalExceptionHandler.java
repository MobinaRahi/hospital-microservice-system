package hospital.clinicalservice.exception;

import hospital.clinicalservice.dto.response.ApiResponse;
import hospital.clinicalservice.exception.allergy.AllergyNotFoundException;
import hospital.clinicalservice.exception.diagnosis.DiagnosisNotFoundException;
import hospital.clinicalservice.exception.encounter.EncounterNotFoundException;
import hospital.clinicalservice.exception.encounter.InvalidEncounterStateException;
import hospital.clinicalservice.exception.nursingnote.NursingNoteNotFoundException;
import hospital.clinicalservice.exception.observation.ObservationNotFoundException;
import hospital.clinicalservice.exception.prescription.InvalidPrescriptionStateException;
import hospital.clinicalservice.exception.prescription.PrescriptionItemNotFoundException;
import hospital.clinicalservice.exception.prescription.PrescriptionNotFoundException;
import hospital.clinicalservice.exception.triage.TriageNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==================== 404 - Not Found ====================

    @ExceptionHandler(EncounterNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleEncounterNotFound(EncounterNotFoundException ex) {
        log.error("Encounter not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(DiagnosisNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleDiagnosisNotFound(DiagnosisNotFoundException ex) {
        log.error("Diagnosis not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(ObservationNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleObservationNotFound(ObservationNotFoundException ex) {
        log.error("Observation not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(PrescriptionNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlePrescriptionNotFound(PrescriptionNotFoundException ex) {
        log.error("Prescription not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(PrescriptionItemNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlePrescriptionItemNotFound(PrescriptionItemNotFoundException ex) {
        log.error("Prescription item not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(NursingNoteNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNursingNoteNotFound(NursingNoteNotFoundException ex) {
        log.error("Nursing note not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(TriageNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleTriageNotFound(TriageNotFoundException ex) {
        log.error("Triage not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(AllergyNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleAllergyNotFound(AllergyNotFoundException ex) {
        log.error("Allergy not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    // ==================== 400 - Bad Request ====================

    @ExceptionHandler(InvalidEncounterStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidEncounterState(InvalidEncounterStateException ex) {
        log.error("Invalid encounter state: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(InvalidPrescriptionStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidPrescriptionState(InvalidPrescriptionStateException ex) {
        log.error("Invalid prescription state: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");
        log.error("Validation error: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message, HttpStatus.BAD_REQUEST.value()));
    }

    // ==================== 500 - Internal Server Error ====================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
