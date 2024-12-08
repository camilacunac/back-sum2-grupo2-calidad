package com.example.recetas_back.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.example.recetas_back.model.Comentario;
import com.example.recetas_back.model.Receta;
import com.example.recetas_back.model.Response;
import com.example.recetas_back.service.RecetaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class RecetaControllerTest {

    @InjectMocks
    private RecetaController recetaController; // Inyecta el servicio simulado

    @Mock
    private RecetaService recetaService; // Mock del servicio

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializar mocks
    }

    @Test
    void testGetAllRecetas() {
        when(recetaService.getAllRecetas()).thenReturn(ResponseEntity.ok(new Response("success", null, "")));

        ResponseEntity<Response> response = recetaController.getAllRecetas();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().getState());
    }

    @Test
    void testGetRecetaByIdNotFound() {
        when(recetaService.getRecetaById(99L))
                .thenReturn(ResponseEntity.status(404).body(new Response("error", null, "Receta no encontrada")));

        ResponseEntity<Response> response = recetaController.getRecetaById(99L);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Receta no encontrada", response.getBody().getError());
    }

    @Test
    void testSearchRecetasExito() {
        Response mockResponse = new Response("success", null, "");
        when(recetaService.searchRecetas("Paella", "Media", "Española", "España"))
                .thenReturn(ResponseEntity.ok(mockResponse));

        ResponseEntity<Response> response = recetaController.searchRecetas("Paella", "Media", "Española", "España");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().getState());
    }

    @Test
    void testPublicarRecetaExito() {
        Response mockResponse = new Response("success", null, "");
        when(recetaService.publicarReceta(any(), eq("mockToken")))
                .thenReturn(ResponseEntity.status(201).body(mockResponse));

        ResponseEntity<Response> response = recetaController.publicarReceta(new Receta(), "Bearer mockToken");

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("success", response.getBody().getState());
    }

    @Test
    void testPublicarRecetaException() {
        when(recetaService.publicarReceta(any(), eq("mockToken"))).thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Response> response = recetaController.publicarReceta(new Receta(), "Bearer mockToken");

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error interno", response.getBody().getError());
    }

    @Test
    void testAgregarComentarioExito() {
        Response mockResponse = new Response("success", null, "Comentario agregado");
        when(recetaService.agregarComentario(eq(1L), any(), eq("mockToken")))
                .thenReturn(ResponseEntity.ok(mockResponse));

        ResponseEntity<Response> response = recetaController.agregarComentario(1L, new Comentario(),
                "Bearer mockToken");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().getState());
        assertEquals("Comentario agregado", response.getBody().getError());
    }

    @Test
    void testAgregarComentarioConTokenValido() {
        // Configurar el mock del servicio para una respuesta exitosa
        Response mockResponse = new Response("success", null, "Comentario agregado");
        when(recetaService.agregarComentario(eq(1L), any(), eq("mockToken")))
                .thenReturn(ResponseEntity.ok(mockResponse));

        // Llamar al método del controlador con un token válido
        ResponseEntity<Response> response = recetaController.agregarComentario(
                1L, new Comentario(), "Bearer mockToken");

        // Verificar que el servicio fue llamado con el token correcto
        verify(recetaService).agregarComentario(eq(1L), any(), eq("mockToken"));

        // Verificar la respuesta del controlador
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().getState());
        assertEquals("Comentario agregado", response.getBody().getError());
    }

}
