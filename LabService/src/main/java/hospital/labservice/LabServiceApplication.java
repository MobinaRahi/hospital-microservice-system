package hospital.labservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main application class for LabService.
 *
 * <p><strong>Enabled Features:</strong></p>
 * <ul>
 *   <li>{@code @EnableJpaAuditing} - Automatic audit fields (createdAt, updatedAt)</li>
 *   <li>{@code @EnableFeignClients} - Feign clients for inter-service communication</li>
 *   <li>{@code @EnableCaching} - Spring Cache with Caffeine provider</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
@EnableCaching
public class LabServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabServiceApplication.class, args);
    }

}
