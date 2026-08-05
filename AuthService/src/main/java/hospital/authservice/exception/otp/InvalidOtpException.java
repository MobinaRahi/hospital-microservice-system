package hospital.authservice.exception.otp;

/**
 * Thrown when the OTP code entered by the user does not match.
 * The user can retry up to 3 times before the OTP is locked.
 *
 * @author MobinaRahi
 */
public class InvalidOtpException extends RuntimeException {

    public InvalidOtpException() {
        super("Invalid OTP code. Please check and try again.");
    }

    public InvalidOtpException(String message) {
        super(message);
    }
}
