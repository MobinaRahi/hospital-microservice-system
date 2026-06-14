package hospital.coreservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * JPA Auditing configuration.
 * <p>
 * Enables automatic population of @CreatedDate, @LastModifiedDate,
 * and @CreatedBy fields on entities.
 * </p>
 *
 * @author Mobina
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

    /**
     * Provides the current auditor (user ID).
     * Returns 1L as a system user until real authentication is integrated.
     */
    @Bean
    public AuditorAware<Long> auditorProvider() {
        return () -> Optional.of(1L);
    }
}
