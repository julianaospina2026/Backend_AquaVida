package com.example.proyecto_acueducto.Controller;

import java.util.Optional;

import com.example.proyecto_acueducto.Dto.LoginRequest;
import com.example.proyecto_acueducto.Dto.LoginResponse;
import com.example.proyecto_acueducto.Service.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String identificacion = request.getIdentificacion() != null ? request.getIdentificacion().trim() : "";
        String password = request.getPassword() != null ? request.getPassword().trim() : "";

        if (identificacion.isBlank() || password.isBlank()) {
            return new ResponseEntity<>(new LoginResponse(null, null, "Debe ingresar correo o cédula y contraseña"), HttpStatus.BAD_REQUEST);
        }

        if ("admin@gmail.com".equalsIgnoreCase(identificacion) && "12345".equals(password)) {
            return ResponseEntity.ok(new LoginResponse("ADMIN", "/admin.html", "Bienvenido administrador"));
        }

        if ("operador@gmail.com".equalsIgnoreCase(identificacion) && "4224".equals(password)) {
            return ResponseEntity.ok(new LoginResponse("OPERADOR", "/operador.html", "Bienvenido operador"));
        }
        
        if ("presidente@gmail.com".equalsIgnoreCase(identificacion) && "presi2024".equals(password)) {
            return ResponseEntity.ok(new LoginResponse("PRESIDENTE", "/presidente.html", "Bienvenido Presidente"));
        }
        return Optional.ofNullable(usuarioService.login(identificacion, password))
                .map(usuario -> {
                    String rolNombre = "USUARIO";
                    String redirectUrl = "/usuario.html";

                    // Validación dinámica de rol y redirección
                    if (usuario.getRol() != null && usuario.getRol().getNombre() != null) {
                        String dbRol = usuario.getRol().getNombre().toUpperCase();
                        rolNombre = dbRol;
                        
                        if (dbRol.contains("ADMIN")) {
                            redirectUrl = "/admin.html";
                        } else if (dbRol.contains("PRESIDENTE")) {
                            redirectUrl = "/presidente.html";
                        } else if (dbRol.contains("OPERADOR")) {
                            redirectUrl = "/operador.html";
                        }
                    }

                    return new ResponseEntity<>(
                        new LoginResponse(rolNombre, redirectUrl, "Ingreso exitoso"),
                        HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(
                        new LoginResponse(null, null, "Credenciales incorrectas"),
                        HttpStatus.UNAUTHORIZED));
    }
}
