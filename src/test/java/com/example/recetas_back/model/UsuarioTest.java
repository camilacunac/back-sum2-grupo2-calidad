package com.example.recetas_back.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsuarioTest {

    @Test
    void testGettersAndSetters() {
        // Crear una instancia de Usuario
        Usuario usuario = new Usuario();

        // Configurar valores de prueba
        Long id = 1L;
        String correo = "test@example.com";
        String contrasena = "Password123!";
        String nombre = "John";
        String apellido = "Doe";
        String rol = "admin";
        String direccion = "123 Main St";
        String telefono = "1234567890";
        LocalDate fechaRegistro = LocalDate.now();

        // Probar setters
        usuario.setId(id);
        usuario.setCorreo(correo);
        usuario.setContrasena(contrasena);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setRol(rol);
        usuario.setDireccion(direccion);
        usuario.setTelefono(telefono);
        usuario.setFechaRegistro(fechaRegistro);

        // Probar getters
        assertEquals(id, usuario.getId());
        assertEquals(correo, usuario.getCorreo());
        assertEquals(contrasena, usuario.getContrasena());
        assertEquals(nombre, usuario.getNombre());
        assertEquals(apellido, usuario.getApellido());
        assertEquals(rol, usuario.getRol());
        assertEquals(direccion, usuario.getDireccion());
        assertEquals(telefono, usuario.getTelefono());
        assertEquals(fechaRegistro, usuario.getFechaRegistro());
    }
}
