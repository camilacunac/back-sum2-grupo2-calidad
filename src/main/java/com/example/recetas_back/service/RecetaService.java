package com.example.recetas_back.service;

import com.example.recetas_back.model.Receta;
import com.example.recetas_back.model.Comentario;
import com.example.recetas_back.model.Response;
import org.springframework.http.ResponseEntity;

public interface RecetaService {

    ResponseEntity<Response> publicarReceta(Receta receta, String token);

    ResponseEntity<Response> getAllRecetas();

    ResponseEntity<Response> getRecetaById(Long id);

    ResponseEntity<Response> searchRecetas(String nombre, String dificultad, String tipoCocina, String paisOrigen);

    ResponseEntity<Response> agregarComentario(Long recetaId, Comentario comentario, String token);
}
