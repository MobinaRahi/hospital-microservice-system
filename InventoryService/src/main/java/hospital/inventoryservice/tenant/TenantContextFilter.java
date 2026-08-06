package hospital.inventoryservice.tenant;

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
 * Servlet filter that clears the TenantContext after each request.
 * Prevents tenant data leakage between requests in thread pools.
 *
 * <p><strong>Order:</strong> Set to HIGHEST_PRECEDENCE so it wraps all other filters.
 * The clear() happens in a finally block, ensuring it runs even on exceptions.</p>
 *
 * @author MobinaRahi
 */
@Component
@Order(Integer.MIN_VALUE)
@Slf4j
public class TenantContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            // Always clear tenant context after request completes
            if (TenantContext.hasTenant()) {
                log.debug("Clearing TenantContext for thread: {}", Thread.currentThread().getName());
                TenantContext.clear();
            }
        }
    }
}
