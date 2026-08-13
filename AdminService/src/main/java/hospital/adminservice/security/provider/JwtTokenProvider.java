package hospital.adminservice.security.provider;

import hospital.adminservice.security.model.SecurityUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret:dev-only-insecure-secret-change-me-in-production-0123456789abcdef}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:3600000}")
    private long jwtExpiration;

    @Value("${app.jwt.refresh-expiration:86400000}")
    private long refreshExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateToken(userDetails, jwtExpiration);
    }

    public String generateRefreshToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateToken(userDetails, refreshExpiration);
    }

    private String generateToken(UserDetails userDetails, long expiration) {
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        JwtBuilder builder = Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("auth", authorities);

        if (userDetails instanceof SecurityUser securityUser) {
            builder.claim("uid", securityUser.getId());
            if (securityUser.getTenantId() != null) {
                builder.claim("tenantId", securityUser.getTenantId());
            }
        }

        return builder.issuedAt(now).expiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS256).compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    public Long getTenantIdFromToken(String token) {
        try {
            Object tenantId = Jwts.parser().verifyWith(getSigningKey()).build()
                    .parseSignedClaims(token).getPayload().get("tenantId");
            if (tenantId instanceof Integer) return ((Integer) tenantId).longValue();
            if (tenantId instanceof Long) return (Long) tenantId;
            return null;
        } catch (Exception e) { return null; }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (ExpiredJwtException e) { log.error("JWT token expired: {}", e.getMessage()); }
        catch (UnsupportedJwtException e) { log.error("JWT unsupported: {}", e.getMessage()); }
        catch (IllegalArgumentException e) { log.error("JWT claims empty: {}", e.getMessage()); }
        return false;
    }
}
