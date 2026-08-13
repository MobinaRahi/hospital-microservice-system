package hospital.notificationservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI configuration for NotificationService.
 *
 * <p>Configures JWT Bearer authentication for API documentation.</p>
 *
 * @author MobinaRahi
 */
@Configuration
public class SwaggerConfig {

    /**
     * Creates OpenAPI configuration with JWT Bearer security scheme.
     *
     * @return configured OpenAPI instance
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NotificationService API")
                        .version("1.0")
                        .description("Microservice for managing notifications (SMS, Email, In-App)")
                        .contact(new Contact()
                                .name("MobinaRahi")
                                .email("mobina.rahi@example.com")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT Bearer token for authentication")));
    }
}
