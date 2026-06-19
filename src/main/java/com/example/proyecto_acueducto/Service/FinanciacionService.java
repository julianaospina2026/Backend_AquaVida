package com.example.proyecto_acueducto.Service;

import com.example.proyecto_acueducto.Model.Financiacion;
import com.example.proyecto_acueducto.Model.CuotaFinanciacion;
import com.example.proyecto_acueducto.Repository.FinanciacionRepository;
import com.example.proyecto_acueducto.Repository.CuotaFinanciacionRepository;
import com.example.proyecto_acueducto.Repository.ClienteRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FinanciacionService {

    private final FinanciacionRepository financiacionRepository;
    private final CuotaFinanciacionRepository cuotaRepository;
    private final ClienteRepository clienteRepository;

    public FinanciacionService(
            FinanciacionRepository financiacionRepository,
            CuotaFinanciacionRepository cuotaRepository,
            ClienteRepository clienteRepository) {

        this.financiacionRepository = financiacionRepository;
        this.cuotaRepository = cuotaRepository;
        this.clienteRepository = clienteRepository;
    }

    // =========================
    // CREAR FINANCIACIÓN
    // =========================
    @Transactional
    public Financiacion crear(Financiacion f) {

        if (f.getCliente() == null || f.getCliente().getId() == null) {
            throw new IllegalArgumentException("La financiación debe estar asociada a un cliente válido.");
        }

        clienteRepository.findById(f.getCliente().getId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

        if (f.getCuotasTotales() <= 0) {
            throw new IllegalArgumentException("El número de cuotas debe ser mayor a cero.");
        }

        f.setCuotasPagadas(0);
        f.setFechaInicio(LocalDate.now());

        Financiacion guardada = financiacionRepository.save(f);

        // =========================
        // GENERAR CUOTAS AUTOMÁTICAS
        // =========================
        List<CuotaFinanciacion> cuotas = new ArrayList<>();

        for (int i = 1; i <= f.getCuotasTotales(); i++) {

            CuotaFinanciacion cuota = new CuotaFinanciacion();

            cuota.setFinanciacion(guardada);
            cuota.setNumeroCuota(i);
            cuota.setValor(f.getValorCuota());
            cuota.setFechaVencimiento(LocalDate.now().plusMonths(i));
            cuota.setEstado("PENDIENTE");

            cuotas.add(cuota);
        }

        cuotaRepository.saveAll(cuotas);

        return guardada;
    }

    // =========================
    // LISTAR POR CLIENTE
    // =========================
    public List<Financiacion> listarPorCliente(Long clienteId) {
        return financiacionRepository.findByClienteId(clienteId);
    }

    // =========================
    // OBTENER POR ID (FALTABA PARA FRONTEND)
    // =========================
    public Financiacion obtenerPorId(Long id) {
        return financiacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Financiación no encontrada"));
    }
}