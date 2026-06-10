package com.example.proyecto_acueducto.Service;

import com.example.proyecto_acueducto.Model.Cliente;
import com.example.proyecto_acueducto.Model.Lectura;
import com.example.proyecto_acueducto.Repository.ClienteRepository;
import com.example.proyecto_acueducto.Repository.LecturaRepository;

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

    private static final BigDecimal CONSUMO_ALTO = new BigDecimal("80");
    private static final BigDecimal PRECIO_M3 = new BigDecimal("2500");

    public LecturaService(LecturaRepository lecturaRepository,
                          ClienteRepository clienteRepository) {
        this.lecturaRepository = lecturaRepository;
        this.clienteRepository = clienteRepository;
    }

    // =========================
    // GUARDAR LECTURA
    // =========================
    @Transactional
    public Lectura guardar(Lectura lectura) {

        validarDatos(lectura);

        Cliente cliente = obtenerCliente(lectura.getCliente().getId());

        validarDuplicado(lectura, cliente.getId());

        BigDecimal lecturaAnterior = obtenerUltimaLectura(cliente.getId());

        return construirYGuardar(lectura, cliente, lecturaAnterior);
    }

    // =========================
    // VALIDACIONES
    // =========================
    private void validarDatos(Lectura lectura) {

        if (lectura.getCliente() == null || lectura.getCliente().getId() == null) {
            throw new IllegalArgumentException("Debe enviar el id del cliente");
        }

        if (lectura.getLecturaActual() == null) {
            throw new IllegalArgumentException("Debe enviar la lectura actual");
        }

        if (lectura.getPeriodo() == null || lectura.getPeriodo().isBlank()) {
            throw new IllegalArgumentException("Debe enviar el periodo");
        }
    }

    // =========================
    // OBTENER CLIENTE
    // =========================
    private Cliente obtenerCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Cliente no encontrado con ID " + id));
    }

    // =========================
    // VALIDAR DUPLICADO
    // =========================
    private void validarDuplicado(Lectura lectura, Long clienteId) {
        if (lecturaRepository.existsByClienteIdAndPeriodo(clienteId, lectura.getPeriodo())) {
            throw new IllegalStateException(
                    "Ya existe lectura para el cliente en el periodo " + lectura.getPeriodo()
            );
        }
    }

    // =========================
    // ÚLTIMA LECTURA
    // =========================
    private BigDecimal obtenerUltimaLectura(Long clienteId) {
        return lecturaRepository
                .findTopByClienteIdOrderByFechaLecturaDesc(clienteId)
                .map(Lectura::getLecturaActual)
                .orElse(BigDecimal.ZERO);
    }

    // =========================
    // CONSTRUIR Y GUARDAR
    // =========================
    private Lectura construirYGuardar(Lectura lectura,
                                      Cliente cliente,
                                      BigDecimal lecturaAnterior) {

        BigDecimal lecturaActual = lectura.getLecturaActual()
                .setScale(3, RoundingMode.HALF_UP);

        if (lecturaActual.compareTo(lecturaAnterior) < 0) {
            throw new IllegalArgumentException(
                    "La lectura actual no puede ser menor a la anterior"
            );
        }

        BigDecimal consumo = lecturaActual.subtract(lecturaAnterior)
                .setScale(3, RoundingMode.HALF_UP);

        lectura.setCliente(cliente);
        lectura.setLecturaAnterior(lecturaAnterior);
        lectura.setLecturaActual(lecturaActual);
        lectura.setValor(consumo.multiply(PRECIO_M3));

        if (lectura.getFechaLectura() == null) {
            lectura.setFechaLectura(LocalDate.now());
        }

        // Observación
        if (consumo.compareTo(BigDecimal.ZERO) == 0) {
            lectura.setObservacion("Sin consumo");
        } else if (consumo.compareTo(CONSUMO_ALTO) > 0) {
            lectura.setObservacion("Consumo alto");
        } else {
            lectura.setObservacion("Consumo normal");
        }

        return lecturaRepository.save(lectura);
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    @Transactional(readOnly = true)
    public Optional<Lectura> buscarPorId(Long id) {
        return lecturaRepository.findById(id);
    }

    // =========================
    // LISTAR CON FILTROS
    // =========================
    @Transactional(readOnly = true)
    public Page<Lectura> listarConFiltros(Long clienteId, String periodo, Pageable pageable) {

        if (clienteId != null && periodo != null && !periodo.isBlank()) {
            return lecturaRepository.findByClienteIdAndPeriodo(clienteId, periodo, pageable);
        }

        if (clienteId != null) {
            return lecturaRepository.findByClienteId(clienteId, pageable);
        }

        if (periodo != null && !periodo.isBlank()) {
            return lecturaRepository.findByPeriodo(periodo, pageable);
        }

        return lecturaRepository.findAll(pageable);
    }

    // =========================
    // 🔥 LISTAR POR CLIENTE (CORREGIDO)
    // =========================
    @Transactional(readOnly = true)
    public List<Lectura> obtenerPorCliente(Long clienteId) {
        return lecturaRepository.findByClienteId(clienteId);
    }

    // =========================
    // ELIMINAR
    // =========================
    @Transactional
    public void eliminar(Long id) {

        if (!lecturaRepository.existsById(id)) {
            throw new EntityNotFoundException("Lectura no existe con ID " + id);
        }

        lecturaRepository.deleteById(id);
    }
}