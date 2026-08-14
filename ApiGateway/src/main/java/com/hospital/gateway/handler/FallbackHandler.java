package com.hospital.gateway.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Fallback Handler for Circuit Breaker
 * 
 * Returns user-friendly error responses when services are unavailable.
 * 
 * @author MobinaRahi
 */
@RestController
@Slf4j
public class FallbackHandler {

    @GetMapping("/fallback/auth")
    public Mono<Map<String, Object>> authFallback() {
        return createFallbackResponse("Auth Service is temporarily unavailable");
    }

    @GetMapping("/fallback/core")
    public Mono<Map<String, Object>> coreFallback() {
        return createFallbackResponse("Core Service is temporarily unavailable");
    }

    @GetMapping("/fallback/clinical")
    public Mono<Map<String, Object>> clinicalFallback() {
        return createFallbackResponse("Clinical Service is temporarily unavailable");
    }

    @GetMapping("/fallback/inventory")
    public Mono<Map<String, Object>> inventoryFallback() {
        return createFallbackResponse("Inventory Service is temporarily unavailable");
    }

    @GetMapping("/fallback/billing")
    public Mono<Map<String, Object>> billingFallback() {
        return createFallbackResponse("Billing Service is temporarily unavailable");
    }

    @GetMapping("/fallback/admin")
    public Mono<Map<String, Object>> adminFallback() {
        return createFallbackResponse("Admin Service is temporarily unavailable");
    }

    @GetMapping("/fallback/lab")
    public Mono<Map<String, Object>> labFallback() {
        return createFallbackResponse("Lab Service is temporarily unavailable");
    }

    @GetMapping("/fallback/notification")
    public Mono<Map<String, Object>> notificationFallback() {
        return createFallbackResponse("Notification Service is temporarily unavailable");
    }

    @GetMapping("/fallback/tenant")
    public Mono<Map<String, Object>> tenantFallback() {
        return createFallbackResponse("Tenant Service is temporarily unavailable");
    }

    private Mono<Map<String, Object>> createFallbackResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("error", "Service Unavailable");
        response.put("message", message);
        response.put("path", "/fallback");

        log.warn("Fallback triggered: {}", message);

        return Mono.just(response);
    }

}
