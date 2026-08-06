package hospital.inventoryservice.config;

import hospital.inventoryservice.tenant.TenantContext;
import org.hibernate.Filter;
import org.hibernate.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Hibernate Filter configuration for automatic tenant-based data isolation.
 * 
 * <p><strong>How it works:</strong></p>
 * <ol>
 *   <li>For each request, extracts tenantId from TenantContext</li>
 *   <li>Enables Hibernate filter that automatically adds WHERE tenant_id = ? to all queries</li>
 *   <li>Ensures complete data isolation between tenants at the database level</li>
 * </ol>
 * 
 * <p><strong>Alternative approach:</strong> If you prefer explicit tenant filtering,
 * you can remove this filter and add tenantId parameter to repository methods manually.</p>
 *
 * @author MobinaRahi
 */
@Configuration
public class TenantFilter extends OncePerRequestFilter {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            Long tenantId = TenantContext.getCurrentTenant();
            
            if (tenantId != null) {
                Session session = entityManager.unwrap(Session.class);
                Filter filter = session.enableFilter("tenantFilter");
                filter.setParameter("tenantId", tenantId);
                filter.validate();
            }
            
            filterChain.doFilter(request, response);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                Session session = entityManager.unwrap(Session.class);
                session.disableFilter("tenantFilter");
            }
        }
    }
}
