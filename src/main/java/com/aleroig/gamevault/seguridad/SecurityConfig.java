package com.aleroig.gamevault.seguridad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // Catálogo público
                        .requestMatchers(HttpMethod.GET, "/api/v1/videojuegos", "/api/v1/videojuegos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/estudios", "/api/v1/estudios/**").permitAll()

                        // Actividad solo para administradores
                        .requestMatchers(HttpMethod.GET, "/api/v1/actividad").hasRole("ADMIN")

                        // Reviews: lectura pública, creación autenticada
                        .requestMatchers(HttpMethod.POST, "/api/v1/videojuegos/*/reviews").hasAnyRole("USER", "ADMIN")

                        // Gestión de videojuegos solo para administradores
                        .requestMatchers(HttpMethod.POST, "/api/v1/videojuegos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/videojuegos/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/videojuegos/*").hasRole("ADMIN")

                        // Gestión de estudios solo para administradores
                        .requestMatchers(HttpMethod.POST, "/api/v1/estudios").hasRole("ADMIN")

                        // Errores de Spring
                        .requestMatchers("/error").permitAll()

                        // Cualquier otra ruta queda cerrada
                        .anyRequest().denyAll()
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        var admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        var user = User.withUsername("user")
                .password(passwordEncoder.encode("user123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}