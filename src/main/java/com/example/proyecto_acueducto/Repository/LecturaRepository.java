package com.example.proyecto_acueducto.Repository;

import com.example.proyecto_acueducto.Model.Lectura;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface LecturaRepository extends JpaRepository<Lectura, Long> {

    // ==========================================
    // VALIDACIONES
    // ==========================================

    // Verificar si ya existe una lectura para un cliente en un periodo
    boolean existsByClienteIdAndPeriodo(Long clienteId, String periodo);

    // ==========================================
    // CONSULTAS DE LECTURAS
    // ==========================================

    // Obtener la última lectura registrada de un cliente
    Optional<Lectura> findTopByClienteIdOrderByFechaLecturaDesc(Long clienteId);

    // Obtener todas las lecturas de un cliente
    List<Lectura> findByClienteId(Long clienteId);

    // Obtener lecturas de un cliente con paginación
    Page<Lectura> findByClienteId(Long clienteId, Pageable pageable);

    // Obtener lecturas por periodo
    Page<Lectura> findByPeriodo(String periodo, Pageable pageable);

    // Obtener lecturas por cliente y periodo
    Page<Lectura> findByClienteIdAndPeriodo(
            Long clienteId,
            String periodo,
            Pageable pageable
    );

    // ==========================================
    // REPORTES
    // ==========================================

    // Sumar el consumo total de un periodo
    @Query("""
            SELECT COALESCE(SUM(l.consumoM3), 0)
            FROM Lectura l
            WHERE l.periodo = :periodo
        """)
    BigDecimal sumarConsumoPorPeriodo(@Param("periodo") String periodo);
}