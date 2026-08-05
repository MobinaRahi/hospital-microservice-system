package hospital.authservice.model.token;

import hospital.authservice.model.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Stores one-time-password (OTP) tokens used for password recovery.
 * Each OTP is a 6-digit numeric code linked to a user, valid for a short period.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each OTP is valid for 5 minutes</li>
 *   <li>Each OTP can only be used once</li>
 *   <li>A user can have at most one active OTP at any time</li>
 *   <li>After 3 failed verification attempts, the OTP is locked</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity(name = "otpTokenEntity")
@Table(name = "otp_tokens",
        indexes = {
                @Index(name = "idx_otp_token", columnList = "token"),
                @Index(name = "idx_otp_user", columnList = "user_id"),
                @Index(name = "idx_otp_expiry", columnList = "expiry_date")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OtpToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The 6-digit OTP code (e.g. "482916").
     * Stored as String to preserve leading zeros.
     */
    @Column(name = "token", nullable = false, length = 6)
    private String token;

    /**
     * The user this OTP belongs to.
     * Links to the User entity via ManyToOne relationship.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * When this OTP expires.
     * Set to 5 minutes after creation.
     */
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    /**
     * Whether this OTP has been successfully used.
     * Once used, it cannot be reused.
     */
    @Column(name = "used", nullable = false)
    @Builder.Default
    private boolean used = false;

    /**
     * Number of failed verification attempts.
     * After reaching MAX_FAILED_ATTEMPTS (3), the OTP is locked.
     */
    @Column(name = "failed_attempts", nullable = false)
    @Builder.Default
    private int failedAttempts = 0;

    /**
     * Maximum allowed failed verification attempts before locking.
     */
    private static final int MAX_FAILED_ATTEMPTS = 3;

    /**
     * Checks whether this OTP is still valid.
     * An OTP is valid if it:
     * - Has not been used
     * - Has not been locked (too many failed attempts)
     * - Has not expired
     *
     * @return true if the OTP can still be used for verification
     */
    public boolean isValid() {
        return !used
                && !isLocked()
                && expiryDate.isAfter(LocalDateTime.now());
    }

    /**
     * Checks whether the OTP has been locked due to too many failed attempts.
     *
     * @return true if the OTP is locked and cannot be verified
     */
    public boolean isLocked() {
        return failedAttempts >= MAX_FAILED_ATTEMPTS;
    }

    /**
     * Records a failed verification attempt.
     * Called when the user enters an incorrect OTP code.
     */
    public void recordFailedAttempt() {
        this.failedAttempts++;
    }

    /**
     * Marks this OTP as successfully used.
     * Called after successful OTP verification.
     */
    public void markUsed() {
        this.used = true;
    }
}
