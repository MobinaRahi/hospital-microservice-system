package hospital.authservice.service;

import hospital.authservice.dto.password.*;
import hospital.authservice.exception.otp.*;
import hospital.authservice.exception.user.UserNotFoundException;
import hospital.authservice.model.User;
import hospital.authservice.model.token.OtpToken;
import hospital.authservice.model.token.PasswordToken;
import hospital.authservice.repository.OtpTokenRepository;
import hospital.authservice.repository.PasswordTokenRepository;
import hospital.authservice.repository.UserRepository;
import hospital.authservice.service.impl.PasswordRecoveryServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PasswordRecoveryServiceImpl}.
 * Tests the complete OTP password recovery flow:
 *   Step 1: requestOtp → Step 2: verifyOtp → Step 3: resetPassword
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class PasswordRecoveryServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private OtpTokenRepository otpTokenRepository;
    @Mock private PasswordTokenRepository passwordTokenRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordRecoveryServiceImpl passwordRecoveryService;

    private User testUser;

    @BeforeEach
    void setUp() throws Exception {
        // Set the @Value field
        ReflectionTestUtils.setField(passwordRecoveryService, "sendRealEmail", false);

        // Create a test user
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .passwordHash("$2a$12$encodedPassword")
                .build();
    }

    // ════════════════════════════════════════════════════════════════════
    // Step 1: requestOtp
    // ════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Step 1: Request OTP")
    class RequestOtpTests {

        @Test
        @DisplayName("should generate OTP successfully for existing user")
        void shouldGenerateOtpSuccessfully() {
            // Given
            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
            when(otpTokenRepository.invalidateAllByUserId(anyLong())).thenReturn(0);
            when(otpTokenRepository.save(any(OtpToken.class))).thenAnswer(inv -> inv.getArgument(0));

            // When
            ForgotPasswordRequest request = ForgotPasswordRequest.builder()
                    .email("test@example.com")
                    .build();
            OtpResponse response = passwordRecoveryService.requestOtp(request);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getMessage()).contains("OTP code sent");
            assertThat(response.getExpiresInSeconds()).isEqualTo(300); // 5 minutes
            assertThat(response.getMaskedEmail()).isEqualTo("tes***@example.com");

            verify(otpTokenRepository).save(any(OtpToken.class));
            verify(otpTokenRepository).invalidateAllByUserId(1L);
        }

        @Test
        @DisplayName("should throw UserNotFoundException when email doesn't exist")
        void shouldThrowWhenEmailNotFound() {
            // Given
            when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

            // When & Then
            ForgotPasswordRequest request = ForgotPasswordRequest.builder()
                    .email("unknown@example.com")
                    .build();

            assertThatThrownBy(() -> passwordRecoveryService.requestOtp(request))
                    .isInstanceOf(UserNotFoundException.class);

            verify(otpTokenRepository, never()).save(any());
        }

        @Test
        @DisplayName("should invalidate existing OTPs before generating new one")
        void shouldInvalidateExistingOtps() {
            // Given
            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
            when(otpTokenRepository.invalidateAllByUserId(1L)).thenReturn(2); // 2 existing OTPs
            when(otpTokenRepository.save(any(OtpToken.class))).thenAnswer(inv -> inv.getArgument(0));

            // When
            ForgotPasswordRequest request = ForgotPasswordRequest.builder()
                    .email("test@example.com")
                    .build();
            passwordRecoveryService.requestOtp(request);

            // Then
            verify(otpTokenRepository).invalidateAllByUserId(1L);
        }
    }

    // ════════════════════════════════════════════════════════════════════
    // Step 2: verifyOtp
    // ════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Step 2: Verify OTP")
    class VerifyOtpTests {

        @Test
        @DisplayName("should verify OTP successfully and return reset token")
        void shouldVerifyOtpSuccessfully() {
            // Given
            OtpToken validOtp = OtpToken.builder()
                    .id(1L)
                    .token("123456")
                    .user(testUser)
                    .expiryDate(LocalDateTime.now().plusMinutes(5))
                    .used(false)
                    .failedAttempts(0)
                    .build();

            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
            when(otpTokenRepository.findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(1L))
                    .thenReturn(Optional.of(validOtp));
            when(otpTokenRepository.save(any(OtpToken.class))).thenAnswer(inv -> inv.getArgument(0));
            when(passwordTokenRepository.save(any(PasswordToken.class))).thenAnswer(inv -> inv.getArgument(0));

            // When
            VerifyOtpRequest request = VerifyOtpRequest.builder()
                    .email("test@example.com")
                    .otpCode("123456")
                    .build();
            OtpVerifyResponse response = passwordRecoveryService.verifyOtp(request);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getResetToken()).isNotNull().isNotEmpty();
            assertThat(response.getResetTokenExpiresInMinutes()).isEqualTo(10);
            assertThat(response.getMessage()).contains("verified successfully");

            verify(otpTokenRepository).save(argThat(otp -> otp.isUsed()));
            verify(passwordTokenRepository).save(any(PasswordToken.class));
        }

        @Test
        @DisplayName("should throw InvalidOtpException when code is wrong")
        void shouldThrowWhenOtpCodeIsWrong() {
            // Given
            OtpToken validOtp = OtpToken.builder()
                    .id(1L)
                    .token("123456")
                    .user(testUser)
                    .expiryDate(LocalDateTime.now().plusMinutes(5))
                    .used(false)
                    .failedAttempts(0)
                    .build();

            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
            when(otpTokenRepository.findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(1L))
                    .thenReturn(Optional.of(validOtp));
            when(otpTokenRepository.save(any(OtpToken.class))).thenAnswer(inv -> inv.getArgument(0));

            // When & Then
            VerifyOtpRequest request = VerifyOtpRequest.builder()
                    .email("test@example.com")
                    .otpCode("999999") // Wrong code
                    .build();

            assertThatThrownBy(() -> passwordRecoveryService.verifyOtp(request))
                    .isInstanceOf(InvalidOtpException.class)
                    .hasMessageContaining("attempt(s) remaining");

            verify(otpTokenRepository).save(argThat(otp -> otp.getFailedAttempts() == 1));
            verify(passwordTokenRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw OtpLockedException after 3 failed attempts")
        void shouldLockAfterThreeFailedAttempts() {
            // Given
            OtpToken lockedOtp = OtpToken.builder()
                    .id(1L)
                    .token("123456")
                    .user(testUser)
                    .expiryDate(LocalDateTime.now().plusMinutes(5))
                    .used(false)
                    .failedAttempts(3) // Already 3 failed attempts
                    .build();

            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
            when(otpTokenRepository.findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(1L))
                    .thenReturn(Optional.of(lockedOtp));

            // When & Then
            VerifyOtpRequest request = VerifyOtpRequest.builder()
                    .email("test@example.com")
                    .otpCode("123456")
                    .build();

            assertThatThrownBy(() -> passwordRecoveryService.verifyOtp(request))
                    .isInstanceOf(OtpLockedException.class);
        }

        @Test
        @DisplayName("should throw OtpExpiredException when OTP is expired")
        void shouldThrowWhenOtpExpired() {
            // Given
            OtpToken expiredOtp = OtpToken.builder()
                    .id(1L)
                    .token("123456")
                    .user(testUser)
                    .expiryDate(LocalDateTime.now().minusMinutes(1)) // Expired 1 minute ago
                    .used(false)
                    .failedAttempts(0)
                    .build();

            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
            when(otpTokenRepository.findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(1L))
                    .thenReturn(Optional.of(expiredOtp));

            // When & Then
            VerifyOtpRequest request = VerifyOtpRequest.builder()
                    .email("test@example.com")
                    .otpCode("123456")
                    .build();

            assertThatThrownBy(() -> passwordRecoveryService.verifyOtp(request))
                    .isInstanceOf(OtpExpiredException.class);
        }
    }

    // ════════════════════════════════════════════════════════════════════
    // Step 3: resetPassword
    // ════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Step 3: Reset Password")
    class ResetPasswordTests {

        @Test
        @DisplayName("should reset password successfully with valid reset token")
        void shouldResetPasswordSuccessfully() {
            // Given
            String resetTokenValue = "test-reset-token-uuid";
            String newPassword = "NewPass123";

            PasswordToken resetToken = PasswordToken.builder()
                    .id(1L)
                    .token(resetTokenValue)
                    .user(testUser)
                    .expiryDate(LocalDateTime.now().plusMinutes(10))
                    .used(false)
                    .build();

            when(passwordTokenRepository.findByTokenAndUsedFalse(resetTokenValue))
                    .thenReturn(Optional.of(resetToken));
            when(passwordEncoder.encode(newPassword)).thenReturn("$2a$12$encodedNewPassword");
            when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
            when(passwordTokenRepository.save(any(PasswordToken.class))).thenAnswer(inv -> inv.getArgument(0));
            when(otpTokenRepository.invalidateAllByUserId(1L)).thenReturn(0);

            // When
            ResetPasswordWithOtpRequest request = ResetPasswordWithOtpRequest.builder()
                    .resetToken(resetTokenValue)
                    .newPassword(newPassword)
                    .build();
            passwordRecoveryService.resetPassword(request);

            // Then
            verify(userRepository).save(argThat(user ->
                    user.getPasswordHash().equals("$2a$12$encodedNewPassword")
            ));
            verify(passwordTokenRepository).save(argThat(token -> token.isUsed()));
            verify(otpTokenRepository).invalidateAllByUserId(1L);
        }

        @Test
        @DisplayName("should throw InvalidResetTokenException when token doesn't exist")
        void shouldThrowWhenTokenNotFound() {
            // Given
            when(passwordTokenRepository.findByTokenAndUsedFalse("invalid-token"))
                    .thenReturn(Optional.empty());

            // When & Then
            ResetPasswordWithOtpRequest request = ResetPasswordWithOtpRequest.builder()
                    .resetToken("invalid-token")
                    .newPassword("NewPass123")
                    .build();

            assertThatThrownBy(() -> passwordRecoveryService.resetPassword(request))
                    .isInstanceOf(InvalidResetTokenException.class);

            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw InvalidResetTokenException when token is expired")
        void shouldThrowWhenTokenExpired() {
            // Given
            PasswordToken expiredToken = PasswordToken.builder()
                    .id(1L)
                    .token("expired-token")
                    .user(testUser)
                    .expiryDate(LocalDateTime.now().minusMinutes(1)) // Expired
                    .used(false)
                    .build();

            when(passwordTokenRepository.findByTokenAndUsedFalse("expired-token"))
                    .thenReturn(Optional.of(expiredToken));

            // When & Then
            ResetPasswordWithOtpRequest request = ResetPasswordWithOtpRequest.builder()
                    .resetToken("expired-token")
                    .newPassword("NewPass123")
                    .build();

            assertThatThrownBy(() -> passwordRecoveryService.resetPassword(request))
                    .isInstanceOf(InvalidResetTokenException.class);
        }
    }

    // ════════════════════════════════════════════════════════════════════
    // resendOtp
    // ════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Resend OTP")
    class ResendOtpTests {

        @Test
        @DisplayName("should invalidate old OTP and generate new one")
        void shouldResendOtpSuccessfully() {
            // Given
            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
            when(otpTokenRepository.invalidateAllByUserId(1L)).thenReturn(1);
            when(otpTokenRepository.save(any(OtpToken.class))).thenAnswer(inv -> inv.getArgument(0));

            // When
            ForgotPasswordRequest request = ForgotPasswordRequest.builder()
                    .email("test@example.com")
                    .build();
            OtpResponse response = passwordRecoveryService.resendOtp(request);

            // Then
            assertThat(response).isNotNull();
            verify(otpTokenRepository).invalidateAllByUserId(1L);
            verify(otpTokenRepository).save(any(OtpToken.class));
        }
    }

    // ════════════════════════════════════════════════════════════════════
    // Full E2E Flow (within unit test)
    // ════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("🔗 Full flow: requestOtp → verifyOtp → resetPassword")
    void shouldCompleteFullPasswordRecoveryFlow() {
        // ── Step 1: Request OTP ──
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(otpTokenRepository.invalidateAllByUserId(1L)).thenReturn(0);
        when(otpTokenRepository.save(any(OtpToken.class))).thenAnswer(inv -> inv.getArgument(0));

        ForgotPasswordRequest forgotRequest = ForgotPasswordRequest.builder()
                .email("test@example.com")
                .build();
        OtpResponse otpResponse = passwordRecoveryService.requestOtp(forgotRequest);
        assertThat(otpResponse).isNotNull();

        // ── Step 2: Verify OTP ──
        // Capture the OTP that was saved
        ArgumentCaptor<OtpToken> otpCaptor = ArgumentCaptor.forClass(OtpToken.class);
        verify(otpTokenRepository).save(otpCaptor.capture());
        String generatedOtp = otpCaptor.getValue().getToken();

        OtpToken savedOtp = OtpToken.builder()
                .id(1L)
                .token(generatedOtp)
                .user(testUser)
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .used(false)
                .failedAttempts(0)
                .build();

        when(otpTokenRepository.findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(1L))
                .thenReturn(Optional.of(savedOtp));
        when(otpTokenRepository.save(any(OtpToken.class))).thenAnswer(inv -> inv.getArgument(0));
        when(passwordTokenRepository.save(any(PasswordToken.class))).thenAnswer(inv -> inv.getArgument(0));

        VerifyOtpRequest verifyRequest = VerifyOtpRequest.builder()
                .email("test@example.com")
                .otpCode(generatedOtp)
                .build();
        OtpVerifyResponse verifyResponse = passwordRecoveryService.verifyOtp(verifyRequest);
        assertThat(verifyResponse.getResetToken()).isNotNull();

        // ── Step 3: Reset Password ──
        ArgumentCaptor<PasswordToken> tokenCaptor = ArgumentCaptor.forClass(PasswordToken.class);
        verify(passwordTokenRepository).save(tokenCaptor.capture());
        String savedResetToken = tokenCaptor.getValue().getToken();

        PasswordToken savedToken = PasswordToken.builder()
                .id(1L)
                .token(savedResetToken)
                .user(testUser)
                .expiryDate(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build();

        when(passwordTokenRepository.findByTokenAndUsedFalse(savedResetToken))
                .thenReturn(Optional.of(savedToken));
        when(passwordEncoder.encode("MyNewPass123")).thenReturn("$2a$12$hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(otpTokenRepository.invalidateAllByUserId(1L)).thenReturn(1);

        ResetPasswordWithOtpRequest resetRequest = ResetPasswordWithOtpRequest.builder()
                .resetToken(savedResetToken)
                .newPassword("MyNewPass123")
                .build();

        // Execute — should NOT throw
        assertThatNoException().isThrownBy(() -> passwordRecoveryService.resetPassword(resetRequest));

        // Verify password was updated
        verify(userRepository).save(argThat(user ->
                user.getPasswordHash().equals("$2a$12$hashedPassword")
        ));
    }
}
