package hospital.inventoryservice.exception.global;

import hospital.inventoryservice.exception.drugcategory.CategoryHasChildrenException;
import hospital.inventoryservice.exception.equipment.EquipmentNotAvailableException;
import hospital.inventoryservice.exception.equipmentassignment.EquipmentAlreadyAssignedException;
import hospital.inventoryservice.exception.equipmentassignment.EquipmentNotReturnedException;
import hospital.inventoryservice.exception.purchaseorder.IllegalStatusTransitionException;
import hospital.inventoryservice.exception.stock.InsufficientStockException;
import hospital.inventoryservice.exception.drug.DrugNotFoundException;
import hospital.inventoryservice.exception.drug.DuplicateDrugBarcodeException;
import hospital.inventoryservice.exception.drugcategory.DrugCategoryNotFoundException;
import hospital.inventoryservice.exception.drugcategory.DuplicateDrugCategoryNameException;
import hospital.inventoryservice.exception.stock.StockNotFoundException;
import hospital.inventoryservice.exception.supplier.SupplierNotFoundException;
import hospital.inventoryservice.exception.supplier.DuplicateSupplierEmailException;
import hospital.inventoryservice.exception.purchaseorder.PurchaseOrderNotFoundException;
import hospital.inventoryservice.exception.purchaseorderitem.PurchaseOrderItemNotFoundException;
import hospital.inventoryservice.exception.equipment.EquipmentNotFoundException;
import hospital.inventoryservice.exception.equipment.DuplicateEquipmentSerialNumberException;
import hospital.inventoryservice.exception.equipmentassignment.EquipmentAssignmentNotFoundException;
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
 * Global exception handler for InventoryService.
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
            DrugNotFoundException.class,
            DrugCategoryNotFoundException.class,
            StockNotFoundException.class,
            SupplierNotFoundException.class,
            PurchaseOrderNotFoundException.class,
            PurchaseOrderItemNotFoundException.class,
            EquipmentNotFoundException.class,
            EquipmentAssignmentNotFoundException.class,
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // ==================== 409 - Conflict (Duplicates) ====================

    @ExceptionHandler({
            DuplicateDrugBarcodeException.class,
            DuplicateDrugCategoryNameException.class,
            DuplicateSupplierEmailException.class,
            DuplicateEquipmentSerialNumberException.class,
    })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex) {
        log.error("Duplicate resource: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // ==================== 400 - Bad Request (Business Rules) ====================

    @ExceptionHandler({
            IllegalStatusTransitionException.class,
            CategoryHasChildrenException.class,
            EquipmentNotAvailableException.class,
            InsufficientStockException.class,
            EquipmentAlreadyAssignedException.class,
            EquipmentNotReturnedException.class,
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

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("Validation failed")
                        .errors(errors)
                        .build()
        );
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
                .build();
        return ResponseEntity.status(status).body(response);
    }
}
