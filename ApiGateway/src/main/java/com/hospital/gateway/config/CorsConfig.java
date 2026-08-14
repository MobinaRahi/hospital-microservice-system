package com.hospital.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * CORS Configuration for API Gateway
 * 
 * Allows cross-origin requests from frontend applications.
 * 
 * @author MobinaRahi
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        
        // Allowed origins (in production, specify exact domains)
        corsConfig.setAllowedOrigins(List.of(
            "http://localhost:3000",  // React dev
            "http://localhost:5173",  // Vite dev
            "http://localhost:5373",  // Production
            "*"  // For development - remove in production
        ));
        
        // Allowed methods
        corsConfig.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        
        // Allowed headers
        corsConfig.setAllowedHeaders(List.of(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin",
            "X-Requested-With",
            "X-User-Id",
            "X-Username",
            "X-User-Role"
        ));
        
        // Expose headers to client
        corsConfig.setExposedHeaders(List.of(
            "Authorization",
            "X-User-Id",
            "X-Username",
            "X-User-Role"
        ));
        
        // Allow credentials
        corsConfig.setAllowCredentials(true);
        
        // Max age (seconds)
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }

}
