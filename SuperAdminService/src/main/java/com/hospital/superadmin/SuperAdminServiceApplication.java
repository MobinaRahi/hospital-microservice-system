package com.hospital.superadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Super Admin Service Application
 * Manages SaaS platform: tenants, plans, users, and system monitoring.
 *
 * @author MobinaRahi
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
@EnableCaching
public class SuperAdminServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuperAdminServiceApplication.class, args);
    }

}
