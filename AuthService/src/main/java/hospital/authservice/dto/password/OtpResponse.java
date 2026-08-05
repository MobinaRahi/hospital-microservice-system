package hospital.authservice.dto.password;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response returned after successful OTP generation (Step 1).
 * Contains a message, expiry info, and the masked email for confirmation.
 *
 * @author MobinaRahi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpResponse {

    /**
     * Human-readable message (e.g. "OTP code sent to your email").
     */
    private String message;

    /**
     * How many seconds until this OTP expires.
     */
    private int expiresInSeconds;

    /**
     * Masked email for user confirmation (e.g. "mob***@gmail.com").
     */
    private String maskedEmail;
}
