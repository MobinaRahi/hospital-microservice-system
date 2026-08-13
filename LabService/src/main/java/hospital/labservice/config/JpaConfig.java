package hospital.labservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA configuration for LabService.
 * Enables JPA repositories in the hospital.labservice.repository package.
 *
 * @author MobinaRahi
 */
@Configuration
@EnableJpaRepositories(basePackages = "hospital.labservice.repository")
public class JpaConfig {
}
