package hospital.billingservice.exception.global;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response format for all API endpoints.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private boolean success;
    private int status;
    private String message;
    private Map<String, String> errors;
    private LocalDateTime timestamp;
}
