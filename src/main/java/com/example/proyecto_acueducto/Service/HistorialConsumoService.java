package com.example.proyecto_acueducto.Service;

import com.example.proyecto_acueducto.Model.Lectura;
import com.example.proyecto_acueducto.Repository.LecturaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialConsumoService {

    private final LecturaRepository lecturaRepository;

    public HistorialConsumoService(LecturaRepository lecturaRepository) {
        this.lecturaRepository = lecturaRepository;
    }

    // =========================
    // POR CLIENTE (LISTA SIMPLE)
    // =========================
    public List<Lectura> obtenerPorCliente(Long clienteId) {
        return lecturaRepository.findByClienteId(clienteId);
    }

    // =========================
    // POR CLIENTE + PERIODO
    // =========================
    public List<Lectura> obtenerPorClienteYPeriodo(Long clienteId, String periodo) {
        return lecturaRepository.findByClienteIdAndPeriodo(clienteId, periodo);
    }

    // =========================
    // POR PERIODO
    // =========================
    public List<Lectura> obtenerPorPeriodo(String periodo) {
        return lecturaRepository.findByPeriodo(periodo);
    }
}