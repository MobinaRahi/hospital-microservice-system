package hospital.clinicalservice.security.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * Lightweight security user extracted from JWT token.
 * Does NOT connect to database - only reads token claims.
 */
@Getter
public class SecurityUser implements UserDetails {
    private final Long id;
    private final String username;
    private final Collection<GrantedAuthority> authorities;

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

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return ""; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
