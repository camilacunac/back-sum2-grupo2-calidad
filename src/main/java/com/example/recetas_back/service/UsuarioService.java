package com.example.recetas_back.service;

import com.example.recetas_back.model.Response;
import com.example.recetas_back.model.Usuario;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface UsuarioService {
    ResponseEntity<Response> crearUsuario(Usuario usuario) throws Exception;

    ResponseEntity<Response> login(String correo, String contrasena) throws Exception;

    ResponseEntity<Response> actualizarRol(Long idUsuario, String nuevoRol) throws Exception;

    ResponseEntity<Response> eliminarUsuario(Long idUsuario) throws Exception;

    List<Usuario> getAllUsuarios();

    String obtenerRolDesdeToken(String token) throws Exception;
}
