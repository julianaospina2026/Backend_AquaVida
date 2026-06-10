package com.example.proyecto_acueducto.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.proyecto_acueducto.Model.CuotaFinanciacion;
import com.example.proyecto_acueducto.Model.Factura;
import com.example.proyecto_acueducto.Model.Lectura;
import com.example.proyecto_acueducto.Repository.CuotaFinanciacionRepository;
import com.example.proyecto_acueducto.Repository.FacturaRepository;
import com.example.proyecto_acueducto.Repository.LecturaRepository;

@Service
public class FacturaService {
    private static final Logger log = LoggerFactory.getLogger(FacturaService.class);
    
    private final FacturaRepository facturaRepository;
    private final LecturaRepository lecturaRepository;
    private final CuotaFinanciacionRepository cuotaRepository;

    // Valores por defecto para un acueducto veredal (podrían venir de una tabla de configuración)
    private static final BigDecimal VALOR_M3 = new BigDecimal("2500");
    private static final BigDecimal CARGO_FIJO = new BigDecimal("8000");

    public FacturaService(FacturaRepository facturaRepository, 
        LecturaRepository lecturaRepository,
        CuotaFinanciacionRepository cuotaRepository) {
        this.facturaRepository = facturaRepository;
        this.lecturaRepository = lecturaRepository;
        this.cuotaRepository = cuotaRepository;
    }

    public List<Factura> listarTodas() {
        return facturaRepository.findAll();
    }

    public Optional<Factura> buscarPorId(Long id) {
        return facturaRepository.findById(id);
    }

    public List<Factura> listarPorCliente(Long clienteId) {
        return facturaRepository.findByLecturaClienteId(clienteId);
    }

    @Transactional
    public void generarFacturasMasivas(String periodo) {
        // 1. Obtener todas las lecturas del periodo que no tengan factura
        List<Lectura> lecturas = lecturaRepository.findByPeriodo(periodo, Pageable.unpaged()).getContent();

        // OPTIMIZACIÓN: Cargar IDs de lecturas que ya tienen factura para evitar duplicados sin consultar en cada iteración
        Set<Long> lecturaIdsConFactura = facturaRepository.findAll().stream()
            .map(f -> f.getLectura() != null ? f.getLectura().getId() : null)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        // OPTIMIZACIÓN: Cargar cuotas pendientes una sola vez fuera del bucle
        List<CuotaFinanciacion> cuotasPendientes = cuotaRepository.findByEstado("PENDIENTE");
        if (cuotasPendientes == null) {
            cuotasPendientes = List.of();
        }

        for (Lectura lectura : lecturas) {
            // Evitar duplicados si ya existe factura para esta lectura
            if (lectura == null || lectura.getId() == null) {
                log.warn("Lectura nula o sin id encontrada en periodo {}: se omite.", periodo);
                continue;
            }
            if (lecturaIdsConFactura.contains(lectura.getId())) {
                log.debug("Ya existe factura para lecturaId={}", lectura.getId());
                continue;
            }

            Factura factura = new Factura();
            factura.setLectura(lectura);
            factura.setFechaEmision(LocalDate.now());
            factura.setFechaVencimiento(LocalDate.now().plusDays(15));
            factura.setEstado("PENDIENTE");
            factura.setCargoFijo(CARGO_FIJO);

            // Cálculo por consumo (defensivo si consumo es null)
            BigDecimal consumo = lectura.getConsumoM3() != null ? lectura.getConsumoM3() : BigDecimal.ZERO;
            BigDecimal valorConsumo = consumo.multiply(VALOR_M3);
            factura.setValorConsumo(valorConsumo);

            BigDecimal otrosCobros = BigDecimal.ZERO;
            
            // Filtramos en memoria las cuotas que pertenecen al cliente de la lectura
            if (lectura.getCliente() != null && lectura.getCliente().getId() != null) {
                Long clienteId = lectura.getCliente().getId();
                for (CuotaFinanciacion cuota : cuotasPendientes) {
                    if (cuota != null && cuota.getFinanciacion() != null && cuota.getFinanciacion().getCliente() != null
                            && clienteId.equals(cuota.getFinanciacion().getCliente().getId())) {
                        if (cuota.getValor() != null) {
                            otrosCobros = otrosCobros.add(cuota.getValor());
                        }
                        // En un sistema real, aquí marcaríamos la cuota como "PROCESADA_EN_FACTURA"
                    }
                }
            } else {
                log.warn("Lectura id={} no tiene cliente asociado, no se suman cuotas.", lectura.getId());
            }
            
            factura.setOtrosCobros(otrosCobros);

            // Total final
            BigDecimal total = valorConsumo.add(CARGO_FIJO).add(otrosCobros);
            factura.setTotalPagar(total);

            facturaRepository.save(factura);
            log.info("Factura generada: lecturaId={} total={}", lectura.getId(), factura.getTotalPagar());
        }
    }
}
