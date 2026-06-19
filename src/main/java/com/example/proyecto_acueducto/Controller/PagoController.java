package com.example.proyecto_acueducto.Controller;

import com.example.proyecto_acueducto.Dto.PagoDTO;
import com.example.proyecto_acueducto.Dto.ReciboDTO;
import com.example.proyecto_acueducto.Model.Pago;
import com.example.proyecto_acueducto.Service.PagoService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    // =====================================================
    // 🔹 ADMIN: LISTAR TODOS LOS PAGOS (ESTE TE FALTABA)
    // =====================================================
    @GetMapping
    public ResponseEntity<List<Pago>> listarTodos() {
        return ResponseEntity.ok(pagoService.listarTodos());
    }

    // =====================================================
    // 🔹 1. REGISTRAR PAGO (RETORNA RECIBO)
    // =====================================================
    @PostMapping
    public ResponseEntity<?> registrarPago(@RequestBody Pago pago) {

        try {
            Pago resultado = pagoService.registrar(pago);

            ReciboDTO recibo = pagoService.generarRecibo(resultado);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(recibo);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar pago: " + e.getMessage());
        }
    }

    // =====================================================
    // 🔹 2. DESCARGAR RECIBO PDF
    // =====================================================
    @GetMapping("/recibo/{facturaId}")
    public ResponseEntity<byte[]> descargarRecibo(
            @PathVariable Long facturaId) {

        byte[] pdf = pagoService.generarReciboPdf(facturaId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=recibo-" + facturaId + ".pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // =====================================================
    // 🔹 3. PAGOS POR FACTURA
    // =====================================================
    @GetMapping("/factura/{facturaId}")
    public ResponseEntity<List<Pago>> obtenerPagosPorFactura(
            @PathVariable Long facturaId) {

        return ResponseEntity.ok(
                pagoService.listarPorFactura(facturaId)
        );
    }

    // =====================================================
    // 🔹 4. HISTORIAL POR CLIENTE (DTO)
    // =====================================================
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PagoDTO>> obtenerPorCliente(
            @PathVariable Long clienteId) {

        return ResponseEntity.ok(
                pagoService.listarPorClienteDTO(clienteId)
        );
    }
}