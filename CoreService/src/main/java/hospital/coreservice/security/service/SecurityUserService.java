package hospital.coreservice.security.service;

import com.company.springbootfullsample.security.model.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("securityUserService")
public class SecurityUserService {

    public boolean isCurrentUser(Long userId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof SecurityUser securityUser) {
            return securityUser.getId().equals(userId);
        }
        return false;
    }
}