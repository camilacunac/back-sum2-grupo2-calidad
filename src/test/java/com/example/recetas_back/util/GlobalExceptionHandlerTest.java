package com.example.recetas_back.util;

import com.example.recetas_back.config.GlobalExceptionHandler;
import com.example.recetas_back.model.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleNotFound() {
        // Mock de una excepción NoHandlerFoundException
        NoHandlerFoundException exception = new NoHandlerFoundException("GET", "/invalid-path", null);

        // Llama al método del GlobalExceptionHandler
        ResponseEntity<Response> response = globalExceptionHandler.handleNotFound(exception);

        // Valida los resultados
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("error", response.getBody().getState());
        assertNull(response.getBody().getRes());
        assertEquals("Recurso no encontrado", response.getBody().getError());
    }

    @Test
    void testHandleException() {
        // Mock de una excepción genérica
        Exception exception = new Exception("Generic exception");

        // Llama al método del GlobalExceptionHandler
        ResponseEntity<Response> response = globalExceptionHandler.handleException(exception);

        // Valida los resultados
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("error", response.getBody().getState());
        assertNull(response.getBody().getRes());
        assertEquals("Ocurrió un error interno", response.getBody().getError());
    }
}
