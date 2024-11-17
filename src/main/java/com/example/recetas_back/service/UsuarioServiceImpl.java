package com.example.recetas_back.service;

import com.example.recetas_back.model.Response;
import com.example.recetas_back.model.Usuario;
import com.example.recetas_back.repository.UsuarioRepository;
import com.example.recetas_back.util.JWTUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JWTUtil jwtTokenUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<Response> crearUsuario(Usuario usuario) throws Exception {
        Response response;
        try {
            if (!isValidEmail(usuario.getCorreo())) {
                response = new Response("error", null, "Correo no válido");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (!isValidPassword(usuario.getContrasena())) {
                response = new Response("error", null,
                        "Contraseña no válida. Debe tener al menos 8 caracteres, incluyendo una letra mayúscula, una minúscula, un número y un carácter especial.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (!isValidRole(usuario.getRol())) {
                response = new Response("error", null, "Rol no válido. Solo se permite 'admin' o 'cliente'.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Hashear la contraseña antes de guardarla
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            usuario.setFechaRegistro(LocalDate.now());

            // Guardar usuario
            Usuario newUser = usuarioRepository.save(usuario);
            response = new Response("success", newUser, "");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response = new Response("error", null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<Response> login(String correo, String contrasena) throws Exception {
        Response response;
        Usuario usuario = usuarioRepository.findByCorreo(correo);

        if (usuario == null) {
            response = new Response("error", null, "Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            response = new Response("error", null, "Contraseña incorrecta");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Generar el token JWT
        String token = jwtTokenUtil.generateToken(usuario.getCorreo());

        // Respuesta de éxito con el token en el cuerpo
        response = new Response("success", token, "Inicio de sesión exitoso");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // @Override
    // public void logout(HttpServletResponse response) {
    // // Esta lógica no es necesaria para servicios REST sin cookies,
    // // puede ser eliminada si no planeas usar cookies en la autenticación.
    // }

    @Override
    public ResponseEntity<Response> actualizarRol(Long idUsuario, String nuevoRol) throws Exception {
        Response response;
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (!usuarioOpt.isPresent()) {
            response = new Response("error", null, "Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Usuario usuario = usuarioOpt.get();
        if (!isValidRole(nuevoRol)) {
            response = new Response("error", null, "Rol no valido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        usuario.setRol(nuevoRol);
        Usuario updatedUser = usuarioRepository.save(usuario);
        response = new Response("success", updatedUser, "");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<Response> eliminarUsuario(Long idUsuario) throws Exception {
        Response response;
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (!usuarioOpt.isPresent()) {
            response = new Response("error", null, "Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        usuarioRepository.deleteById(idUsuario);
        response = new Response("success", "Usuario eliminado con éxito", "");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    // Funciones de validación
    public boolean isValidRole(String role) {
        String lowerCaseRole = role.toLowerCase();
        return lowerCaseRole.equals("admin") || lowerCaseRole.equals("cliente");
    }

    public boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }
}
