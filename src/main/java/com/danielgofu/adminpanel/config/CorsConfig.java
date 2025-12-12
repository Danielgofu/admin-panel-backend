package com.danielgofu.adminpanel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CorsConfig {

    // Se inyecta la variable de entorno, si existe. Si no existe, es null.
    @Value("${FRONTEND_URL:}") 
    private String frontendUrl; 

    private static final String LOCALHOST_URL = "http://localhost:5173"; 

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        
        List<String> allowedOrigins = new ArrayList<>();
        allowedOrigins.add(LOCALHOST_URL); // Siempre permitimos el desarrollo local

        // Si la variable FRONTEND_URL está definida (es decir, estamos en Producción), la añadimos.
        if (frontendUrl != null && !frontendUrl.trim().isEmpty()) {
            allowedOrigins.add(frontendUrl.trim());
        }

        // Configuración de CORS
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}