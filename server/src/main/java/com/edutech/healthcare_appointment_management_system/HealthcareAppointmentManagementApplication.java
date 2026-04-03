package com.edutech.healthcare_appointment_management_system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HealthcareAppointmentManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthcareAppointmentManagementApplication.class, args);
    }

    @Bean
    CommandLineRunner fixDb(JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                System.out.println("========== CHECKING AND FIXING DATABASE SCHEMA ==========");
                jdbcTemplate.execute("ALTER TABLE users MODIFY id BIGINT AUTO_INCREMENT;");
                jdbcTemplate.execute("ALTER TABLE patients MODIFY id BIGINT AUTO_INCREMENT;");
                jdbcTemplate.execute("ALTER TABLE doctors MODIFY id BIGINT AUTO_INCREMENT;");
                jdbcTemplate.execute("ALTER TABLE receptionists MODIFY id BIGINT AUTO_INCREMENT;");
                System.out.println("✅ DATABASE AUTO_INCREMENT CONSTRAINTS FIXED!");
            } catch (Exception e) {
                System.out.println("⚠️ DB Auto-Increment Fix Skipped (Tables may not exist yet or are already correct).");
            }
        };
    }
}
