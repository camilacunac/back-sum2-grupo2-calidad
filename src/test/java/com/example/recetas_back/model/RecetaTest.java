package com.example.recetas_back.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecetaTest {

    private Receta receta;
    private Usuario usuario;
    private Date fechaCreacion;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        fechaCreacion = new Date();
        receta = new Receta(
                "Paella", // nombre
                "Española", // tipoCocina
                "Arroz, mariscos", // ingredientes
                "España", // paisOrigen
                "Media", // dificultad
                "Cocinar los ingredientes y mezclar", // instrucciones
                45, // tiempoCoccion
                "imagen_url", // imagenUrl
                "video_url", // videoUrl
                fechaCreacion, // fechaCreacion
                usuario // usuario
        );
    }

    @Test
    void testConstructor() {
        assertEquals("Paella", receta.getNombre());
        assertEquals("Española", receta.getTipoCocina());
        assertEquals("Arroz, mariscos", receta.getIngredientes());
        assertEquals("España", receta.getPaisOrigen());
        assertEquals("Media", receta.getDificultad());
        assertEquals("Cocinar los ingredientes y mezclar", receta.getInstrucciones());
        assertEquals(45, receta.getTiempoCoccion());
        assertEquals("imagen_url", receta.getImagenUrl());
        assertEquals("video_url", receta.getVideoUrl());
        assertEquals(fechaCreacion, receta.getFechaCreacion());
        assertEquals(usuario, receta.getUsuario());
    }

    @Test
    void testSettersAndGetters() {
        receta.setId(1L);
        receta.setNombre("Tacos");
        receta.setTipoCocina("Mexicana");
        receta.setIngredientes("Tortilla, carne, salsa");
        receta.setPaisOrigen("México");
        receta.setDificultad("Fácil");
        receta.setInstrucciones("Preparar los ingredientes y ensamblar");
        receta.setTiempoCoccion(30);
        receta.setImagenUrl("new_image_url");
        receta.setVideoUrl("new_video_url");

        Date nuevaFechaCreacion = new Date();
        receta.setFechaCreacion(nuevaFechaCreacion);

        Usuario nuevoUsuario = new Usuario();
        receta.setUsuario(nuevoUsuario);

        List<Comentario> comentarios = List.of(new Comentario());
        receta.setComentarios(comentarios);

        assertEquals(1L, receta.getId());
        assertEquals("Tacos", receta.getNombre());
        assertEquals("Mexicana", receta.getTipoCocina());
        assertEquals("Tortilla, carne, salsa", receta.getIngredientes());
        assertEquals("México", receta.getPaisOrigen());
        assertEquals("Fácil", receta.getDificultad());
        assertEquals("Preparar los ingredientes y ensamblar", receta.getInstrucciones());
        assertEquals(30, receta.getTiempoCoccion());
        assertEquals("new_image_url", receta.getImagenUrl());
        assertEquals("new_video_url", receta.getVideoUrl());
        assertEquals(nuevaFechaCreacion, receta.getFechaCreacion());
        assertEquals(nuevoUsuario, receta.getUsuario());
        assertEquals(comentarios, receta.getComentarios());
    }

    @Test
    void testDefaultConstructor() {
        Receta emptyReceta = new Receta();
        assertNull(emptyReceta.getId());
        assertNull(emptyReceta.getNombre());
        assertNull(emptyReceta.getTipoCocina());
        assertNull(emptyReceta.getIngredientes());
        assertNull(emptyReceta.getPaisOrigen());
        assertNull(emptyReceta.getDificultad());
        assertNull(emptyReceta.getInstrucciones());
        assertNull(emptyReceta.getTiempoCoccion());
        assertNull(emptyReceta.getImagenUrl());
        assertNull(emptyReceta.getVideoUrl());
        assertNull(emptyReceta.getFechaCreacion());
        assertNull(emptyReceta.getUsuario());
        assertNull(emptyReceta.getComentarios());
    }
}
