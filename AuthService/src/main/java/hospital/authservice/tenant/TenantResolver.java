package hospital.authservice.tenant;

import hospital.authservice.model.tenant.Tenant;
import hospital.authservice.repository.TenantRepository;
import hospital.authservice.security.provider.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Resolves the current tenant from incoming HTTP requests.
 * Supports multiple resolution strategies:
 *
 * <ol>
 *   <li><strong>JWT Claim:</strong> Extracts tenantId from the JWT token (primary method)</li>
 *   <li><strong>Subdomain:</strong> Extracts tenant from subdomain (e.g., hospital1.novacare.com)</li>
 *   <li><strong>Header:</strong> Reads X-Tenant-Id header (for internal service-to-service calls)</li>
 * </ol>
 *
 * @author MobinaRahi
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TenantResolver {

    private final TenantRepository tenantRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Resolves and sets the tenant ID in TenantContext for the given request.
     * Tries JWT claim first, then subdomain, then header.
     *
     * @param request the incoming HTTP request
     */
    public void resolveAndSetTenant(HttpServletRequest request) {
        Long tenantId = null;

        // Strategy 1: Extract from JWT token (primary)
        tenantId = resolveFromJwt(request);

        // Strategy 2: Extract from subdomain
        if (tenantId == null) {
            tenantId = resolveFromSubdomain(request);
        }

        // Strategy 3: Extract from X-Tenant-Id header (internal calls)
        if (tenantId == null) {
            tenantId = resolveFromHeader(request);
        }

        if (tenantId != null) {
            TenantContext.setCurrentTenant(tenantId);
            log.debug("Tenant resolved: {} for request: {} {}", tenantId, request.getMethod(), request.getRequestURI());
        } else {
            log.debug("No tenant found for request: {} {}", request.getMethod(), request.getRequestURI());
        }
    }

    /**
     * Extracts tenant ID from the JWT Authorization header.
     */
    private Long resolveFromJwt(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtTokenProvider.validateToken(token)) {
                    return jwtTokenProvider.getTenantIdFromToken(token);
                }
            }
        } catch (Exception e) {
            log.debug("Could not extract tenant from JWT: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Extracts tenant ID from the request subdomain.
     * Example: hospital1.novacare.com → looks up "hospital1" in tenants table.
     */
    private Long resolveFromSubdomain(HttpServletRequest request) {
        try {
            String host = request.getServerName();
            if (host != null && host.contains(".")) {
                String subdomain = host.split("\\.")[0];
                // Ignore common non-tenant subdomains
                if (!subdomain.equals("www") && !subdomain.equals("localhost") && !subdomain.equals("api")) {
                    return tenantRepository.findBySubdomainAndDeletedFalse(subdomain)
                            .map(Tenant::getId)
                            .orElse(null);
                }
            }
        } catch (Exception e) {
            log.debug("Could not extract tenant from subdomain: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Extracts tenant ID from the X-Tenant-Id header.
     * Used for internal service-to-service communication via Feign.
     */
    private Long resolveFromHeader(HttpServletRequest request) {
        try {
            String tenantHeader = request.getHeader("X-Tenant-Id");
            if (tenantHeader != null && !tenantHeader.isBlank()) {
                return Long.parseLong(tenantHeader);
            }
        } catch (NumberFormatException e) {
            log.debug("Invalid X-Tenant-Id header: {}", request.getHeader("X-Tenant-Id"));
        }
        return null;
    }
}
