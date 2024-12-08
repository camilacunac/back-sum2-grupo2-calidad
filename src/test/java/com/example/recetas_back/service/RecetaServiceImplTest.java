package com.example.recetas_back.service;

import com.example.recetas_back.model.Comentario;
import com.example.recetas_back.model.Receta;
import com.example.recetas_back.model.Response;
import com.example.recetas_back.model.Usuario;
import com.example.recetas_back.repository.ComentarioRepository;
import com.example.recetas_back.repository.RecetaRepository;
import com.example.recetas_back.repository.UsuarioRepository;
import com.example.recetas_back.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RecetaServiceImplTest {

    private RecetaServiceImpl recetaService;
    private RecetaRepository recetaRepository;
    private ComentarioRepository comentarioRepository;
    private UsuarioRepository usuarioRepository;
    private JWTUtil jwtUtil;

    @BeforeEach
    void setUp() {
        recetaRepository = mock(RecetaRepository.class);
        comentarioRepository = mock(ComentarioRepository.class);
        jwtUtil = mock(JWTUtil.class);
        usuarioRepository = mock(UsuarioRepository.class);

        recetaService = new RecetaServiceImpl(recetaRepository, comentarioRepository, jwtUtil, usuarioRepository);
    }

    @Test
    void testPublicarRecetaUsuarioNoEncontrado() {
        when(jwtUtil.extractUsername("token")).thenReturn("correo@test.com");
        when(usuarioRepository.findByCorreo("correo@test.com")).thenReturn(null);

        ResponseEntity<Response> response = recetaService.publicarReceta(new Receta(), "token");

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Usuario no encontrado", response.getBody().getError());
    }

    @Test
    void testGetRecetaByIdRecetaNoEncontrada() {
        when(recetaRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Response> response = recetaService.getRecetaById(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Receta no encontrada", response.getBody().getError());
    }

    @Test
    void testGetAllRecetas() {
        when(recetaRepository.findAll()).thenReturn(List.of(new Receta()));

        ResponseEntity<Response> response = recetaService.getAllRecetas();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("success", response.getBody().getState());
    }

    @Test
    void testSearchRecetasEmptyResult() {
        when(recetaRepository.findAll()).thenReturn(List.of(
                new Receta("Paella", "Española", "Arroz, Mariscos", "España", "Media", "Instrucciones", 45, null, null,
                        new Date(), null)));

        ResponseEntity<Response> response = recetaService.searchRecetas("Tacos", null, null, null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, ((List<?>) response.getBody().getRes()).size());
    }

    @Test
    void testAgregarComentarioUsuarioNoEncontrado() {
        when(jwtUtil.extractUsername("token")).thenReturn("correo@test.com");
        when(usuarioRepository.findByCorreo("correo@test.com")).thenReturn(null);

        ResponseEntity<Response> response = recetaService.agregarComentario(1L, new Comentario(), "token");

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Usuario no encontrado", response.getBody().getError());
    }

    @Test
    void testPublicarRecetaException() {
        when(jwtUtil.extractUsername("token")).thenReturn("correo@test.com");
        when(usuarioRepository.findByCorreo("correo@test.com")).thenReturn(new Usuario());
        when(recetaRepository.save(any())).thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Response> response = recetaService.publicarReceta(new Receta(), "token");

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error interno", response.getBody().getError());
    }

    @Test
    void testPublicarRecetaExito() {
        when(jwtUtil.extractUsername("token")).thenReturn("correo@test.com");
        Usuario usuario = new Usuario();
        when(usuarioRepository.findByCorreo("correo@test.com")).thenReturn(usuario);

        Receta receta = new Receta();
        when(recetaRepository.save(receta)).thenReturn(receta);

        ResponseEntity<Response> response = recetaService.publicarReceta(receta, "token");

        assertEquals(201, response.getStatusCodeValue()); // CREATED
        assertEquals("success", response.getBody().getState());
        assertEquals(receta, response.getBody().getRes());
    }

    @Test
    void testGetAllRecetasException() {
        when(recetaRepository.findAll()).thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Response> response = recetaService.getAllRecetas();

        assertEquals(500, response.getStatusCodeValue()); // INTERNAL_SERVER_ERROR
        assertEquals("Error interno", response.getBody().getError());
    }

    @Test
    void testGetRecetaByIdExito() {
        Receta receta = new Receta();
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));

        ResponseEntity<Response> response = recetaService.getRecetaById(1L);

        assertEquals(200, response.getStatusCodeValue()); // OK
        assertEquals("success", response.getBody().getState());
        assertEquals(receta, response.getBody().getRes());
    }

    @Test
    void testGetRecetaByIdException() {
        when(recetaRepository.findById(1L)).thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Response> response = recetaService.getRecetaById(1L);

        assertEquals(500, response.getStatusCodeValue()); // INTERNAL_SERVER_ERROR
        assertEquals("Error interno", response.getBody().getError());
    }

    @Test
    void testSearchRecetasMatch() {
        Receta receta = new Receta("Paella", "Española", "Arroz, Mariscos", "España", "Media", "Instrucciones", 45,
                null, null, new Date(), null);
        when(recetaRepository.findAll()).thenReturn(List.of(receta));

        ResponseEntity<Response> response = recetaService.searchRecetas("Paella", null, null, null);

        assertEquals(200, response.getStatusCodeValue()); // OK
        assertEquals(1, ((List<?>) response.getBody().getRes()).size());
        assertEquals("Paella", ((Receta) ((List<?>) response.getBody().getRes()).get(0)).getNombre());
    }

    @Test
    void testSearchRecetasException() {
        when(recetaRepository.findAll()).thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Response> response = recetaService.searchRecetas(null, null, null, null);

        assertEquals(500, response.getStatusCodeValue()); // INTERNAL_SERVER_ERROR
        assertEquals("Error interno", response.getBody().getError());
    }

    @Test
    void testAgregarComentarioExito() {
        when(jwtUtil.extractUsername("token")).thenReturn("correo@test.com");
        Usuario usuario = new Usuario();
        when(usuarioRepository.findByCorreo("correo@test.com")).thenReturn(usuario);

        Receta receta = new Receta();
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));

        Comentario comentario = new Comentario();
        when(comentarioRepository.save(comentario)).thenReturn(comentario);

        ResponseEntity<Response> response = recetaService.agregarComentario(1L, comentario, "token");

        assertEquals(200, response.getStatusCodeValue()); // OK
        assertEquals("success", response.getBody().getState());
        assertEquals(comentario, response.getBody().getRes());
    }

    @Test
    void testAgregarComentarioRecetaNoEncontrada() {
        when(jwtUtil.extractUsername("token")).thenReturn("correo@test.com");
        Usuario usuario = new Usuario();
        when(usuarioRepository.findByCorreo("correo@test.com")).thenReturn(usuario);

        when(recetaRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Response> response = recetaService.agregarComentario(1L, new Comentario(), "token");

        assertEquals(404, response.getStatusCodeValue()); // NOT_FOUND
        assertEquals("Receta no encontrada", response.getBody().getError());
    }

    @Test
    void testAgregarComentarioException() {
        when(jwtUtil.extractUsername("token")).thenReturn("correo@test.com");
        Usuario usuario = new Usuario();
        when(usuarioRepository.findByCorreo("correo@test.com")).thenReturn(usuario);

        when(recetaRepository.findById(1L)).thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Response> response = recetaService.agregarComentario(1L, new Comentario(), "token");

        assertEquals(500, response.getStatusCodeValue()); // INTERNAL_SERVER_ERROR
        assertEquals("Error interno", response.getBody().getError());
    }

    @Test
    void testGetAllComentariosAdminSuccess() {
        String token = "mockToken";
        String rol = "admin";
        when(jwtUtil.extractRole(token.replace("Bearer ", ""))).thenReturn(rol);

        List<Comentario> comentarios = List.of(
                new Comentario("Contenido 1", new Date(), new Usuario(), new Receta()),
                new Comentario("Contenido 2", new Date(), new Usuario(), new Receta()));
        when(comentarioRepository.findAll()).thenReturn(comentarios);

        ResponseEntity<Response> response = recetaService.getAllComentarios(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getState());
        assertNotNull(response.getBody().getRes());
    }

    @Test
    void testGetAllComentariosForbidden() {
        String token = "mockToken";
        String rol = "cliente";
        when(jwtUtil.extractRole(token.replace("Bearer ", ""))).thenReturn(rol);

        ResponseEntity<Response> response = recetaService.getAllComentarios(token);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("error", response.getBody().getState());
        assertEquals("No tienes permisos para acceder a esta funcionalidad", response.getBody().getError());
    }

    @Test
    void testGetAllComentariosException() {
        String token = "mockToken";
        String rol = "admin";
        when(jwtUtil.extractRole(token.replace("Bearer ", ""))).thenReturn(rol);
        when(comentarioRepository.findAll()).thenThrow(new RuntimeException("DB Error"));

        ResponseEntity<Response> response = recetaService.getAllComentarios(token);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("error", response.getBody().getState());
        assertEquals("DB Error", response.getBody().getError());
    }

    @Test
    void testEliminarComentarioAdminSuccess() {
        String token = "mockToken";
        String rol = "admin";
        when(jwtUtil.extractRole(token.replace("Bearer ", ""))).thenReturn(rol);

        Comentario comentario = new Comentario("Contenido", new Date(), new Usuario(), new Receta());
        when(comentarioRepository.findById(1L)).thenReturn(Optional.of(comentario));
        doNothing().when(comentarioRepository).deleteById(1L);

        ResponseEntity<Response> response = recetaService.eliminarComentario(1L, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getState());
        assertEquals("Comentario eliminado con éxito", response.getBody().getError());
    }

    @Test
    void testEliminarComentarioForbidden() {
        String token = "mockToken";
        String rol = "cliente";
        when(jwtUtil.extractRole(token.replace("Bearer ", ""))).thenReturn(rol);

        ResponseEntity<Response> response = recetaService.eliminarComentario(1L, token);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("error", response.getBody().getState());
        assertEquals("No tienes permisos para realizar esta acción", response.getBody().getError());
    }

    @Test
    void testEliminarComentarioNotFound() {
        String token = "mockToken";
        String rol = "admin";
        when(jwtUtil.extractRole(token.replace("Bearer ", ""))).thenReturn(rol);

        when(comentarioRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Response> response = recetaService.eliminarComentario(1L, token);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("error", response.getBody().getState());
        assertEquals("Comentario no encontrado", response.getBody().getError());
    }

    @Test
    void testEliminarComentarioException() {
        String token = "mockToken";
        String rol = "admin";
        when(jwtUtil.extractRole(token.replace("Bearer ", ""))).thenReturn(rol);

        when(comentarioRepository.findById(1L)).thenThrow(new RuntimeException("DB Error"));

        ResponseEntity<Response> response = recetaService.eliminarComentario(1L, token);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("error", response.getBody().getState());
        assertEquals("DB Error", response.getBody().getError());
    }

}
