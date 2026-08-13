package hospital.adminservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Standard API response wrapper for all endpoints.
 * Ensures consistent response format across the service.
 *
 * <p><strong>Response Format:</strong></p>
 * <pre>
 * {
 *   "success": true,
 *   "message": "Hospital created successfully",
 *   "data": { ... },
 *   "timestamp": "2026-08-12T10:30:00",
 *   "status": 201
 * }
 * </pre>
 *
 * <p><strong>Error Response Format:</strong></p>
 * <pre>
 * {
 *   "success": false,
 *   "message": "Hospital not found",
 *   "timestamp": "2026-08-12T10:30:00",
 *   "status": 404
 * }
 * </pre>
 *
 * <p><strong>Usage:</strong></p>
 * <pre>
 * // Success with data
 * ApiResponse.success(hospitalDto, "Hospital created", 201)
 *
 * // Success without data
 * ApiResponse.success("Hospital deleted", 200)
 *
 * // Error response
 * ApiResponse.error("Hospital not found", 404)
 * </pre>
 *
 * @param <T> the type of data being returned
 * @author MobinaRahi
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /** Whether the request was successful */
    private boolean success;

    /** Human-readable message describing the result */
    private String message;

    /** Response data (null for void operations) */
    private T data;

    /** Timestamp when the response was generated */
    private LocalDateTime timestamp;

    /** HTTP status code */
    private int status;

    /**
     * Creates a success response with data.
     *
     * @param data      the response data
     * @param message   success message
     * @param status    HTTP status code
     * @param <T>       data type
     * @return ApiResponse with success=true
     */
    public static <T> ApiResponse<T> success(T data, String message, int status) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .status(status)
                .build();
    }

    /**
     * Creates a success response without data.
     *
     * @param message success message
     * @param status  HTTP status code
     * @param <T>     data type
     * @return ApiResponse with success=true and data=null
     */
    public static <T> ApiResponse<T> success(String message, int status) {
        return success(null, message, status);
    }

    /**
     * Creates an error response.
     *
     * @param message error message
     * @param status  HTTP status code
     * @param <T>     data type
     * @return ApiResponse with success=false
     */
    public static <T> ApiResponse<T> error(String message, int status) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .status(status)
                .build();
    }
}
