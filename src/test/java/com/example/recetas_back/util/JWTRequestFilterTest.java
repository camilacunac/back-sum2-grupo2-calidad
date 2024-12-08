package com.example.recetas_back.util;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.mock.web.MockFilterChain;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class JWTRequestFilterTest {

    private JWTRequestFilter jwtRequestFilter;

    @Mock
    private JWTUtil jwtUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtRequestFilter = new JWTRequestFilter(jwtUtil);
    }

    @Test
    void testPublicPath() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        request.setServletPath("/usuarios/login"); // Ruta pública

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Verifica que la solicitud continuó sin validación
        verify(filterChain, times(1)).doFilter(request, response);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testValidTokenInHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        request.setServletPath("/recetas/privada"); // Ruta protegida
        request.addHeader("Authorization", "Bearer validToken");

        when(jwtUtil.extractUsername("validToken")).thenReturn("testUser");
        when(jwtUtil.validateToken("validToken", "testUser")).thenReturn(true);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Verifica que la solicitud continuó y se configuró la autenticación
        verify(filterChain, times(1)).doFilter(request, response);
        assertEquals("testUser", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @Test
    void testExpiredToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        request.setServletPath("/recetas/privada"); // Ruta protegida
        request.addHeader("Authorization", "Bearer expiredToken");

        when(jwtUtil.extractUsername("expiredToken")).thenThrow(new ExpiredJwtException(null, null, "Token expirado"));

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Verifica que se devolvió un error 403
        assertEquals(403, response.getStatus());
        assertEquals("{\"status\":\"error\",\"data\":null,\"message\":\"JWT expirado\"}",
                response.getContentAsString());
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void testInvalidToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        request.setServletPath("/recetas/privada"); // Ruta protegida
        request.addHeader("Authorization", "Bearer invalidToken");

        when(jwtUtil.extractUsername("invalidToken")).thenThrow(new RuntimeException("Token inválido"));

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Verifica que se devolvió un error 403
        assertEquals(403, response.getStatus());
        assertEquals("{\"status\":\"error\",\"data\":null,\"message\":\"Token inválido\"}",
                response.getContentAsString());
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void testTokenInCookie() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        request.setServletPath("/recetas/privada"); // Ruta protegida
        request.setCookies(new Cookie("token", "validToken"));

        when(jwtUtil.extractUsername("validToken")).thenReturn("testUser");
        when(jwtUtil.validateToken("validToken", "testUser")).thenReturn(true);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Verifica que la solicitud continuó y se configuró la autenticación
        verify(filterChain, times(1)).doFilter(request, response);
        assertEquals("testUser", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

}
