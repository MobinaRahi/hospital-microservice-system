package com.hospital.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway Application
 * 
 * Central entry point for all client requests.
 * Routes requests to appropriate microservices.
 * 
 * Features:
 * - Request Routing
 * - Load Balancing
 * - Circuit Breaker
 * - Rate Limiting
 * - CORS Configuration
 * 
 * @author MobinaRahi
 */
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

}
