package com.example.proyecto_acueducto.Controller;

import com.example.proyecto_acueducto.Dto.LoginRequest;
import com.example.proyecto_acueducto.Model.Usuario;
import com.example.proyecto_acueducto.Service.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // =========================
    // LISTAR USUARIOS
    // =========================
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    // =========================
    // CREAR USUARIO
    // =========================
    @PostMapping
    public ResponseEntity<Usuario> guardar(@RequestBody Usuario usuario) {
        return new ResponseEntity<>(
                usuarioService.guardar(usuario),
                HttpStatus.CREATED
        );
    }

    // =========================
    // BUSCAR POR USERNAME
    // =========================
    @GetMapping("/username/{username}")
    public ResponseEntity<Usuario> buscar(@PathVariable String username) {
        return usuarioService.buscarPorUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // ACTUALIZAR
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(
            @PathVariable Long id,
            @RequestBody Usuario usuario
    ) {
        return ResponseEntity.ok(usuarioService.actualizar(id, usuario));
    }

    // =========================
    // ELIMINAR
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // LOGIN (CORRECTO)
    // =========================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        var usuarioOpt = usuarioService.login(
                request.getIdentificacion(),
                request.getPassword()
        );

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales incorrectas");
        }

        return ResponseEntity.ok(usuarioOpt.get());
    }
}