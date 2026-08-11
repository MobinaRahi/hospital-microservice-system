package hospital.billingservice.dto.response;

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
 *   "message": "Invoice created successfully",
 *   "data": { ... },
 *   "status": 201,
 *   "timestamp": "2026-01-01T10:30:00"
 * }
 * </pre>
 *
 * @param <T> the type of data being returned
 * @author MobinaRahi
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final LocalDateTime timestamp;
    private final int status;

    // ==================== Success Methods ====================

    public static <T> ApiResponse<T> success(T data, String message, int statusCode) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .status(statusCode)
                .build();
    }

    public static <T> ApiResponse<T> success(String message, int statusCode) {
        return success(null, message, statusCode);
    }

    // ==================== Error Methods ====================

    public static <T> ApiResponse<T> error(String message, int statusCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .status(statusCode)
                .build();
    }
}
