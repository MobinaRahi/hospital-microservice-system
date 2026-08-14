package hospital.tenantservice.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * JWT Token Provider.
 *
 * <p>Handles JWT token validation and parsing.</p>
 *
 * @author MobinaRahi
 */
@Component
@Slf4j
public class JwtTokenProvider {

    private final SecretKey key;

    /**
     * Creates JwtTokenProvider with secret key from configuration.
     *
     * @param secret the JWT secret
     */
    public JwtTokenProvider(@Value("${app.jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Validates a JWT token.
     *
     * @param token the JWT token
     * @return true if valid
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
            return false;
        }
    }

    /**
     * Gets username from JWT token.
     *
     * @param token the JWT token
     * @return the username
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

    /**
     * Gets tenant ID from JWT token.
     *
     * @param token the JWT token
     * @return the tenant ID
     */
    public Long getTenantIdFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return claims.get("tenantId", Long.class);
    }
}
