package com.cicconesoftware.tripsentinel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/** Temporary open security configuration for frontend prototyping. */
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Authentication is deferred; allow prototype API writes without CSRF tokens.
            .csrf(csrf -> csrf.disable())
            // Use the centralized Spring MVC rules defined in CorsConfig.
            .cors(Customizer.withDefaults())
            // Allow every HTTP method while authentication and authorization are deferred.
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
