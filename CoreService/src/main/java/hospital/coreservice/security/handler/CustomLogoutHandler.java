package hospital.coreservice.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
/**
 * Handler for logout operations.
 *
 * @author Mobina
 */
@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    @Override
    public void logout(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            Authentication authentication
    ) {
        log.info("👤 User logged out");
        SecurityContextHolder.clearContext();
    }
}