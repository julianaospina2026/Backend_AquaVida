package com.example.proyecto_acueducto.Controller;

import com.example.proyecto_acueducto.Model.Factura;
import com.example.proyecto_acueducto.Service.FacturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    // =========================
    // TODAS LAS FACTURAS
    // =========================
    @GetMapping
    public ResponseEntity<List<Factura>> listar() {
        return ResponseEntity.ok(facturaService.listarTodas());
    }

    // =========================
    // FACTURA POR ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Factura> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.buscarPorId(id));
    }

    // =========================
    // FACTURAS POR CLIENTE
    // =========================
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Factura>> listarPorCliente(
            @PathVariable Long clienteId) {

        List<Factura> facturas =
                facturaService.listarPorCliente(clienteId);

        if (facturas == null) {
            return ResponseEntity.ok(List.of());
        }

        return ResponseEntity.ok(facturas);
    }

    // =========================
    // FACTURAS PENDIENTES
    // =========================
    @GetMapping("/cliente/{clienteId}/pendientes")
    public ResponseEntity<List<Factura>> pendientes(
            @PathVariable Long clienteId) {

        List<Factura> pendientes =
                facturaService.listarPendientesPorCliente(clienteId);

        if (pendientes == null) {
            return ResponseEntity.ok(List.of());
        }

        return ResponseEntity.ok(pendientes);
    }

    // =========================
    // FACTURA POR LECTURA
    // =========================
    @GetMapping("/lectura/{lecturaId}")
    public ResponseEntity<Factura> obtenerPorLectura(
            @PathVariable Long lecturaId) {

        Factura factura =
                facturaService.buscarPorLectura(lecturaId);

        return factura != null
                ? ResponseEntity.ok(factura)
                : ResponseEntity.notFound().build();
    }

    // =========================
    // GENERAR FACTURA INDIVIDUAL
    // =========================
    @PostMapping("/generar/{lecturaId}")
    public ResponseEntity<Factura> generarFactura(
            @PathVariable Long lecturaId) {

        Factura factura =
                facturaService.generarFacturaPorLectura(lecturaId);

        return ResponseEntity.ok(factura);
    }

    // =========================
    // GENERAR FACTURAS POR PERIODO
    // =========================
    @PostMapping("/generar-periodo")
    public ResponseEntity<String> generarFacturasMes(
            @RequestParam String periodo) {

        facturaService.generarFacturasMasivas(periodo);

        return ResponseEntity.ok(
                "Proceso de facturación ejecutado correctamente para el periodo: "
                        + periodo
        );
    }

    // =========================
    // DEBUG
    // =========================
    @GetMapping("/debug")
    public ResponseEntity<List<Factura>> debug() {
        return ResponseEntity.ok(
                facturaService.listarTodas()
        );
    }
}
