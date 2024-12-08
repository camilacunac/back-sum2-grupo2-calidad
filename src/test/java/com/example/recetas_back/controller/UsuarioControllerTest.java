package com.example.recetas_back.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.recetas_back.model.LoginDTO;
import com.example.recetas_back.model.Response;
import com.example.recetas_back.model.Usuario;
import com.example.recetas_back.service.UsuarioService;
import com.example.recetas_back.controller.UsuarioController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializar los mocks
    }

    @Test
    void testCrearUsuario() throws Exception {
        // Crear un objeto Usuario con valores válidos
        Usuario usuario = new Usuario(
                "correo@test.com", // Correo
                "Password@123", // Contraseña
                "John", // Nombre
                "Doe", // Apellido
                "cliente", // Rol
                "123 Calle Falsa", // Dirección
                "123456789", // Teléfono
                LocalDate.now() // Fecha de registro
        );

        // Crear una respuesta simulada
        Response expectedResponse = new Response("success", usuario, "");

        // Configurar el mock del servicio
        when(usuarioService.crearUsuario(any(Usuario.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        // Llamar al método del controlador
        ResponseEntity<Response> response = usuarioController.crearUsuario(usuario);

        // Verificar el resultado esperado
        assertEquals(200, response.getStatusCodeValue()); // Comprobar el código de estado
        assertEquals("success", response.getBody().getState()); // Comprobar el estado de la respuesta
        assertEquals(usuario.getCorreo(), ((Usuario) response.getBody().getRes()).getCorreo()); // Comprobar el correo
    }

    @Test
    void testActualizarRolRolInvalido() throws Exception {
        ResponseEntity<Response> mockResponse = ResponseEntity.status(400)
                .body(new Response("error", null, "Rol no valido"));
        when(usuarioService.actualizarRol(anyLong(), eq("invalid_rol"))).thenReturn(mockResponse);

        ResponseEntity<Response> response = usuarioController.actualizarRol(1L, "invalid_rol");

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Rol no valido", response.getBody().getError());
    }

    @Test
    void testGetAllUsuariosExito() {
        Usuario usuario1 = new Usuario("correo1@test.com", "Password@123", "John", "Doe", "cliente", null, null,
                LocalDate.now());
        Usuario usuario2 = new Usuario("correo2@test.com", "Password@123", "Jane", "Doe", "admin", null, null,
                LocalDate.now());

        when(usuarioService.getAllUsuarios()).thenReturn(List.of(usuario1, usuario2));

        List<Usuario> usuarios = usuarioController.getAllUsuarios();

        assertEquals(2, usuarios.size());
        assertEquals("correo1@test.com", usuarios.get(0).getCorreo());
        assertEquals("correo2@test.com", usuarios.get(1).getCorreo());
    }

    @Test
    void testCrearUsuarioException() throws Exception {
        when(usuarioService.crearUsuario(any())).thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Response> response = usuarioController.crearUsuario(new Usuario());

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error interno al crear el usuario", response.getBody().getError());
    }

    @Test
    void testLoginExito() throws Exception {
        LoginDTO loginDTO = new LoginDTO("correo@test.com", "Password@123");
        Response mockResponse = new Response("success", "mockToken", "Inicio de sesión exitoso");

        when(usuarioService.login(eq(loginDTO.getCorreo()), eq(loginDTO.getContrasena())))
                .thenReturn(ResponseEntity.ok(mockResponse));

        ResponseEntity<Response> response = usuarioController.login(loginDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().getState());
        assertEquals("mockToken", response.getBody().getRes());
    }

    @Test
    void testLoginException() throws Exception {
        LoginDTO loginDTO = new LoginDTO("correo@test.com", "Password@123");
        when(usuarioService.login(eq(loginDTO.getCorreo()), eq(loginDTO.getContrasena())))
                .thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Response> response = usuarioController.login(loginDTO);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error interno en el inicio de sesión", response.getBody().getError());
    }

    @Test
    void testActualizarRolException() throws Exception {
        when(usuarioService.actualizarRol(anyLong(), anyString())).thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Response> response = usuarioController.actualizarRol(1L, "admin");

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error interno al actualizar el rol", response.getBody().getError());
    }

    @Test
    void testEliminarUsuarioExito() throws Exception {
        Response mockResponse = new Response("success", "Usuario eliminado con éxito", "");
        when(usuarioService.eliminarUsuario(1L)).thenReturn(ResponseEntity.ok(mockResponse));

        ResponseEntity<Response> response = usuarioController.eliminarUsuario(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().getState());
        assertEquals("Usuario eliminado con éxito", response.getBody().getRes());
    }

    @Test
    void testEliminarUsuarioException() throws Exception {
        when(usuarioService.eliminarUsuario(anyLong())).thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Response> response = usuarioController.eliminarUsuario(1L);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error interno al eliminar el usuario", response.getBody().getError());
    }

    @Test
    void testValidarTokenExito() {
        ResponseEntity<Response> response = usuarioController.validarToken();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().getState());
        assertEquals("Token valido", response.getBody().getRes());
    }

}
