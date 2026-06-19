package com.example.proyecto_acueducto.Service;

import com.example.proyecto_acueducto.Dto.PagoDTO;
import com.example.proyecto_acueducto.Dto.ReciboDTO;
import com.example.proyecto_acueducto.Model.Factura;
import com.example.proyecto_acueducto.Model.Pago;
import com.example.proyecto_acueducto.Repository.FacturaRepository;
import com.example.proyecto_acueducto.Repository.PagoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final FacturaRepository facturaRepository;
    private final PdfService pdfService;

    public PagoService(
            PagoRepository pagoRepository,
            FacturaRepository facturaRepository,
            PdfService pdfService) {

        this.pagoRepository = pagoRepository;
        this.facturaRepository = facturaRepository;
        this.pdfService = pdfService;
    }

    // =====================================================
    // REGISTRAR PAGO (NO TOCAR LOGICA)
    // =====================================================
    @Transactional
    public Pago registrar(Pago pago) {

        if (pago.getFactura() == null || pago.getFactura().getId() == null) {
            throw new IllegalArgumentException("Debe enviar el ID de la factura");
        }

        Factura factura = facturaRepository.findById(pago.getFactura().getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Factura no encontrada con ID: " + pago.getFactura().getId()
                        )
                );

        if ("PAGADA".equalsIgnoreCase(factura.getEstado())) {
            throw new IllegalStateException("La factura ya está pagada");
        }

        pago.setFactura(factura);

        Pago guardado = pagoRepository.save(pago);

        factura.setEstado("PAGADA");
        facturaRepository.save(factura);

        return guardado;
    }

    // =====================================================
    // RECIBO (SAFE VERSION - SIN CRASH)
    // =====================================================
    public ReciboDTO generarRecibo(Pago pago) {

        ReciboDTO r = new ReciboDTO();

        r.setIdPago(pago.getId());
        r.setFacturaId(pago.getFactura().getId());
        r.setNumeroFactura(pago.getFactura().getNumeroFactura());

        r.setMonto(pago.getMonto());
        r.setMetodoPago(pago.getMetodoPago());
        r.setFechaPago(pago.getFechaPago());

        // 🔥 FIX DEFINITIVO (evita 500 por Lazy o null)
        String nombreCliente = "Sin cliente";

        if (pago.getFactura() != null
                && pago.getFactura().getLectura() != null
                && pago.getFactura().getLectura().getCliente() != null
                && pago.getFactura().getLectura().getCliente().getNombre() != null) {

            nombreCliente = pago.getFactura()
                    .getLectura()
                    .getCliente()
                    .getNombre();
        }

        r.setClienteNombre(nombreCliente);

        return r;
    }

    // =====================================================
    // PDF
    // =====================================================
    public byte[] generarReciboPdf(Long pagoId) {

        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() ->
                        new RuntimeException("Pago no encontrado"));

        return pdfService.generarReciboPdf(generarRecibo(pago));
    }

    // =====================================================
    // PAGOS POR FACTURA
    // =====================================================
    public List<Pago> listarPorFactura(Long facturaId) {
        return pagoRepository.findByFacturaId(facturaId);
    }

    // =====================================================
    // PAGOS POR CLIENTE (ENTIDAD)
    // =====================================================
    public List<Pago> listarPorCliente(Long clienteId) {
        return pagoRepository.findByClienteId(clienteId);
    }

    // =====================================================
    // PAGOS POR CLIENTE (DTO)
    // =====================================================
    public List<PagoDTO> listarPorClienteDTO(Long clienteId) {

        return pagoRepository.findByClienteId(clienteId)
                .stream()
                .map(p -> {

                    PagoDTO dto = new PagoDTO();

                    dto.setId(p.getId());
                    dto.setFacturaId(
                            p.getFactura() != null ? p.getFactura().getId() : null
                    );

                    dto.setMonto(p.getMonto());
                    dto.setFechaPago(p.getFechaPago());
                    dto.setMetodoPago(p.getMetodoPago());

                    return dto;
                })
                .toList();
    }

    // =====================================================
    // ADMIN DASHBOARD (ESTE TE FALTABA PROBABLEMENTE)
    // =====================================================
    public List<Pago> listarTodos() {
        return pagoRepository.findAllByOrderByFechaPagoDesc();
    }
}
