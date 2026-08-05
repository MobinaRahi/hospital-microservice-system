package hospital.authservice.repository;

import hospital.authservice.model.token.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository for {@link OtpToken} entities.
 * Provides methods for OTP lifecycle management:
 * creation, verification, invalidation, and cleanup.
 *
 * @author MobinaRahi
 */
@Repository
public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {

    /**
     * Finds an OTP token by its code value, only if it hasn't been used yet.
     *
     * @param token the 6-digit OTP code
     * @return the OTP token if found and unused
     */
    Optional<OtpToken> findByTokenAndUsedFalse(String token);

    /**
     * Finds the most recent active (unused) OTP for a specific user.
     * Used during verification to find which OTP to check against.
     *
     * @param userId the ID of the user
     * @return the latest unused OTP for this user
     */
    Optional<OtpToken> findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(Long userId);

    /**
     * Invalidates (marks as used) all active OTPs for a user.
     * Called when a new OTP is requested, to ensure only one active OTP exists.
     *
     * @param userId the ID of the user
     * @return the number of OTPs that were invalidated
     */
    @Modifying
    @Query("UPDATE otpTokenEntity o SET o.used = true WHERE o.user.id = :userId AND o.used = false")
    int invalidateAllByUserId(@Param("userId") Long userId);

    /**
     * Deletes all OTP tokens that expired before the given cutoff time.
     * Used for periodic cleanup to prevent database bloat.
     *
     * @param cutoff the cutoff time — tokens expired before this are deleted
     * @return the number of tokens deleted
     */
    @Modifying
    @Query("DELETE FROM otpTokenEntity o WHERE o.expiryDate < :cutoff")
    int deleteExpiredTokens(@Param("cutoff") LocalDateTime cutoff);

    /**
     * Counts how many active (unused, not expired) OTPs a user currently has.
     * Should normally be 0 or 1.
     *
     * @param userId the ID of the user
     * @param now    the current time
     * @return count of active OTPs
     */
    long countByUserIdAndUsedFalseAndExpiryDateAfter(Long userId, LocalDateTime now);
}
