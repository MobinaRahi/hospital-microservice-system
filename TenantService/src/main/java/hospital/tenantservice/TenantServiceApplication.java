package hospital.tenantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main entry point for TenantService.
 *
 * <p><strong>Features:</strong></p>
 * <ul>
 *   <li>JPA Auditing for automatic audit fields</li>
 *   <li>OpenFeign for inter-service communication</li>
 *   <li>Caching with Caffeine</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
@EnableCaching
public class TenantServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenantServiceApplication.class, args);
    }

}
