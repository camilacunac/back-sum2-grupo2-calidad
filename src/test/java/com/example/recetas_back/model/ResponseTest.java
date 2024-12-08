package com.example.recetas_back.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseTest {

    @Test
    void testResponseSettersAndGetters() {
        // Crear objeto Response con valores iniciales
        Response response = new Response("success", "Initial Response", "No error");

        // Validar valores iniciales con getters
        assertEquals("success", response.getState());
        assertEquals("Initial Response", response.getRes());
        assertEquals("No error", response.getError());

        // Modificar valores con setters
        response.setState("error");
        response.setRes("Updated Response");
        response.setError("An error occurred");

        // Validar valores actualizados con getters
        assertEquals("error", response.getState());
        assertEquals("Updated Response", response.getRes());
        assertEquals("An error occurred", response.getError());
    }
}
