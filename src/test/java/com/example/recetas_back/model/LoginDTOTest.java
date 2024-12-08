package com.example.recetas_back.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginDTOTest {

    @Test
    void testDefaultConstructorAndSetters() {
        // Crear una instancia de LoginDTO con el constructor por defecto
        LoginDTO loginDTO = new LoginDTO();

        // Configurar valores de prueba
        String correo = "test@example.com";
        String contrasena = "Password123!";

        // Usar los setters para asignar valores
        loginDTO.setCorreo(correo);
        loginDTO.setContrasena(contrasena);

        // Verificar los valores con los getters
        assertEquals(correo, loginDTO.getCorreo());
        assertEquals(contrasena, loginDTO.getContrasena());
    }
}
