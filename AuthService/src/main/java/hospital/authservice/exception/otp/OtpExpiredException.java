package hospital.authservice.exception.otp;

/**
 * Thrown when the OTP code has expired (more than 5 minutes since generation).
 * The user must request a new OTP.
 *
 * @author MobinaRahi
 */
public class OtpExpiredException extends RuntimeException {

    public OtpExpiredException() {
        super("OTP code has expired. Please request a new one.");
    }

    public OtpExpiredException(String message) {
        super(message);
    }
}
