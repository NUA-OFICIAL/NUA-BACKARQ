package com.nua.core.security;

import com.nua.core.exceptions.exceptions.TransactionException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
@AutoConfiguration
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    private static final String BLUE = "\u001B[34m";
    private static final String RESET = "\u001B[0m";
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(@Value("${cors.allowed-origins:*}") String allowedOrigins){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization","Content-Type","X-Requested-With","Accept"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        log.info(BLUE + "CORS CONFIG" + RESET);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth").permitAll()
                        .anyRequest().authenticated()

                );
        log.info(BLUE + "FILTER CHAIN" + RESET);
        return http.build();
    }

    private void logout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String nombreMetodo = Thread.currentThread().getStackTrace()[1].getMethodName().toUpperCase();
        if (cookies == null){
            log.info("cookies inexistentes");
            throw new TransactionException(TransactionException.INVALID_TOKEN_MESSAGE, "LOGOUT");
        }

        String jwtToken = null;
        for (Cookie cookie : cookies){
            if ("jwt".equals(cookie.getName())){
                jwtToken = cookie.getValue();
                break;
            }
        }

        if(jwtToken == null){
            log.info("token invalido");
            throw new TransactionException(TransactionException.INVALID_TOKEN_MESSAGE, "LOGOUT");
        }
    }
}