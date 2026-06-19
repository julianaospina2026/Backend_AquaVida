package com.example.proyecto_acueducto.Controller;

import com.example.proyecto_acueducto.Model.Pago;
import com.example.proyecto_acueducto.Service.HistorialPagosService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/historial-pagos")
public class HistorialPagosController {
    private final HistorialPagosService service;

    public HistorialPagosController(HistorialPagosService service) {
        this.service = service;
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Pago> obtenerPagosCliente(@PathVariable Long clienteId) {
        return service.obtenerPorCliente(clienteId);
    }
    
}
