package com.example.proyecto_acueducto.Service;

import com.example.proyecto_acueducto.Model.Lectura;
import com.example.proyecto_acueducto.Repository.LecturaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialLecturasService {

    private final LecturaRepository lecturaRepository;

    public HistorialLecturasService(LecturaRepository lecturaRepository) {
        this.lecturaRepository = lecturaRepository;
    }

    // =========================
    // POR CLIENTE (LISTA SIMPLE)
    // =========================
    public List<Lectura> obtenerPorCliente(Long clienteId) {
        return lecturaRepository.findByClienteId(clienteId);
    }

    // =========================
    // POR CLIENTE (PAGINADO)
    // =========================
    public List<Lectura> obtenerPorClientePaginado(Long clienteId, Pageable pageable) {
        return lecturaRepository
                .findByClienteId(clienteId, pageable)
                .getContent();
    }
}