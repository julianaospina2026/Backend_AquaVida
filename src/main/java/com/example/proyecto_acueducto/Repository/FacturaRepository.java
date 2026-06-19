package com.example.proyecto_acueducto.Repository;

import com.example.proyecto_acueducto.Model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    // =========================
    // FACTURAS POR ESTADO
    // =========================
    List<Factura> findByEstado(String estado);

    // 👉 CONTEO POR ESTADO (FALTABA ESTO)
    long countByEstado(String estado);

    // =========================
    // FACTURA POR LECTURA
    // =========================
    Optional<Factura> findByLecturaId(Long lecturaId);

    // =========================
    // FACTURAS PAGADAS POR PERIODO
    // =========================
    @Query("""
        SELECT f
        FROM Factura f
        WHERE f.lectura.periodo = :periodo
        AND f.estado = 'PAGADA'
    """)
    List<Factura> findFacturasPagadasPorPeriodo(@Param("periodo") String periodo);

    // =========================
    // FACTURAS POR CLIENTE (CORREGIDO Y ROBUSTO)
    // =========================
    @Query("""
        SELECT f
        FROM Factura f
        JOIN f.lectura l
        JOIN l.cliente c
        WHERE c.id = :clienteId
    """)
    List<Factura> findByClienteId(@Param("clienteId") Long clienteId);

    // =========================
    // FACTURAS PENDIENTES POR CLIENTE (MEJORADO)
    // =========================
    @Query("""
        SELECT f
        FROM Factura f
        JOIN f.lectura l
        JOIN l.cliente c
        WHERE c.id = :clienteId
        AND TRIM(UPPER(f.estado)) = 'PENDIENTE'
    """)
    List<Factura> findPendientesPorCliente(@Param("clienteId") Long clienteId);

    // =========================
    // OPCIONAL (DEBUG ÚTIL)
    // =========================
    @Query("""
        SELECT f
        FROM Factura f
        JOIN FETCH f.lectura l
        JOIN FETCH l.cliente c
    """)
    List<Factura> findAllConCliente();
}