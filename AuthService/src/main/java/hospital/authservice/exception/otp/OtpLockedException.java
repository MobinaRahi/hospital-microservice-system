package hospital.authservice.exception.otp;

/**
 * Thrown when the OTP has been locked due to too many failed verification attempts.
 * The user must request a new OTP to continue.
 *
 * @author MobinaRahi
 */
public class OtpLockedException extends RuntimeException {

    public OtpLockedException() {
        super("This OTP has been locked due to too many failed attempts. Please request a new OTP.");
    }

    public OtpLockedException(String message) {
        super(message);
    }
}
