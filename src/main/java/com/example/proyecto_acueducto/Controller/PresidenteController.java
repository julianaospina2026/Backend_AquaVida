package com.example.proyecto_acueducto.Controller;

import com.example.proyecto_acueducto.Repository.LecturaRepository;
import com.example.proyecto_acueducto.Repository.PagoRepository;
import com.example.proyecto_acueducto.Service.PresidenteService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/presidente")
@CrossOrigin(origins = "*")
public class PresidenteController {

    private final PresidenteService presidenteService;
    private final LecturaRepository lecturaRepository;
    private final PagoRepository pagoRepository;

    public PresidenteController(
            PresidenteService presidenteService,
            LecturaRepository lecturaRepository,
            PagoRepository pagoRepository) {

        this.presidenteService = presidenteService;
        this.lecturaRepository = lecturaRepository;
        this.pagoRepository = pagoRepository;
    }

    // 📊 Dashboard principal
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        return ResponseEntity.ok(
                presidenteService.obtenerResumenEjecutivo()
        );
    }

    // 💰 Todos los pagos
    @GetMapping("/pagos")
    public ResponseEntity<?> getPagos() {
        return ResponseEntity.ok(
                pagoRepository.findAll()
        );
    }

    // 📊 Todas las lecturas
    @GetMapping("/lecturas")
    public ResponseEntity<?> getLecturas() {
        return ResponseEntity.ok(
                lecturaRepository.findAll()
        );
    }

    // 📈 Historial de lecturas por cliente
    @GetMapping("/historial/{clienteId}")
    public ResponseEntity<?> getHistorialCliente(
            @PathVariable Long clienteId) {

        return ResponseEntity.ok(
                lecturaRepository.findByClienteId(clienteId)
        );
    }

    // 🧪 Prueba de funcionamiento
    @GetMapping("/saludo")
    public ResponseEntity<String> saludo() {
        return ResponseEntity.ok(
                "Panel Presidencial Activo 🏛️"
        );
    }
}