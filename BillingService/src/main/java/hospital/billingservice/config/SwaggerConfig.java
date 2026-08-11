package hospital.billingservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI configuration for BillingService.
 *
 * <p><strong>Endpoints:</strong></p>
 * <ul>
 *   <li>Swagger UI: http://localhost:8085/swagger-ui.html</li>
 *   <li>OpenAPI JSON: http://localhost:8085/v3/api-docs</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BillingService API")
                        .version("1.0.0")
                        .description("Hospital Billing Management Service - " +
                                "Manages invoices, payments, insurance, payroll and employee management")
                        .contact(new Contact()
                                .name("Mobina Rahi")
                                .email("mobina.rahi@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT Authorization header using the Bearer scheme")));
    }
}
