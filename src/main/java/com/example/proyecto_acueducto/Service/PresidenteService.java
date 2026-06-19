package com.example.proyecto_acueducto.Service;

import com.example.proyecto_acueducto.Model.Factura;
import com.example.proyecto_acueducto.Repository.ClienteRepository;
import com.example.proyecto_acueducto.Repository.FacturaRepository;
import com.example.proyecto_acueducto.Repository.TurnoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PresidenteService {

    private final ClienteRepository clienteRepository;
    private final FacturaRepository facturaRepository;
    private final TurnoRepository turnoRepository;

    public PresidenteService(
            ClienteRepository clienteRepository,
            FacturaRepository facturaRepository,
            TurnoRepository turnoRepository
    ) {
        this.clienteRepository = clienteRepository;
        this.facturaRepository = facturaRepository;
        this.turnoRepository = turnoRepository;
    }

    // =========================
    // RESUMEN EJECUTIVO (SEGURO Y OPTIMIZADO)
    // =========================
    public Map<String, Object> obtenerResumenEjecutivo() {

        Map<String, Object> resumen = new HashMap<>();

        long totalClientes = clienteRepository.count();

        List<Factura> facturasPagadas =
                facturaRepository.findByEstado("PAGADA");

        List<Factura> facturasPendientes =
                facturaRepository.findByEstado("PENDIENTE");

        BigDecimal recaudoTotal = facturasPagadas.stream()
                .filter(f -> f != null && f.getTotalPagar() != null)
                .map(Factura::getTotalPagar)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal carteraPendiente = facturasPendientes.stream()
                .filter(f -> f != null && f.getTotalPagar() != null)
                .map(Factura::getTotalPagar)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long turnosPendientes =
                turnoRepository.findByEstado("PROGRAMADO").size();

        resumen.put("totalSuscriptores", totalClientes);
        resumen.put("totalRecaudadoHistorico", recaudoTotal);
        resumen.put("carteraPorCobrar", carteraPendiente);
        resumen.put("mantenimientosPendientes", turnosPendientes);

        return resumen;
    }

    // =========================
    // ANÁLISIS DE MOROSIDAD (CORREGIDO)
    // =========================
    public Map<String, Object> obtenerAnalisisMorosidad() {

        Map<String, Object> analisis = new HashMap<>();
        
        // Alineado con ReportesService para contar Clientes únicos, no facturas
        long conteoMorosos =
                facturaRepository.findByEstado("PENDIENTE").stream()
                        .filter(f -> f != null && f.getLectura() != null && f.getLectura().getCliente() != null)
                        .map(f -> f.getLectura().getCliente().getId())
                        .distinct()
                        .count();

        analisis.put("conteoMorosos", conteoMorosos);

        return analisis;
    }
}