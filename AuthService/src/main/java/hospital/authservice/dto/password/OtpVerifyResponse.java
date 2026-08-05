package hospital.authservice.dto.password;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response returned after successful OTP verification (Step 2).
 * Contains a temporary reset token that must be used in Step 3 to set the new password.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpVerifyResponse {

    /**
     * Human-readable success message.
     */
    private String message;

    /**
     * Temporary reset token (UUID).
     * Must be passed in Step 3 along with the new password.
     * Valid for 10 minutes.
     */
    private String resetToken;

    /**
     * How many minutes until the reset token expires.
     */
    private int resetTokenExpiresInMinutes;
}
