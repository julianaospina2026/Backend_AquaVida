package com.example.proyecto_acueducto.Controller;

import com.example.proyecto_acueducto.Model.Usuario;
import com.example.proyecto_acueducto.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/crear-usuario")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {

        Usuario nuevoUsuario = usuarioService.guardar(usuario);

        return ResponseEntity.ok(nuevoUsuario);
    }
}