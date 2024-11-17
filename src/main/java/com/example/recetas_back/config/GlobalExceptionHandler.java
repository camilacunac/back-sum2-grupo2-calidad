package com.example.recetas_back.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.example.recetas_back.model.Response;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Manejo de errores 404 (No encontrado)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Response> handleNotFound(NoHandlerFoundException ex) {
        Response response = new Response("error", null, "Recurso no encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Manejo de cualquier otra excepción
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleException(Exception ex) {
        Response response = new Response("error", null, "Ocurrió un error interno");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
