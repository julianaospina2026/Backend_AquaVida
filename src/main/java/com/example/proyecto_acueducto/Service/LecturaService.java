package com.example.proyecto_acueducto.Service;

import com.example.proyecto_acueducto.Model.Cliente;
import com.example.proyecto_acueducto.Model.Lectura;
import com.example.proyecto_acueducto.Model.Factura;
import com.example.proyecto_acueducto.Repository.ClienteRepository;
import com.example.proyecto_acueducto.Repository.LecturaRepository;
import com.example.proyecto_acueducto.Repository.FacturaRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LecturaService {

    private final LecturaRepository lecturaRepository;
    private final ClienteRepository clienteRepository;
    private final FacturaRepository facturaRepository;

    private static final BigDecimal CONSUMO_ALTO = new BigDecimal("80");
    private static final BigDecimal PRECIO_M3 = new BigDecimal("2500");

    public LecturaService(
            LecturaRepository lecturaRepository,
            ClienteRepository clienteRepository,
            FacturaRepository facturaRepository) {

        this.lecturaRepository = lecturaRepository;
        this.clienteRepository = clienteRepository;
        this.facturaRepository = facturaRepository;
    }

    @Transactional
    public Lectura guardar(Lectura lectura) {

        validarDatos(lectura);

        Cliente cliente = clienteRepository.findById(lectura.getCliente().getId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

        validarDuplicado(cliente.getId(), lectura.getPeriodo());

        BigDecimal anterior = obtenerUltimaLectura(cliente.getId());

        Lectura lecturaGuardada = construirYGuardar(lectura, cliente, anterior);

        Lectura lecturaReal = lecturaRepository.findById(lecturaGuardada.getId())
                .orElseThrow(() -> new EntityNotFoundException("Lectura no encontrada después de guardar"));

        generarFactura(lecturaReal);

        return lecturaGuardada;
    }

    private void validarDatos(Lectura lectura) {

        if (lectura.getCliente() == null || lectura.getCliente().getId() == null) {
            throw new IllegalArgumentException("Cliente inválido");
        }

        if (lectura.getLecturaActual() == null) {
            throw new IllegalArgumentException("Lectura actual requerida");
        }

        if (lectura.getPeriodo() == null || lectura.getPeriodo().isBlank()) {
            throw new IllegalArgumentException("Periodo requerido");
        }
    }

    private void validarDuplicado(Long clienteId, String periodo) {
        if (lecturaRepository.existsByClienteIdAndPeriodo(clienteId, periodo)) {
            throw new IllegalStateException("Ya existe lectura en ese periodo");
        }
    }

    private BigDecimal obtenerUltimaLectura(Long clienteId) {

        return lecturaRepository
                .findTopByClienteIdOrderByIdDesc(clienteId)
                .map(Lectura::getLecturaActual)
                .orElse(BigDecimal.ZERO);
    }

    private Lectura construirYGuardar(Lectura lectura, Cliente cliente, BigDecimal anterior) {

        BigDecimal actual = lectura.getLecturaActual()
                .setScale(3, RoundingMode.HALF_UP);

        if (actual.compareTo(anterior) < 0) {
            throw new IllegalArgumentException("Lectura inválida: menor a la anterior");
        }

        BigDecimal consumo = actual.subtract(anterior);

        // 🔥 VALIDACIÓN NUEVA (EVITA FACTURAS EN 0)
        if (consumo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("No se permite consumo 0, no se genera lectura");
        }

        lectura.setCliente(cliente);
        lectura.setLecturaAnterior(anterior);
        lectura.setLecturaActual(actual);
        lectura.setConsumoM3(consumo);

        lectura.setValor(consumo.multiply(PRECIO_M3));

        if (lectura.getFechaLectura() == null) {
            lectura.setFechaLectura(LocalDate.now());
        }

        if (consumo.compareTo(CONSUMO_ALTO) > 0) {
            lectura.setObservacion("Consumo alto");
        } else {
            lectura.setObservacion("Consumo normal");
        }

        return lecturaRepository.save(lectura);
    }

    private void generarFactura(Lectura lectura) {

        System.out.println("🔥 GENERANDO FACTURA LECTURA ID: " + lectura.getId());

        if (lectura.getId() == null || lectura.getCliente() == null) {
            throw new RuntimeException("Datos inválidos para factura");
        }

        BigDecimal consumo = lectura.getLecturaActual()
                .subtract(lectura.getLecturaAnterior());

        // 🔥 VALIDACIÓN CLAVE: EVITAR FACTURAS EN 0
        if (consumo.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("⚠ No se genera factura: consumo 0 o inválido");
            return;
        }

        BigDecimal valorConsumo = consumo.multiply(PRECIO_M3);

        Factura factura = new Factura();

        factura.setCliente(lectura.getCliente());
        factura.setLectura(lectura);

        factura.setValorConsumo(valorConsumo);
        factura.setCargoFijo(BigDecimal.ZERO);
        factura.setOtrosCobros(BigDecimal.ZERO);
        factura.setTotalPagar(valorConsumo);

        factura.setFechaEmision(LocalDate.now());
        factura.setFechaVencimiento(LocalDate.now().plusDays(15));

        factura.setEstado("PENDIENTE");

        factura.setNumeroFactura(
                "FAC-" + lectura.getCliente().getId() + "-" + lectura.getId()
        );

        facturaRepository.save(factura);

        System.out.println("💾 FACTURA CREADA PARA LECTURA: " + lectura.getId());
    }

    @Transactional(readOnly = true)
    public Optional<Lectura> buscarPorId(Long id) {
        return lecturaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Lectura> listarConFiltros(Long clienteId, String periodo, Pageable pageable) {

        if (clienteId != null && periodo != null) {
            return lecturaRepository.findByClienteIdAndPeriodo(clienteId, periodo, pageable);
        }
        if (clienteId != null) {
            return lecturaRepository.findByClienteId(clienteId, pageable);
        }
        if (periodo != null) {
            return lecturaRepository.findByPeriodo(periodo, pageable);
        }
        return lecturaRepository.findAll(pageable);
    }

    public List<Lectura> obtenerPorCliente(Long clienteId) {
        return lecturaRepository.findByClienteId(clienteId);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!lecturaRepository.existsById(id)) {
            throw new EntityNotFoundException("No existe lectura con ID " + id);
        }
        lecturaRepository.deleteById(id);
    }
}