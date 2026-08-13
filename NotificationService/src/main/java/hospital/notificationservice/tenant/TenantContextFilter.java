package hospital.notificationservice.tenant;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filter that clears TenantContext after each request.
 *
 * <p>Ensures tenant context is cleaned up to prevent memory leaks
 * in the thread pool.</p>
 *
 * @author MobinaRahi
 */
@Component
@Order(1)
@Slf4j
public class TenantContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            if (TenantContext.hasTenant()) {
                TenantContext.clear();
                log.debug("Cleared tenant context after request");
            }
        }
    }
}
