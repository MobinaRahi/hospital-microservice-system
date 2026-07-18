package hospital.clinicalservice.security.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * Lightweight security user extracted from JWT token claims.
 * Does NOT connect to database — only reads token claims.
 *
 * <p><strong>How it's created:</strong></p>
 * <pre>
 * JWT Claims:
 *   "sub": "dr.ali"
 *   "auth": "ROLE_DOCTOR,ROLE_USER"
 *   "uid": 123
 *     ↓
 * SecurityUser(123, "dr.ali", "ROLE_DOCTOR,ROLE_USER")
 * </pre>
 *
 * <p><strong>Why not use User entity?</strong></p>
 * <ul>
 *   <li>CoreService doesn't own User entity (AuthService does)</li>
 *   <li>Avoids database call on every request</li>
 *   <li>User info is already in the JWT token</li>
 * </ul>
 *
 * @author Mobina
 */
@Getter
public class SecurityUser implements UserDetails {

    /** User ID from JWT "uid" claim */
    private final Long id;

    /** Username from JWT "sub" claim */
    private final String username;

    /** Authorities parsed from JWT "auth" claim (comma-separated) */
    private final Collection<GrantedAuthority> authorities;

    /**
     * Creates SecurityUser from JWT token claims.
     *
     * @param id User ID from "uid" claim
     * @param username Username from "sub" claim
     * @param authoritiesCsv Comma-separated roles from "auth" claim
     *                       Example: "ROLE_DOCTOR,ROLE_USER"
     */
    public SecurityUser(Long id, String username, String authoritiesCsv) {
        this.id = id;
        this.username = username;
        this.authorities = authoritiesCsv == null || authoritiesCsv.isBlank()
                ? List.of()
                : Arrays.stream(authoritiesCsv.split(","))
                .filter(s -> !s.isBlank())
                .map(String::trim)
                .map(SimpleGrantedAuthority::new)
                .map(GrantedAuthority.class::cast)
                .toList();
    }

    // ==================== UserDetails Interface ====================

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return ""; }  // Not used - auth is via JWT
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
