package hospital.authservice.exception.otp;

/**
 * Thrown when the reset token (received after OTP verification) is invalid or expired.
 * The user must restart the password recovery process from Step 1.
 *
 * @author MobinaRahi
 */
public class InvalidResetTokenException extends RuntimeException {

    public InvalidResetTokenException() {
        super("Invalid or expired reset token. Please restart the password recovery process.");
    }

    public InvalidResetTokenException(String message) {
        super(message);
    }
}
