package com.example.recetas_back.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComentarioDTOTest {

    @Test
    void testComentarioDTOGettersAndSetters() {
        // Datos de prueba
        Long id = 1L;
        String contenido = "Contenido del comentario";
        Date fechaComentario = new Date();
        String nombreUsuario = "Juan";
        String apellidoUsuario = "Pérez";
        String nombreReceta = "Receta de prueba";

        // Crear objeto ComentarioDTO
        ComentarioDTO comentarioDTO = new ComentarioDTO(id, contenido, fechaComentario, nombreUsuario, apellidoUsuario,
                nombreReceta);

        // Validar getters
        assertEquals(id, comentarioDTO.getId());
        assertEquals(contenido, comentarioDTO.getContenido());
        assertEquals(fechaComentario, comentarioDTO.getFechaComentario());
        assertEquals(nombreUsuario, comentarioDTO.getNombreUsuario());
        assertEquals(apellidoUsuario, comentarioDTO.getApellidoUsuario());
        assertEquals(nombreReceta, comentarioDTO.getNombreReceta());

        // Validar setters
        Long nuevoId = 2L;
        String nuevoContenido = "Nuevo contenido";
        Date nuevaFechaComentario = new Date();
        String nuevoNombreUsuario = "Pedro";
        String nuevoApellidoUsuario = "Gómez";
        String nuevoNombreReceta = "Otra receta";

        comentarioDTO.setId(nuevoId);
        comentarioDTO.setContenido(nuevoContenido);
        comentarioDTO.setFechaComentario(nuevaFechaComentario);
        comentarioDTO.setNombreUsuario(nuevoNombreUsuario);
        comentarioDTO.setApellidoUsuario(nuevoApellidoUsuario);
        comentarioDTO.setNombreReceta(nuevoNombreReceta);

        // Verificar cambios realizados por los setters
        assertEquals(nuevoId, comentarioDTO.getId());
        assertEquals(nuevoContenido, comentarioDTO.getContenido());
        assertEquals(nuevaFechaComentario, comentarioDTO.getFechaComentario());
        assertEquals(nuevoNombreUsuario, comentarioDTO.getNombreUsuario());
        assertEquals(nuevoApellidoUsuario, comentarioDTO.getApellidoUsuario());
        assertEquals(nuevoNombreReceta, comentarioDTO.getNombreReceta());
    }
}
