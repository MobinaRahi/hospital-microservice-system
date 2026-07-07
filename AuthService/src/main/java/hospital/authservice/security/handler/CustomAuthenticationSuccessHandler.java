package hospital.authservice.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String redirectUrl = "/";

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        label:
        for (GrantedAuthority grantedAuthority : authorities) {
            String role = grantedAuthority.getAuthority();

            switch (role) {
                case "ROLE_ADMIN":
                case "ADMIN":
                    redirectUrl = "/dashboard";
                    break label;
                case "ROLE_DOCTOR":
                case "DOCTOR":
                    redirectUrl = "/doctor/dashboard";
                    break label;
                case "ROLE_NURSE":
                case "NURSE":
                    redirectUrl = "/nurse/dashboard";
                    break label;
                case "ROLE_PATIENT":
                case "PATIENT":
                    redirectUrl = "/patient_dashboard";
                    break label;
            }
        }

        response.sendRedirect(redirectUrl);
    }
}
