package hospital.coreservice.security.filter;

import hospital.coreservice.security.provider.JwtTokenProvider;
import hospital.coreservice.security.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);

            if (jwt != null && tokenProvider.validateToken(jwt)) {
                String username = tokenProvider.getUsernameFromToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("✅ User authenticated: {}", username);
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // ========== اضافه شده ==========
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        return path.startsWith("/") && (
                path.equals("/") ||
                        path.equals("/index") ||
                        path.equals("/home") ||
                        path.equals("/login") ||
                        path.equals("/error") ||
                        path.startsWith("/css/") ||
                        path.startsWith("/js/") ||
                        path.startsWith("/images/") ||
                        path.startsWith("/static/") ||
                        path.startsWith("/webjars/") ||
                        path.startsWith("/patient/book") ||
                        path.startsWith("/doctors/") ||
                        path.startsWith("/departments/") ||
                        path.startsWith("/appointments/book") ||
                        path.startsWith("/api/v1/doctors/search") ||
                        path.startsWith("/api/v1/departments") ||
                        path.startsWith("/api/v1/doctors") ||
                        path.startsWith("/api/v1/doctor/by-department-id") ||
                        path.startsWith("/api/v1/auth/") ||
                        path.startsWith("/swagger-ui/") ||
                        path.startsWith("/v3/api-docs/") ||
                        path.startsWith("/api/v1/doctor/active/by-department-id") ||
                        path.startsWith("/api/v1/appointments/doctor/available") ||
                        path.startsWith("/api/v1/appointments/patient") ||
                        path.startsWith("/patient/dashboard") ||
                        path.startsWith("/patient_dashboard") ||
                        path.startsWith("/h2-console/")
        );
    }
}