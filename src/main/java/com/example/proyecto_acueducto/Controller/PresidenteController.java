package com.example.proyecto_acueducto.Controller;

import com.example.proyecto_acueducto.Repository.LecturaRepository;
import com.example.proyecto_acueducto.Repository.PagoRepository;
import com.example.proyecto_acueducto.Service.PresidenteService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/presidente")
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

    // =========================
    // DASHBOARD
    // =========================
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        return ResponseEntity.ok(
                presidenteService.obtenerResumenEjecutivo()
        );
    }

    // =========================
    // PAGOS
    // =========================
    @GetMapping("/pagos")
    public ResponseEntity<?> getPagos() {
        return ResponseEntity.ok(
                pagoRepository.findAll()
        );
    }

    // =========================
    // LECTURAS
    // =========================
    @GetMapping("/lecturas")
    public ResponseEntity<?> getLecturas() {
        return ResponseEntity.ok(
                lecturaRepository.findAll()
        );
    }

    // =========================
    // HISTORIAL POR CLIENTE (FIX IMPORTANTE)
    // =========================
    @GetMapping("/historial/{clienteId}")
    public ResponseEntity<?> getHistorialCliente(@PathVariable Long clienteId) {

        return ResponseEntity.ok(
                lecturaRepository.findByClienteId(clienteId)
        );
    }

    // =========================
    // SALUDO
    // =========================
    @GetMapping("/saludo")
    public ResponseEntity<String> saludo() {
        return ResponseEntity.ok(
                "Panel Presidencial Activo 🏛️"
        );
    }
}