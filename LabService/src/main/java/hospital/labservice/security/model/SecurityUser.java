package hospital.labservice.security.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * SecurityUser implements UserDetails for Spring Security.
 * Represents an authenticated user with tenant context.
 *
 * <p><strong>Multi-Tenancy:</strong></p>
 * <ul>
 *   <li>Includes tenantId for data isolation</li>
 *   <li>Each user belongs to a specific hospital tenant</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Getter
public class SecurityUser implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String username;
    private final String password;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final Long tenantId;
    private final boolean enabled;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final Collection<? extends GrantedAuthority> authorities;

    public SecurityUser(Long id, String username, String password, String email,
                        String firstName, String lastName, Long tenantId,
                        boolean enabled, boolean accountNonExpired, boolean accountNonLocked,
                        boolean credentialsNonExpired, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tenantId = tenantId;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.authorities = authorities;
    }

    /**
     * Creates a SecurityUser from basic user details.
     *
     * @param id        user ID from AuthService
     * @param username  username
     * @param password  password (not stored, used for authentication)
     * @param email     email address
     * @param firstName first name
     * @param lastName  last name
     * @param tenantId  tenant ID for multi-tenancy
     * @param enabled   whether user is enabled
     * @param roles     list of role names
     * @return SecurityUser instance
     */
    public static SecurityUser create(Long id, String username, String password, String email,
                                      String firstName, String lastName, Long tenantId,
                                      boolean enabled, List<String> roles) {
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
        return new SecurityUser(id, username, password, email, firstName, lastName, tenantId,
                enabled, true, true, true, authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Gets the full name of the user.
     *
     * @return firstName + " " + lastName, or username if names are null
     */
    public String getFullName() {
        if (firstName == null && lastName == null) return username;
        if (firstName == null) return lastName;
        if (lastName == null) return firstName;
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecurityUser that = (SecurityUser) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
