package com.company.fileSharingManagement.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            // Permit access to these endpoints without authentication
            auth.requestMatchers("/files", "/files/download/**", "/files/share/**", "/styles/**").permitAll();
            auth.anyRequest().authenticated(); // All other requests require authentication
        })
        .oauth2Login(oauth2Login -> oauth2Login
                .loginPage("/files") // Custom login page
                .successHandler(customSuccessHandler()) // Handle successful login
                .failureUrl("/files?error=true") // Optional: Redirect on failure
        )
        .csrf(csrf -> csrf.disable()); // Consider enabling this in production

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setDefaultTargetUrl("/files/home"); // Redirect after successful login
        return successHandler;
    }
}
