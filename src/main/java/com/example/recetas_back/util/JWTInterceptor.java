package com.example.recetas_back.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Agrega lógica de verificación, pero evita modificaciones de cookies
        // directamente aquí
        // String token = request.getHeader("Authorization");

        // if (token == null || !token.startsWith("Bearer ")) {
        // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // return false;
        // }

        // Continuar con la solicitud
        return true;
    }
}
