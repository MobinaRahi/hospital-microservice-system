package hospital.authservice.controller;

import hospital.authservice.dto.password.*;
import hospital.authservice.dto.response.ApiResponse;
import hospital.authservice.service.PasswordRecoveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for password recovery using OTP.
 *
 * <p><strong>Flow:</strong></p>
 * <ol>
 *   <li>POST /api/v1/password/forgot — Request OTP</li>
 *   <li>POST /api/v1/password/verify-otp — Verify OTP and get reset token</li>
 *   <li>POST /api/v1/password/reset — Set new password using reset token</li>
 *   <li>POST /api/v1/password/resend-otp — Resend OTP (optional)</li>
 * </ol>
 *
 * <p><strong>Security:</strong></p>
 * <ul>
 *   <li>All endpoints are PUBLIC (no authentication required)</li>
 *   <li>OTP expires after 5 minutes</li>
 *   <li>OTP is locked after 3 failed attempts</li>
 *   <li>Reset token expires after 10 minutes</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/password")
@RequiredArgsConstructor
@Tag(name = "Password Recovery", description = "OTP-based password recovery (public endpoints)")
public class PasswordRecoveryApi {

    private final PasswordRecoveryService passwordRecoveryService;

    /**
     * Step 1: Request OTP code via email.
     * Public endpoint — no authentication required.
     */
    @PostMapping("/forgot")
    @Operation(summary = "Request OTP code for password recovery")
    public ResponseEntity<ApiResponse<OtpResponse>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        OtpResponse response = passwordRecoveryService.requestOtp(request);
        return ResponseEntity.ok(
                ApiResponse.success(response, "OTP sent successfully", HttpStatus.OK.value())
        );
    }

    /**
     * Step 2: Verify OTP and receive a reset token.
     * Public endpoint — no authentication required.
     */
    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP code and receive reset token")
    public ResponseEntity<ApiResponse<OtpVerifyResponse>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {
        OtpVerifyResponse response = passwordRecoveryService.verifyOtp(request);
        return ResponseEntity.ok(
                ApiResponse.success(response, "OTP verified successfully", HttpStatus.OK.value())
        );
    }

    /**
     * Step 3: Reset password using the reset token.
     * Public endpoint — no authentication required.
     */
    @PostMapping("/reset")
    @Operation(summary = "Reset password using reset token")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordWithOtpRequest request) {
        passwordRecoveryService.resetPassword(request);
        return ResponseEntity.ok(
                ApiResponse.success("Password reset successfully", HttpStatus.OK.value())
        );
    }

    /**
     * Resend OTP (invalidates previous OTP and sends a new one).
     * Public endpoint — no authentication required.
     */
    @PostMapping("/resend-otp")
    @Operation(summary = "Resend OTP code (invalidates previous)")
    public ResponseEntity<ApiResponse<OtpResponse>> resendOtp(
            @Valid @RequestBody ForgotPasswordRequest request) {
        OtpResponse response = passwordRecoveryService.resendOtp(request);
        return ResponseEntity.ok(
                ApiResponse.success(response, "New OTP sent successfully", HttpStatus.OK.value())
        );
    }
}
