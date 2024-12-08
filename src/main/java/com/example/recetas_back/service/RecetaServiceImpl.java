package com.example.recetas_back.service;

import com.example.recetas_back.model.Receta;
import com.example.recetas_back.model.Comentario;
import com.example.recetas_back.model.ComentarioDTO;
import com.example.recetas_back.model.Response;
import com.example.recetas_back.model.Usuario;
import com.example.recetas_back.repository.RecetaRepository;
import com.example.recetas_back.repository.UsuarioRepository;
import com.example.recetas_back.util.JWTUtil;
import com.example.recetas_back.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecetaServiceImpl implements RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public RecetaServiceImpl(RecetaRepository recetaRepository, ComentarioRepository comentarioRepository,
            JWTUtil jwtUtil, UsuarioRepository usuarioRepository) {
        this.recetaRepository = recetaRepository;
        this.comentarioRepository = comentarioRepository;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public ResponseEntity<Response> publicarReceta(Receta receta, String token) {
        try {
            // Obtener el correo del usuario desde el token
            String correoUsuario = jwtUtil.extractUsername(token);

            // Buscar el usuario en la base de datos
            Usuario usuario = usuarioRepository.findByCorreo(correoUsuario);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Response("error", null, "Usuario no encontrado"));
            }

            // Asignar el usuario a la receta
            receta.setUsuario(usuario);

            // Guardar la receta en la base de datos
            Receta nuevaReceta = recetaRepository.save(receta);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new Response("success", nuevaReceta, ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("error", null, e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<Response> getAllRecetas() {
        try {
            List<Receta> recetas = recetaRepository.findAll();
            return ResponseEntity.ok(new Response("success", recetas, ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("error", null, e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<Response> getRecetaById(Long id) {
        try {
            Optional<Receta> recetaOpt = recetaRepository.findById(id);
            if (recetaOpt.isPresent()) {
                return ResponseEntity.ok(new Response("success", recetaOpt.get(), ""));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response("error", null, "Receta no encontrada"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("error", null, e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<Response> searchRecetas(String nombre, String dificultad, String tipoCocina,
            String paisOrigen) {
        try {
            List<Receta> recetas = recetaRepository.findAll().stream()
                    .filter(receta -> (nombre == null
                            || receta.getNombre().toLowerCase().contains(nombre.toLowerCase())) &&
                            (dificultad == null || receta.getDificultad().equalsIgnoreCase(dificultad)) &&
                            (tipoCocina == null || receta.getTipoCocina().equalsIgnoreCase(tipoCocina)) &&
                            (paisOrigen == null || receta.getPaisOrigen().equalsIgnoreCase(paisOrigen)))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new Response("success", recetas, ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("error", null, e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<Response> agregarComentario(Long recetaId, Comentario comentario, String token) {
        try {
            // Obtener el correo del usuario autenticado desde el token JWT
            String correoUsuario = jwtUtil.extractUsername(token);

            // Buscar el usuario en la base de datos
            Usuario usuario = usuarioRepository.findByCorreo(correoUsuario);

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Response("error", null, "Usuario no encontrado"));
            }

            // Buscar la receta a la que se quiere agregar el comentario
            Optional<Receta> recetaOpt = recetaRepository.findById(recetaId);
            if (!recetaOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Response("error", null, "Receta no encontrada"));
            }

            // Asociar el comentario con el usuario y la receta
            Receta receta = recetaOpt.get();
            comentario.setReceta(receta);
            comentario.setUsuario(usuario);
            comentario.setFechaComentario(new Date());

            // Guardar el comentario en la base de datos
            Comentario nuevoComentario = comentarioRepository.save(comentario);
            return ResponseEntity.ok(new Response("success", nuevoComentario, "Comentario agregado con éxito"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("error", null, e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<Response> getAllComentarios(String token) {
        try {
            // Verificar el rol del usuario
            String rol = jwtUtil.extractRole(token.replace("Bearer ", ""));
            if (!"admin".equalsIgnoreCase(rol)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new Response("error", null, "No tienes permisos para acceder a esta funcionalidad"));
            }

            // Obtener todos los comentarios
            List<Comentario> comentarios = comentarioRepository.findAll();

            // Mapear los comentarios a la clase de respuesta personalizada
            List<ComentarioDTO> comentarioResponses = comentarios.stream()
                    .map(comentario -> new ComentarioDTO(
                            comentario.getId(),
                            comentario.getContenido(),
                            comentario.getFechaComentario(),
                            comentario.getUsuario().getNombre(),
                            comentario.getUsuario().getApellido(),
                            comentario.getReceta().getNombre()))
                    .collect(Collectors.toList());

            // Devolver la respuesta
            return ResponseEntity.ok(new Response("success", comentarioResponses, ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("error", null, e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<Response> eliminarComentario(Long comentarioId, String token) {
        try {
            // Extraer el rol del token
            String rol = jwtUtil.extractRole(token.replace("Bearer ", ""));

            if (!"admin".equalsIgnoreCase(rol)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new Response("error", null, "No tienes permisos para realizar esta acción"));
            }

            // Verificar si el comentario existe
            Optional<Comentario> comentarioOpt = comentarioRepository.findById(comentarioId);
            if (!comentarioOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Response("error", null, "Comentario no encontrado"));
            }

            // Eliminar el comentario
            comentarioRepository.deleteById(comentarioId);
            return ResponseEntity.ok(new Response("success", null, "Comentario eliminado con éxito"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("error", null, e.getMessage()));
        }
    }

}
