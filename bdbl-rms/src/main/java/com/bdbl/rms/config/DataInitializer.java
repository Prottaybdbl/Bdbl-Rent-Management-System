package com.bdbl.rms.config;

import com.bdbl.rms.entity.User;
import com.bdbl.rms.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .passwordHash(passwordEncoder.encode("admin123"))
                        .fullName("System Administrator")
                        .email("admin@bdbl.com.bd")
                        .role("SUPER_ADMIN")
                        .status("ACTIVE")
                        .build();
                userRepository.save(admin);
                System.out.println("Default admin user created: admin / admin123");
            }

            if (userRepository.findByUsername("2518").isEmpty()) {
                User user = User.builder()
                        .username("2518")
                        .passwordHash(passwordEncoder.encode("password"))
                        .fullName("Prottay Paul")
                        .email("prottay@bdbl.com.bd")
                        .role("OFFICER")
                        .status("ACTIVE")
                        .build();
                userRepository.save(user);
                System.out.println("Test user created: 2518 / password");
            }
        };
    }
}
