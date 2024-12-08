package com.example.recetas_back.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ComentarioTest {

    @Test
    void testConstructorWithParameters() {
        // Datos de prueba
        String contenido = "Este es un comentario de prueba.";
        Date fechaComentario = new Date();
        Usuario usuario = new Usuario();
        Receta receta = new Receta();

        // Llamada al constructor
        Comentario comentario = new Comentario(contenido, fechaComentario, usuario, receta);

        // Validaciones
        assertEquals(contenido, comentario.getContenido());
        assertEquals(fechaComentario, comentario.getFechaComentario());
        assertEquals(usuario, comentario.getUsuario());
        assertEquals(receta, comentario.getReceta());
    }

    @Test
    void testGetIdAndSetId() {
        Comentario comentario = new Comentario();
        comentario.setId(123L);

        assertEquals(123L, comentario.getId());
    }

    @Test
    void testGetContenidoAndSetContenido() {
        Comentario comentario = new Comentario();
        comentario.setContenido("Nuevo contenido");

        assertEquals("Nuevo contenido", comentario.getContenido());
    }

    @Test
    void testGetFechaComentarioAndSetFechaComentario() {
        Comentario comentario = new Comentario();
        Date fecha = new Date();
        comentario.setFechaComentario(fecha);

        assertEquals(fecha, comentario.getFechaComentario());
    }

    @Test
    void testGetUsuarioAndSetUsuario() {
        Comentario comentario = new Comentario();
        Usuario usuario = new Usuario();
        comentario.setUsuario(usuario);

        assertEquals(usuario, comentario.getUsuario());
    }

    @Test
    void testGetRecetaAndSetReceta() {
        Comentario comentario = new Comentario();
        Receta receta = new Receta();
        comentario.setReceta(receta);

        assertEquals(receta, comentario.getReceta());
    }
}
