package ru.klokov.onlineexam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditingConfig {
    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

    private static class AuditorAwareImpl implements AuditorAware<String> {
        @Override
        public Optional<String> getCurrentAuditor() {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
//                return Optional.empty();
//            }
//
//            UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
//
//            return Optional.ofNullable(userPrincipal.getUsername());
            return Optional.of("USERNAME");
        }
    }
}
