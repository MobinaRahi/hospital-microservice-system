package hospital.notificationservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA configuration for NotificationService.
 *
 * <p>Enables JPA repositories for the service package.</p>
 *
 * @author MobinaRahi
 */
@Configuration
@EnableJpaRepositories(basePackages = "hospital.notificationservice.repository")
public class JpaConfig {
}
