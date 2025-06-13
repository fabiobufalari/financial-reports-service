package ca.buildsystem.reports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main application class for the Financial Reports Service.
 * This service is responsible for generating, storing, and distributing financial reports
 * for the Build System Financial Control System.
 */
@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class FinancialReportsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancialReportsServiceApplication.class, args);
    }
}
