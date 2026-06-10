package com.example.proyecto_acueducto.Service;

import com.example.proyecto_acueducto.Repository.ClienteRepository;
import com.example.proyecto_acueducto.Repository.FacturaRepository;
import com.example.proyecto_acueducto.Repository.TurnoRepository;
import com.example.proyecto_acueducto.Model.Factura;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio especializado para las funcionalidades de alto nivel del Presidente.
 * Proporciona indicadores clave de rendimiento (KPIs) del sistema.
 */
@Service
public class PresidenteService {

    private final ClienteRepository clienteRepository;
    private final FacturaRepository facturaRepository;
    private final TurnoRepository turnoRepository;

    public PresidenteService(ClienteRepository clienteRepository, 
                             FacturaRepository facturaRepository, 
                             TurnoRepository turnoRepository) {
        this.clienteRepository = clienteRepository;
        this.facturaRepository = facturaRepository;
        this.turnoRepository = turnoRepository;
    }

    /**
     * Genera un resumen ejecutivo del estado financiero y operativo del acueducto.
     */
    public Map<String, Object> obtenerResumenEjecutivo() {
        Map<String, Object> resumen = new HashMap<>();
        
        long totalClientes = clienteRepository.count();
        
        BigDecimal recaudoTotal = facturaRepository.findByEstado("PAGADA").stream()
                .map(Factura::getTotalPagar)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        BigDecimal carteraPendiente = facturaRepository.findByEstado("PENDIENTE").stream()
                .map(Factura::getTotalPagar)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        long turnosPendientes = turnoRepository.findByEstado("PENDIENTE").size();

        resumen.put("totalSuscriptores", totalClientes);
        resumen.put("totalRecaudadoHistorico", recaudoTotal);
        resumen.put("carteraPorCobrar", carteraPendiente);
        resumen.put("mantenimientosPendientes", turnosPendientes);
        
        return resumen;
    }

    /**
     * Obtiene el listado de morosidad detallado para gestión administrativa.
     */
    public Map<String, Object> obtenerAnalisisMorosidad() {
        Map<String, Object> analisis = new HashMap<>();
        analisis.put("conteoMorosos", facturaRepository.findByEstado("PENDIENTE").stream().count());
        return analisis;
    }
}