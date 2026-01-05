package com.thiordev.Gestion_des_Rendezvous.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class CorsConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http ) throws Exception {
        http
                // 1. Activer CORS et désactiver CSRF
                .cors(cors -> cors.configurationSource(corsConfigurationSource( )))
                .csrf(csrf -> csrf.disable())

                // 2. Autoriser toutes les requêtes sur l'API (ou configurer selon vos besoins)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build( );
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Autoriser votre domaine Railway et localhost pour le développement
        configuration.setAllowedOrigins(Arrays.asList(
                "https://senafiyahrdv.up.railway.app",
                "http://localhost:3000",
                "http://localhost:5173" // Si vous utilisez Vite
        ));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}