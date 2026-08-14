package hospital.tenantservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA configuration for TenantService.
 *
 * <p>Enables JPA repositories for the service package.</p>
 *
 * @author MobinaRahi
 */
@Configuration
@EnableJpaRepositories(basePackages = "hospital.tenantservice.repository")
public class JpaConfig {
}
