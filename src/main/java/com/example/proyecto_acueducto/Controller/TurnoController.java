package com.example.proyecto_acueducto.Controller;

import com.example.proyecto_acueducto.Model.Turno;
import com.example.proyecto_acueducto.Service.TurnoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/turnos")
public class TurnoController {
    private final TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    @GetMapping
    public List<Turno> listar() {
        return turnoService.listarTodos();
    }

    @PostMapping
    public Turno programar(@RequestBody Turno turno) {
        return turnoService.guardar(turno);
    }

    @PatchMapping("/{id}/estado")
    public Turno actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return turnoService.cambiarEstado(id, estado);
    }
    
}
