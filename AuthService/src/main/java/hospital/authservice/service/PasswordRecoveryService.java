package hospital.authservice.service;

import hospital.authservice.dto.password.*;

/**
 * Service for password recovery using OTP (One-Time Password).
 *
 * <p><strong>3-Step Flow:</strong></p>
 * <ol>
 *   <li>User requests OTP via email → {@link #requestOtp(ForgotPasswordRequest)}</li>
 *   <li>User verifies OTP code → {@link #verifyOtp(VerifyOtpRequest)}</li>
 *   <li>User sets new password → {@link #resetPassword(ResetPasswordWithOtpRequest)}</li>
 * </ol>
 *
 * <p><strong>Security:</strong></p>
 * <ul>
 *   <li>OTP is cryptographically random 6-digit code</li>
 *   <li>OTP expires after 5 minutes</li>
 *   <li>OTP is locked after 3 failed attempts</li>
 *   <li>Reset token is a UUID, valid for 10 minutes</li>
 *   <li>New password is BCrypt-hashed before storage</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface PasswordRecoveryService {

    /**
     * Step 1: Generates and "sends" a 6-digit OTP to the user's email.
     * Invalidates any existing active OTPs for this user.
     *
     * @param request contains the user's email
     * @return response with masked email and expiry info
     */
    OtpResponse requestOtp(ForgotPasswordRequest request);

    /**
     * Step 2: Verifies the OTP code entered by the user.
     * On success, returns a temporary reset token for Step 3.
     *
     * @param request contains email and 6-digit OTP code
     * @return response with reset token
     */
    OtpVerifyResponse verifyOtp(VerifyOtpRequest request);

    /**
     * Step 3: Resets the user's password using the reset token.
     * Marks the token as used and cleans up all OTPs.
     *
     * @param request contains reset token and new password
     */
    void resetPassword(ResetPasswordWithOtpRequest request);

    /**
     * Resends a new OTP (invalidates any existing active OTP first).
     *
     * @param request contains the user's email
     * @return response with masked email and expiry info
     */
    OtpResponse resendOtp(ForgotPasswordRequest request);
}
