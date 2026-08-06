package hospital.inventoryservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard API response wrapper for all endpoints.
 * Ensures consistent response format across the service.
 *
 * <p><strong>Success Response Format:</strong></p>
 * <pre>
 * {
 *   "success": true,
 *   "message": "Drug created successfully",
 *   "status": 201,
 *   "data": { ... }
 * }
 * </pre>
 *
 * <p><strong>Error Response Format:</strong></p>
 * <pre>
 * {
 *   "success": false,
 *   "message": "Drug not found with id: 123",
 *   "status": 404,
 *   "data": null
 * }
 * </pre>
 *
 * @param <T> the type of data being returned
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /**
     * Whether the operation was successful.
     */
    private boolean success;

    /**
     * Human-readable message describing the result.
     */
    private String message;

    /**
     * HTTP status code.
     */
    private int status;

    /**
     * Response data (null for error responses).
     */
    private T data;

    /**
     * Creates a success response with data.
     */
    public static <T> ApiResponse<T> success(T data, String message, int status) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .status(status)
                .data(data)
                .build();
    }

    /**
     * Creates a success response without data.
     */
    public static <T> ApiResponse<T> success(String message, int status) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .status(status)
                .build();
    }

    /**
     * Creates an error response.
     */
    public static <T> ApiResponse<T> error(String message, int status) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .status(status)
                .build();
    }
}
