package com.example.proyecto_acueducto.Controller;

import com.example.proyecto_acueducto.Model.Lectura;
import com.example.proyecto_acueducto.Service.HistorialLecturasService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/historial-lecturas")
public class HistorialLecturasController {
    private final HistorialLecturasService service;

    public HistorialLecturasController(HistorialLecturasService service) {
        this.service = service;
    }

    @GetMapping("/cliente/{id}")
    public List<Lectura> obtenerHistorial(@PathVariable Long id) {
        return service.obtenerPorCliente(id);
    }
    
}
