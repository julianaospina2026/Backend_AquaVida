package com.example.proyecto_acueducto.Controller;

import com.example.proyecto_acueducto.Model.Rol;
import com.example.proyecto_acueducto.Service.RolService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {
    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public List<Rol> listar() {
        return rolService.listarTodos();
    }
    
}
