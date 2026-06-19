package com.example.proyecto_acueducto.Service;

import com.example.proyecto_acueducto.Model.Factura;
import com.example.proyecto_acueducto.Repository.FacturaRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ReportesService {

    private final FacturaRepository facturaRepository;

    public ReportesService(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    // =========================
    // RECAUDO TOTAL POR PERIODO (SEGURO Y OPTIMIZADO)
    // =========================
    public Map<String, BigDecimal> calcularRecaudoPeriodo(String periodo) {

        List<Factura> facturas =
                facturaRepository.findFacturasPagadasPorPeriodo(periodo);

        BigDecimal total = facturas.stream()
                .filter(f -> f != null && f.getTotalPagar() != null)
                .map(Factura::getTotalPagar)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> reporte = new HashMap<>();
        reporte.put("totalRecaudado", total);

        return reporte;
    }

    // =========================
    // CLIENTES CON DEUDA (SEGURO Y SIN NULLS)
    // =========================
    public Long contarClientesConDeuda() {

        List<Factura> facturas = facturaRepository.findByEstado("PENDIENTE");

        return facturas.stream()
                .filter(f -> f != null)
                .filter(f -> f.getLectura() != null)
                .filter(f -> f.getLectura().getCliente() != null)
                .map(f -> f.getLectura().getCliente().getId())
                .distinct()
                .count();
    }
}
