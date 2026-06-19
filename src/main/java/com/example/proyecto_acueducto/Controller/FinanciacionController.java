package com.example.proyecto_acueducto.Controller;

import com.example.proyecto_acueducto.Model.Financiacion;
import com.example.proyecto_acueducto.Service.FinanciacionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/financiaciones")
public class FinanciacionController {

    private final FinanciacionService financiacionService;

    public FinanciacionController(FinanciacionService financiacionService) {
        this.financiacionService = financiacionService;
    }

    // =========================
    // CREAR FINANCIACIÓN
    // =========================
    @PostMapping
    public Financiacion crear(@RequestBody Financiacion financiacion) {
        return financiacionService.crear(financiacion);
    }

    // =========================
    // LISTAR POR CLIENTE
    // =========================
    @GetMapping("/cliente/{clienteId}")
    public List<Financiacion> listarPorCliente(@PathVariable Long clienteId) {
        return financiacionService.listarPorCliente(clienteId);
    }

    // =========================
    // OBTENER POR ID (NUEVO)
    // =========================
    @GetMapping("/{id}")
    public Financiacion obtenerPorId(@PathVariable Long id) {
        return financiacionService.obtenerPorId(id);
    }
}