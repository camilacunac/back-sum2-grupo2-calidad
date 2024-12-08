package com.example.recetas_back.config;

import com.example.recetas_back.util.JWTRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTRequestFilter jwtRequestFilter;

    @Autowired
    public SecurityConfig(@Lazy JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF protection is disabled because we are using JWT and stateless
                                              // sessions
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/usuarios/login", "/usuarios/registro", "/recetas", "/recetas/buscar")
                        .permitAll()
                        .requestMatchers("/recetas/{id}", "validar-token", "/recetas/publicar",
                                "/recetas/{id}/comentarios")
                        .authenticated()
                        .anyRequest().permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        http.addFilterAfter(securityHeadersFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Configuración de CORS para permitir el frontend
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:8081")); // Permite el origen del frontend
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        config.setAllowCredentials(true); // Permite cookies o autenticación con tokens
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    // Define el filtro que añade los encabezados de seguridad
    @Bean
    public Filter securityHeadersFilter() {
        return new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;

                httpServletResponse.setHeader("Content-Security-Policy",
                        "default-src 'self'; " +
                                "script-src 'self' https://code.jquery.com https://cdn.jsdelivr.net https://maxcdn.bootstrapcdn.com; "
                                + "style-src 'self' https://maxcdn.bootstrapcdn.com; " +
                                "img-src 'self' data:; " +
                                "font-src 'self' https://fonts.gstatic.com; " +
                                "connect-src 'self'; " +
                                "frame-ancestors 'none'; " +
                                "object-src 'none'; " +
                                "base-uri 'self'; " +
                                "form-action 'self';");

                httpServletResponse.setHeader("X-Frame-Options", "DENY");
                httpServletResponse.setHeader("X-XSS-Protection", "1; mode=block");
                httpServletResponse.setHeader("X-Content-Type-Options", "nosniff");
                httpServletResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

                chain.doFilter(request, response);
            }

            @Override
            public void destroy() {
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuración de CORS para las rutas
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8081")); // Permite el origen del frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
