package hospital.inventoryservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA configuration for InventoryService.
 * 
 * <p><strong>Purpose:</strong></p>
 * <ul>
 *   <li>Enables Spring Data JPA repositories</li>
 *   <li>Configures repository base package</li>
 *   <li>Activates Hibernate filters for multi-tenancy</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Configuration
@EnableJpaRepositories(basePackages = "hospital.inventoryservice.repository")
public class JpaConfig {
    // Configuration is handled by annotations and BaseEntity
}
