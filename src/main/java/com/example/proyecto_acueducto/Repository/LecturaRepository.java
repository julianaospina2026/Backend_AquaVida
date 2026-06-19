package com.example.proyecto_acueducto.Repository;

import com.example.proyecto_acueducto.Model.Lectura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LecturaRepository extends JpaRepository<Lectura, Long> {

    // =========================
    // CLIENTE
    // =========================
    List<Lectura> findByClienteId(Long clienteId);

    Page<Lectura> findByClienteId(Long clienteId, Pageable pageable);

    // =========================
    // CLIENTE + PERIODO
    // =========================
    List<Lectura> findByClienteIdAndPeriodo(Long clienteId, String periodo);

    Page<Lectura> findByClienteIdAndPeriodo(Long clienteId, String periodo, Pageable pageable);

    // =========================
    // VALIDACIÓN DUPLICADOS
    // =========================
    boolean existsByClienteIdAndPeriodo(Long clienteId, String periodo);

    // =========================
    // PERIODO
    // =========================
    List<Lectura> findByPeriodo(String periodo);

    Page<Lectura> findByPeriodo(String periodo, Pageable pageable);

    // =========================
    // 🔥 ÚLTIMA LECTURA (CORREGIDO Y CONSISTENTE)
    // =========================

    /**
     * Mejor práctica: usar ID porque es incremental y confiable
     */
    Optional<Lectura> findTopByClienteIdOrderByIdDesc(Long clienteId);
}