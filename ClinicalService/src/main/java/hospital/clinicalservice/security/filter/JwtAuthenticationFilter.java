package hospital.clinicalservice.security.filter;

import hospital.clinicalservice.security.model.SecurityUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * JWT authentication filter for ClinicalService.
 * Validates tokens issued by AuthService and sets security context.
 *
 * <p><strong>How it works:</strong></p>
 * <ol>
 *   <li>Extracts JWT from Authorization header (Bearer token)</li>
 *   <li>Validates token signature using shared secret</li>
 *   <li>Extracts user info (id, username, roles) from token claims</li>
 *   <li>Creates SecurityUser and sets authentication in context</li>
 * </ol>
 *
 * <p><strong>Token format (issued by AuthService):</strong></p>
 * <pre>
 * {
 *   "sub": "username",
 *   "auth": "ROLE_DOCTOR,ROLE_USER",
 *   "uid": 123,
 *   "iat": 1689000000,
 *   "exp": 1689003600
 * }
 * </pre>
 *
 * @author Mobina
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${app.jwt.secret:defaultSecretKeyForJWTTokenGenerationMustBeLongEnough1234567890}")
    private String jwtSecret;

    /**
     * Creates HMAC signing key from secret string.
     * Must match the key used by AuthService to sign tokens.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (jwt != null && validateToken(jwt)) {
                Claims claims = getClaims(jwt);
                String username = claims.getSubject();
                String authorities = claims.get("auth", String.class);
                Long userId = extractUserId(claims);

                // Create SecurityUser from token claims (no DB call needed)
                SecurityUser principal = new SecurityUser(userId, username, authorities);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Validates JWT token signature and expiration.
     * Returns false if token is invalid or expired.
     */
    private boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Parses and returns JWT claims.
     * Throws exception if token is invalid or expired.
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extracts user ID from JWT claims.
     * Handles both Number and String formats for compatibility.
     */
    private Long extractUserId(Claims claims) {
        Object value = claims.get("uid");
        if (value instanceof Number number) return number.longValue();
        if (value instanceof String text && !text.isBlank()) return Long.parseLong(text);
        return null;
    }

    /**
     * Extracts JWT from Authorization header.
     * Expected format: "Bearer eyJhbGciOiJIUzI1NiIs..."
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
