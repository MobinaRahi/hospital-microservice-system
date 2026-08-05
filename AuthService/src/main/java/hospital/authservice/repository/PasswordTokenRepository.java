package hospital.authservice.repository;

import hospital.authservice.model.token.PasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository for {@link PasswordToken} entities.
 * Manages reset tokens used in the password recovery flow (Step 3).
 *
 * @author MobinaRahi
 */
@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long> {

    /**
     * Finds a valid (unused) password reset token by its value.
     *
     * @param token the reset token UUID string
     * @return the token if found and not yet used
     */
    Optional<PasswordToken> findByTokenAndUsedFalse(String token);

    /**
     * Invalidates all active password tokens for a user.
     * Called after successful password reset (cleanup).
     *
     * @param userId the ID of the user
     * @return the number of tokens invalidated
     */
    @Modifying
    @Query("UPDATE passwordTokenEntity p SET p.used = true WHERE p.user.id = :userId AND p.used = false")
    int invalidateAllByUserId(@Param("userId") Long userId);

    /**
     * Deletes all expired password tokens.
     * Used for periodic cleanup.
     *
     * @param cutoff tokens expired before this time are deleted
     * @return the number of tokens deleted
     */
    @Modifying
    @Query("DELETE FROM passwordTokenEntity p WHERE p.expiryDate < :cutoff")
    int deleteExpiredTokens(@Param("cutoff") LocalDateTime cutoff);
}
