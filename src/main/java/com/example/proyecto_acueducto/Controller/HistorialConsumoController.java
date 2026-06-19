package com.example.proyecto_acueducto.Controller;

import com.example.proyecto_acueducto.Model.Lectura;
import com.example.proyecto_acueducto.Service.HistorialConsumoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/historial-consumo")
public class HistorialConsumoController {
    private final HistorialConsumoService service;

    public HistorialConsumoController(HistorialConsumoService service) {
        this.service = service;
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Lectura> obtenerConsumoHistorico(@PathVariable Long clienteId) {
        return service.obtenerPorCliente(clienteId);
    }
    
}
