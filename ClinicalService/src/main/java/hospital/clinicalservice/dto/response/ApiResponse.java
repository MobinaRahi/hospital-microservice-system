package hospital.clinicalservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Standard API response wrapper for all endpoints.
 *
 * <p><strong>Response format:</strong></p>
 * <pre>
 * {
 *   "success": true,
 *   "message": "Encounter created successfully",
 *   "data": { ... },
 *   "status": 201,
 *   "timestamp": "2026-07-15T10:30:00"
 * }
 * </pre>
 *
 * <p><strong>Usage:</strong></p>
 * <pre>
 * // Success with data
 * ApiResponse.success(encounterDto, "Created", 201)
 *
 * // Success without data
 * ApiResponse.success("Deleted", 200)
 *
 * // Error
 * ApiResponse.error("Not found", 404)
 * </pre>
 *
 * @author Mobina
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /** Whether the request was successful */
    private final boolean success;

    /** Human-readable message describing the result */
    private final String message;

    /** Response data (null for void operations like delete) */
    private final T data;

    /** Timestamp when the response was generated */
    private final LocalDateTime timestamp;

    /** HTTP status code */
    private final int status;

    // ==================== Success Methods ====================

    /**
     * Creates a success response with data.
     * Used for: GET, POST, PUT endpoints that return data.
     *
     * @param data The response data
     * @param message Success message
     * @param statusCode HTTP status code
     * @return ApiResponse with success=true and data
     */
    public static <T> ApiResponse<T> success(T data, String message, int statusCode) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .status(statusCode)
                .build();
    }

    /**
     * Creates a success response without data.
     * Used for: DELETE, PATCH endpoints that don't return data.
     *
     * @param message Success message
     * @param statusCode HTTP status code
     * @return ApiResponse with success=true and no data
     */
    public static <T> ApiResponse<T> success(String message, int statusCode) {
        return success(null, message, statusCode);
    }

    // ==================== Error Methods ====================

    /**
     * Creates an error response.
     * Used for: All error cases (400, 404, 500, etc.)
     *
     * @param message Error message
     * @param statusCode HTTP status code
     * @return ApiResponse with success=false
     */
    public static <T> ApiResponse<T> error(String message, int statusCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .status(statusCode)
                .build();
    }
}
