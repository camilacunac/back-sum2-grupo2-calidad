package com.example.recetas_back.service;

import com.example.recetas_back.model.Response;
import com.example.recetas_back.model.Usuario;
import com.example.recetas_back.repository.ComentarioRepository;
import com.example.recetas_back.repository.RecetaRepository;
import com.example.recetas_back.repository.UsuarioRepository;
import com.example.recetas_back.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioServiceImplTest {

    private UsuarioServiceImpl usuarioService;
    private UsuarioRepository usuarioRepository;
    private RecetaRepository recetaRepository;
    private ComentarioRepository comentarioRepository;
    private JWTUtil jwtTokenUtil;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        recetaRepository = mock(RecetaRepository.class);
        comentarioRepository = mock(ComentarioRepository.class);
        jwtTokenUtil = mock(JWTUtil.class);

        usuarioService = new UsuarioServiceImpl(usuarioRepository, recetaRepository, comentarioRepository,
                jwtTokenUtil);
    }

    @Test
    void testLoginUsuarioNoEncontrado() throws Exception {
        when(usuarioRepository.findByCorreo("correo@test.com")).thenReturn(null);

        ResponseEntity<Response> response = usuarioService.login("correo@test.com", "password");

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Usuario no encontrado", response.getBody().getError());
    }

    @Test
    void testActualizarRolUsuarioNoEncontrado() throws Exception {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Response> response = usuarioService.actualizarRol(1L, "admin");

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Usuario no encontrado", response.getBody().getError());
    }

    @Test
    void testLoginPasswordIncorrect() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setContrasena(new BCryptPasswordEncoder().encode("Password@123"));
        when(usuarioRepository.findByCorreo("correo@test.com")).thenReturn(usuario);

        ResponseEntity<Response> response = usuarioService.login("correo@test.com", "WrongPassword");

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Contraseña incorrecta", response.getBody().getError());
    }

    @Test
    void testCrearUsuarioCorreoInvalido() throws Exception {
        Usuario usuario = new Usuario("correo_invalido", "Password@123", "John", "Doe", "cliente", null, null,
                LocalDate.now());

        ResponseEntity<Response> response = usuarioService.crearUsuario(usuario);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Correo no válido", response.getBody().getError());
    }

    @Test
    void testEliminarUsuarioNoEncontrado() throws Exception {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Response> response = usuarioService.eliminarUsuario(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Usuario no encontrado", response.getBody().getError());
    }

    @Test
    void testCrearUsuarioPasswordInvalida() throws Exception {
        Usuario usuario = new Usuario("correo@test.com", "invalid", "John", "Doe", "cliente", null, null,
                LocalDate.now());

        ResponseEntity<Response> response = usuarioService.crearUsuario(usuario);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(
                "Contraseña no válida. Debe tener al menos 8 caracteres, incluyendo una letra mayúscula, una minúscula, un número y un carácter especial.",
                response.getBody().getError());
    }

    @Test
    void testCrearUsuarioRolInvalido() throws Exception {
        Usuario usuario = new Usuario("correo@test.com", "Password@123", "John", "Doe", "invalid_role", null, null,
                LocalDate.now());

        ResponseEntity<Response> response = usuarioService.crearUsuario(usuario);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Rol no válido. Solo se permite 'admin' o 'cliente'.", response.getBody().getError());
    }

    @Test
    void testCrearUsuarioExito() throws Exception {
        Usuario usuario = new Usuario("correo@test.com", "Password@123", "John", "Doe", "cliente", null, null,
                LocalDate.now());

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        ResponseEntity<Response> response = usuarioService.crearUsuario(usuario);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().getState());
        assertEquals(usuario, response.getBody().getRes());
    }

    @Test
    void testLoginExito() throws Exception {
        Usuario usuario = new Usuario("correo@test.com", "Password@123", "John", "Doe", "cliente", null, null,
                LocalDate.now());
        when(usuarioRepository.findByCorreo("correo@test.com")).thenReturn(usuario);
        when(jwtTokenUtil.generateToken("correo@test.com", "cliente")).thenReturn("mockToken"); // Ajusta el rol
        when(usuarioRepository.save(any())).thenReturn(usuario);

        usuario.setContrasena(new BCryptPasswordEncoder().encode("Password@123"));

        ResponseEntity<Response> response = usuarioService.login("correo@test.com", "Password@123");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().getState());
        assertEquals("mockToken", response.getBody().getRes());
    }

    @Test
    void testActualizarRolExito() throws Exception {
        Usuario usuario = new Usuario("correo@test.com", "Password@123", "John", "Doe", "cliente", null, null,
                LocalDate.now());
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any())).thenReturn(usuario);

        ResponseEntity<Response> response = usuarioService.actualizarRol(1L, "admin");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().getState());
        assertEquals("admin", ((Usuario) response.getBody().getRes()).getRol());
    }

    @Test
    void testEliminarUsuarioExito() throws Exception {
        Usuario usuario = new Usuario();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(comentarioRepository).deleteAllByUsuarioId(1L); // Mock para comentarios
        doNothing().when(recetaRepository).deleteAllByUsuarioId(1L); // Mock para recetas
        doNothing().when(usuarioRepository).deleteById(1L);

        ResponseEntity<Response> response = usuarioService.eliminarUsuario(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Usuario eliminado con éxito", response.getBody().getRes());
    }

    @Test
    void testGetAllUsuarios() {
        Usuario usuario1 = new Usuario("correo1@test.com", "Password@123", "John", "Doe", "cliente", null, null,
                LocalDate.now());
        Usuario usuario2 = new Usuario("correo2@test.com", "Password@123", "Jane", "Doe", "admin", null, null,
                LocalDate.now());
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario1, usuario2));

        List<Usuario> usuarios = usuarioService.getAllUsuarios();

        assertEquals(2, usuarios.size());
        assertEquals("correo1@test.com", usuarios.get(0).getCorreo());
        assertEquals("correo2@test.com", usuarios.get(1).getCorreo());
    }

    @Test
    void testIsValidRole() {
        assertTrue(usuarioService.isValidRole("admin"));
        assertTrue(usuarioService.isValidRole("cliente"));
        assertFalse(usuarioService.isValidRole("invalid_role"));
    }

    @Test
    void testIsValidPassword() {
        assertTrue(usuarioService.isValidPassword("Password@123"));
        assertFalse(usuarioService.isValidPassword("password"));
        assertFalse(usuarioService.isValidPassword("PASSWORD"));
        assertFalse(usuarioService.isValidPassword("12345678"));
    }

}
