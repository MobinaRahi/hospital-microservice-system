package hospital.authservice.service.impl;

import hospital.authservice.dto.password.*;
import hospital.authservice.exception.otp.*;
import hospital.authservice.exception.user.UserNotFoundException;
import hospital.authservice.model.User;
import hospital.authservice.model.token.OtpToken;
import hospital.authservice.model.token.PasswordToken;
import hospital.authservice.repository.OtpTokenRepository;
import hospital.authservice.repository.PasswordTokenRepository;
import hospital.authservice.repository.UserRepository;
import hospital.authservice.service.PasswordRecoveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of {@link PasswordRecoveryService}.
 *
 * <p><strong>Security Design:</strong></p>
 * <ul>
 *   <li>OTP is a cryptographically random 6-digit code (SecureRandom)</li>
 *   <li>OTP expires after 5 minutes</li>
 *   <li>OTP is locked after 3 failed attempts</li>
 *   <li>Only one active OTP per user at any time</li>
 *   <li>Reset token is a UUID stored in password_tokens table</li>
 *   <li>Reset token expires after 10 minutes</li>
 *   <li>New password is BCrypt-hashed before storage</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordRecoveryServiceImpl implements PasswordRecoveryService {

    // ── Constants ──────────────────────────────────────────────────────
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final int RESET_TOKEN_EXPIRY_MINUTES = 10;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    // ── Dependencies ───────────────────────────────────────────────────
    private final UserRepository userRepository;
    private final OtpTokenRepository otpTokenRepository;
    private final PasswordTokenRepository passwordTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.otp.send-real-email:false}")
    private boolean sendRealEmail;

    // ════════════════════════════════════════════════════════════════════
    // Step 1: Request OTP
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional
    public OtpResponse requestOtp(ForgotPasswordRequest request) {
        log.info("OTP requested for email: {}", maskEmail(request.getEmail()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> UserNotFoundException.byEmail(request.getEmail()));

        // Invalidate any existing active OTPs for this user
        otpTokenRepository.invalidateAllByUserId(user.getId());

        // Generate new OTP
        String otpCode = generateOtp();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);

        OtpToken otpToken = OtpToken.builder()
                .token(otpCode)
                .user(user)
                .expiryDate(expiry)
                .used(false)
                .failedAttempts(0)
                .build();

        otpTokenRepository.save(otpToken);

        // Send OTP (in dev mode, log it; in production, send email/SMS)
        sendOtpToUser(user, otpCode);

        log.info("OTP generated for user id: {}, expires at: {}", user.getId(), expiry);

        return OtpResponse.builder()
                .message("OTP code sent to your email. Please check your inbox.")
                .expiresInSeconds(OTP_EXPIRY_MINUTES * 60)
                .maskedEmail(maskEmail(request.getEmail()))
                .build();
    }

    // ════════════════════════════════════════════════════════════════════
    // Step 2: Verify OTP
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional
    public OtpVerifyResponse verifyOtp(VerifyOtpRequest request) {
        log.info("OTP verification attempted for email: {}", maskEmail(request.getEmail()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> UserNotFoundException.byEmail(request.getEmail()));

        // Find the most recent active OTP for this user
        OtpToken otpToken = otpTokenRepository
                .findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(user.getId())
                .orElseThrow(() -> new InvalidOtpException("No active OTP found. Please request a new one."));

        // Check if locked
        if (otpToken.isLocked()) {
            throw new OtpLockedException();
        }

        // Check if expired
        if (otpToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new OtpExpiredException();
        }

        // Check OTP code
        if (!otpToken.getToken().equals(request.getOtpCode())) {
            otpToken.recordFailedAttempt();
            otpTokenRepository.save(otpToken);

            int remaining = 3 - otpToken.getFailedAttempts();
            if (remaining <= 0) {
                throw new OtpLockedException();
            }
            throw new InvalidOtpException(
                    String.format("Invalid OTP code. %d attempt(s) remaining.", remaining)
            );
        }

        // OTP is valid — mark as used
        otpToken.markUsed();
        otpTokenRepository.save(otpToken);

        // Generate reset token (UUID stored in password_tokens table)
        String resetTokenValue = UUID.randomUUID().toString();
        LocalDateTime resetExpiry = LocalDateTime.now().plusMinutes(RESET_TOKEN_EXPIRY_MINUTES);

        PasswordToken resetToken = PasswordToken.builder()
                .token(resetTokenValue)
                .user(user)
                .expiryDate(resetExpiry)
                .used(false)
                .build();

        passwordTokenRepository.save(resetToken);

        log.info("OTP verified for user id: {}. Reset token generated.", user.getId());

        return OtpVerifyResponse.builder()
                .message("OTP verified successfully. Use the reset token to set your new password.")
                .resetToken(resetTokenValue)
                .resetTokenExpiresInMinutes(RESET_TOKEN_EXPIRY_MINUTES)
                .build();
    }

    // ════════════════════════════════════════════════════════════════════
    // Step 3: Reset Password
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional
    public void resetPassword(ResetPasswordWithOtpRequest request) {
        log.info("Password reset requested with reset token");

        // Find valid reset token
        PasswordToken resetToken = passwordTokenRepository
                .findByTokenAndUsedFalse(request.getResetToken())
                .orElseThrow(InvalidResetTokenException::new);

        // Check expiry
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidResetTokenException();
        }

        // Get user
        User user = resetToken.getUser();

        // Update password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordChangedAt(LocalDateTime.now());
        userRepository.save(user);

        // Mark reset token as used
        resetToken.setUsed(true);
        passwordTokenRepository.save(resetToken);

        // Invalidate all active OTPs for this user (cleanup)
        otpTokenRepository.invalidateAllByUserId(user.getId());

        log.info("Password successfully reset for user id: {}", user.getId());
    }

    // ════════════════════════════════════════════════════════════════════
    // Resend OTP
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional
    public OtpResponse resendOtp(ForgotPasswordRequest request) {
        log.info("OTP resend requested for email: {}", maskEmail(request.getEmail()));
        // Simply invalidate existing and generate new
        return requestOtp(request);
    }

    // ════════════════════════════════════════════════════════════════════
    // Private Helpers
    // ════════════════════════════════════════════════════════════════════

    /**
     * Generates a cryptographically random 6-digit OTP.
     * Range: 100000–999999 (always 6 digits, no leading zeros).
     */
    private String generateOtp() {
        int otp = SECURE_RANDOM.nextInt(900000) + 100000;
        return String.valueOf(otp);
    }

    /**
     * Sends the OTP to the user.
     * In dev mode: logs to console for testing.
     * In production: integrates with email/SMS gateway.
     */
    private void sendOtpToUser(User user, String otpCode) {
        if (sendRealEmail) {
            // TODO: Integrate with email service (e.g., SendGrid, JavaMail)
            log.info("[EMAIL] Sending OTP {} to {}", otpCode, user.getEmail());
        } else {
            // Dev mode: log the OTP for testing
            log.warn("═══════════════════════════════════════════════════");
            log.warn("  DEV MODE — OTP for {}: {}", user.getEmail(), otpCode);
            log.warn("  (Set app.otp.send-real-email=true for production)");
            log.warn("═══════════════════════════════════════════════════");
        }
    }

    /**
     * Masks an email for display purposes.
     * Example: "mobina@gmail.com" → "mob***@gmail.com"
     */
    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "***";
        String[] parts = email.split("@");
        String local = parts[0];
        String domain = parts[1];

        if (local.length() <= 3) {
            return local.charAt(0) + "***@" + domain;
        }
        return local.substring(0, 3) + "***@" + domain;
    }
}
