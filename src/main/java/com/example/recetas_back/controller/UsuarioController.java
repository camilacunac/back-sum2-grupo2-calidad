package com.example.recetas_back.controller;

import com.example.recetas_back.model.LoginDTO;
import com.example.recetas_back.model.Response;
import com.example.recetas_back.model.Usuario;
import com.example.recetas_back.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @PostMapping("/registro")
    public ResponseEntity<Response> crearUsuario(@RequestBody Usuario usuario) {
        try {
            return usuarioService.crearUsuario(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response("error", null, "Error interno al crear el usuario"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginDTO loginRequest) {
        try {
            return usuarioService.login(loginRequest.getCorreo(), loginRequest.getContrasena());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response("error", null, "Error interno en el inicio de sesión"));
        }
    }

    @PutMapping("/{id}/rol")
    public ResponseEntity<Response> actualizarRol(@PathVariable Long id, @RequestParam String nuevoRol) {
        try {
            return usuarioService.actualizarRol(id, nuevoRol);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response("error", null, "Error interno al actualizar el rol"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> eliminarUsuario(@PathVariable Long id) {
        try {
            return usuarioService.eliminarUsuario(id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response("error", null, "Error interno al eliminar el usuario"));
        }
    }

    @GetMapping("/validar-token")
    public ResponseEntity<Response> validarToken() {
        return ResponseEntity.ok(new Response("success", "Token valido", ""));
    }

    @GetMapping("/rol")
    public ResponseEntity<Response> obtenerRol(@RequestHeader("Authorization") String token) {
        try {
            String rol = usuarioService.obtenerRolDesdeToken(token);
            return ResponseEntity.ok(new Response("success", rol, "Rol obtenido correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new Response("error", null, e.getMessage()));
        }
    }

}
