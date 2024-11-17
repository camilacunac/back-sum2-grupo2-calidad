package com.example.recetas_back.controller;

import com.example.recetas_back.model.Comentario;
import com.example.recetas_back.model.Receta;
import com.example.recetas_back.model.Response;
import com.example.recetas_back.service.RecetaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recetas")
public class RecetaController {

        @Autowired
        private RecetaService recetaService;

        // Obtener todas las recetas
        @GetMapping
        public ResponseEntity<Response> getAllRecetas() {
                return recetaService.getAllRecetas();
        }

        // Obtener receta por ID
        @GetMapping("/{id}")
        public ResponseEntity<Response> getRecetaById(@PathVariable Long id) {
                return recetaService.getRecetaById(id);
        }

        // Buscar recetas
        @GetMapping("/buscar")
        public ResponseEntity<Response> searchRecetas(
                        @RequestParam(required = false) String nombre,
                        @RequestParam(required = false) String dificultad,
                        @RequestParam(required = false) String tipoCocina,
                        @RequestParam(required = false) String paisOrigen) {
                return recetaService.searchRecetas(nombre, dificultad, tipoCocina, paisOrigen);
        }

        @PostMapping("/publicar")
        public ResponseEntity<Response> publicarReceta(
                        @RequestBody Receta receta,
                        @RequestHeader("Authorization") String authorizationHeader) {
                try {
                        String token = authorizationHeader.replace("Bearer ", "");
                        return recetaService.publicarReceta(receta, token);
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(new Response("error", null, e.getMessage()));
                }
        }

        @PostMapping("/{id}/comentarios")
        public ResponseEntity<Response> agregarComentario(
                        @PathVariable Long id,
                        @RequestBody Comentario comentario,
                        @RequestHeader("Authorization") String token) {
                String jwt = token.replace("Bearer ", "");
                return recetaService.agregarComentario(id, comentario, jwt);
        }

}
