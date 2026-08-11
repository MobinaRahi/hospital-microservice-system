package hospital.billingservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA configuration for BillingService.
 *
 * @author MobinaRahi
 */
@Configuration
@EnableJpaRepositories(basePackages = "hospital.billingservice.repository")
public class JpaConfig {
}
