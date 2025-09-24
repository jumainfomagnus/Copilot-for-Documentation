package com.enterprise.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main application class for the Enterprise E-commerce Platform.
 * This is the entry point of the Spring Boot application.
 * 
 * Features enabled:
 * - Caching for performance optimization
 * - Asynchronous processing for non-blocking operations
 * - Scheduled tasks for background jobs
 * - Transaction management for data consistency
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
public class EcommercePlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommercePlatformApplication.class, args);
    }
}