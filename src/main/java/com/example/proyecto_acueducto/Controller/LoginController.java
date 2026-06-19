package com.example.proyecto_acueducto.Controller;

import com.example.proyecto_acueducto.Dto.LoginRequest;
import com.example.proyecto_acueducto.Dto.LoginResponse;
import com.example.proyecto_acueducto.Model.Usuario;
import com.example.proyecto_acueducto.Service.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        if (request == null ||
            request.getIdentificacion() == null ||
            request.getPassword() == null) {

            return ResponseEntity.badRequest()
                    .body(new LoginResponse(null, null, "Datos incompletos"));
        }

        Usuario usuario = usuarioService.login(
                request.getIdentificacion(),
                request.getPassword()
        ).orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null, null, "Credenciales incorrectas"));
        }

        String rol = (usuario.getRol() != null && usuario.getRol().getNombre() != null)
                ? usuario.getRol().getNombre().toUpperCase()
                : "USUARIO";

        Long clienteId = (usuario.getCliente() != null)
                ? usuario.getCliente().getId()
                : null;

        LoginResponse response = new LoginResponse(
                rol,
                null,
                "Login exitoso",
                null,
                clienteId
        );

        return ResponseEntity.ok(response);
    }
}