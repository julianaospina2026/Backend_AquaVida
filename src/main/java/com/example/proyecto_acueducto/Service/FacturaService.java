package com.example.proyecto_acueducto.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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

    private static final Logger log =
            LoggerFactory.getLogger(FacturaService.class);

    private final FacturaRepository facturaRepository;
    private final LecturaRepository lecturaRepository;
    private final CuotaFinanciacionRepository cuotaRepository;

    private static final BigDecimal VALOR_M3 = new BigDecimal("2500");
    private static final BigDecimal CARGO_FIJO = new BigDecimal("8000");

    public FacturaService(
            FacturaRepository facturaRepository,
            LecturaRepository lecturaRepository,
            CuotaFinanciacionRepository cuotaRepository
    ) {
        this.facturaRepository = facturaRepository;
        this.lecturaRepository = lecturaRepository;
        this.cuotaRepository = cuotaRepository;
    }

    // =========================
    // CONSULTAS
    // =========================

    public List<Factura> listarTodas() {
        return facturaRepository.findAll();
    }

    public List<Factura> listarPorCliente(Long clienteId) {
        return facturaRepository.findByClienteId(clienteId);
    }

    public List<Factura> listarPendientesPorCliente(Long clienteId) {
        return facturaRepository.findPendientesPorCliente(clienteId);
    }

    public Factura buscarPorId(Long id) {
        return facturaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Factura no encontrada: " + id));
    }

    public Factura buscarPorLectura(Long lecturaId) {
        return facturaRepository.findByLecturaId(lecturaId)
                .orElse(null);
    }

    // =========================
    // FACTURA INDIVIDUAL
    // =========================

    @Transactional
    public Factura generarFacturaPorLectura(Long lecturaId) {

        Lectura lectura = lecturaRepository.findById(lecturaId)
                .orElseThrow(() ->
                        new RuntimeException("Lectura no encontrada"));

        Factura existente = facturaRepository
                .findByLecturaId(lecturaId)
                .orElse(null);

        if (existente != null) {
            return existente;
        }

        List<CuotaFinanciacion> cuotasPendientes =
                cuotaRepository.findByEstado("PENDIENTE");

        if (cuotasPendientes == null) {
            cuotasPendientes = List.of();
        }

        Factura factura = new Factura();

        factura.setLectura(lectura);
        factura.setFechaEmision(LocalDate.now());
        factura.setFechaVencimiento(LocalDate.now().plusDays(15));
        factura.setEstado("PENDIENTE");

        factura.setCargoFijo(CARGO_FIJO);

        BigDecimal consumo = lectura.getConsumoM3() != null
                ? lectura.getConsumoM3()
                : BigDecimal.ZERO;

        BigDecimal valorConsumo = consumo.multiply(VALOR_M3);

        factura.setValorConsumo(valorConsumo);

        BigDecimal otrosCobros = BigDecimal.ZERO;

        if (lectura.getCliente() != null) {

            Long clienteId = lectura.getCliente().getId();

            for (CuotaFinanciacion cuota : cuotasPendientes) {

                if (cuota != null
                        && cuota.getFinanciacion() != null
                        && cuota.getFinanciacion().getCliente() != null
                        && clienteId.equals(
                        cuota.getFinanciacion().getCliente().getId())) {

                    if (cuota.getValor() != null) {
                        otrosCobros = otrosCobros.add(cuota.getValor());
                    }
                }
            }
        }

        factura.setOtrosCobros(otrosCobros);

        factura.setTotalPagar(
                valorConsumo
                        .add(CARGO_FIJO)
                        .add(otrosCobros)
        );

        Factura facturaGuardada = facturaRepository.save(factura);

        facturaGuardada.setNumeroFactura(
                String.format(
                        "FAC-%06d",
                        facturaGuardada.getId()
                )
        );

        return facturaRepository.save(facturaGuardada);
    }

    // =========================
    // GENERACIÓN MASIVA
    // =========================

    @Transactional
    public void generarFacturasMasivas(String periodo) {

        List<Lectura> lecturas = lecturaRepository
                .findByPeriodo(periodo, Pageable.unpaged())
                .getContent();

        log.info("Lecturas encontradas: {}", lecturas.size());

        Set<Long> lecturasConFactura = facturaRepository.findAll()
                .stream()
                .map(f -> f.getLectura() != null
                        ? f.getLectura().getId()
                        : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<CuotaFinanciacion> cuotasPendientes =
                cuotaRepository.findByEstado("PENDIENTE");

        if (cuotasPendientes == null) {
            cuotasPendientes = List.of();
        }

        int creadas = 0;

        for (Lectura lectura : lecturas) {

            if (lectura == null || lectura.getId() == null) {
                continue;
            }

            if (lecturasConFactura.contains(lectura.getId())) {
                continue;
            }

            Factura factura = new Factura();

            factura.setLectura(lectura);
            factura.setFechaEmision(LocalDate.now());
            factura.setFechaVencimiento(LocalDate.now().plusDays(15));
            factura.setEstado("PENDIENTE");

            factura.setCargoFijo(CARGO_FIJO);

            BigDecimal consumo = lectura.getConsumoM3() != null
                    ? lectura.getConsumoM3()
                    : BigDecimal.ZERO;

            BigDecimal valorConsumo = consumo.multiply(VALOR_M3);

            factura.setValorConsumo(valorConsumo);

            BigDecimal otrosCobros = BigDecimal.ZERO;

            if (lectura.getCliente() != null) {

                Long clienteId = lectura.getCliente().getId();

                for (CuotaFinanciacion cuota : cuotasPendientes) {

                    if (cuota != null
                            && cuota.getFinanciacion() != null
                            && cuota.getFinanciacion().getCliente() != null
                            && clienteId.equals(
                            cuota.getFinanciacion().getCliente().getId())) {

                        if (cuota.getValor() != null) {
                            otrosCobros = otrosCobros.add(cuota.getValor());
                        }
                    }
                }
            }

            factura.setOtrosCobros(otrosCobros);

            factura.setTotalPagar(
                    valorConsumo
                            .add(CARGO_FIJO)
                            .add(otrosCobros)
            );

            Factura facturaGuardada =
                    facturaRepository.save(factura);

            facturaGuardada.setNumeroFactura(
                    String.format(
                            "FAC-%06d",
                            facturaGuardada.getId()
                    )
            );

            facturaRepository.save(facturaGuardada);

            creadas++;
        }

        log.info("Facturas creadas: {}", creadas);
    }
}
