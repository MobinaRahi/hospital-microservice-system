package hospital.coreservice.security.model;

import hospital.coreservice.model.Permission;
import hospital.coreservice.model.Role;
import hospital.coreservice.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Getter
public class SecurityUser implements UserDetails {
    private final Long id;
    private final String username;
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final boolean enabled;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final LocalDateTime lockedUntil;
    private final Collection<GrantedAuthority> authorities;

    /**
     * سازنده از روی User entity
     */
    public SecurityUser(User user) {
        Objects.requireNonNull(user, "User cannot be null");

        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPasswordHash();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.enabled = user.isEnabled();
        this.accountNonExpired = user.isAccountNonExpired();
        this.credentialsNonExpired = user.isCredentialsNonExpired();
        this.lockedUntil = user.getLockedUntil();
        this.accountNonLocked = calculateAccountNonLocked(user);
        this.authorities = extractAuthorities(user);
    }

    /**
     * سازنده از روی id، username و authoritiesCsv
     */
    public SecurityUser(Long id, String username, String authoritiesCsv) {
        this.id = id;
        this.username = username;
        this.email = null;
        this.password = "";
        this.firstName = null;
        this.lastName = null;
        this.enabled = true;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.lockedUntil = null;
        this.authorities = authoritiesCsv == null || authoritiesCsv.isBlank()
                ? List.of()
                : Arrays.stream(authoritiesCsv.split(","))
                .filter(s -> !s.isBlank())
                .map(String::trim)
                .map(SimpleGrantedAuthority::new)
                .map(GrantedAuthority.class::cast)
                .toList();
    }

    private boolean calculateAccountNonLocked(User user) {
        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now())) {
            return false;
        }
        return user.isAccountNonLocked();
    }

    private Collection<GrantedAuthority> extractAuthorities(User user) {
        Set<GrantedAuthority> auths = new HashSet<>();
        for (Role role : user.getRoles()) {
            auths.add(new SimpleGrantedAuthority(role.getName().getAuthority()));
            for (Permission perm : role.getPermissions()) {
                auths.add(new SimpleGrantedAuthority(perm.getName()));
            }
        }
        return Set.copyOf(auths);
    }

    public String getFullName() {
        if (firstName == null && lastName == null) return username;
        if (firstName == null) return lastName;
        if (lastName == null) return firstName;
        return firstName + " " + lastName;
    }

    public boolean hasRole(String roleName) {
        if (roleName == null) return false;
        return authorities.stream()
                .anyMatch(a -> roleName.equals(a.getAuthority()));
    }

    public boolean hasPermission(String permName) {
        if (permName == null) return false;
        return authorities.stream()
                .anyMatch(a -> permName.equals(a.getAuthority()));
    }

    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN") || hasRole("ROLE_SUPER_ADMIN");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return username; }

    @Override
    public boolean isAccountNonExpired() { return accountNonExpired; }

    @Override
    public boolean isAccountNonLocked() { return accountNonLocked; }

    @Override
    public boolean isCredentialsNonExpired() { return credentialsNonExpired; }

    @Override
    public boolean isEnabled() { return enabled; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SecurityUser that = (SecurityUser) obj;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() { return Objects.hash(id, username); }

    @Override
    public String toString() {
        return "SecurityUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", accountNonLocked=" + accountNonLocked +
                '}';
    }
}