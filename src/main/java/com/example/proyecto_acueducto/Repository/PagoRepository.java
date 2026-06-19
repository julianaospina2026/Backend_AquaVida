package com.example.proyecto_acueducto.Repository;

import com.example.proyecto_acueducto.Model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    // =====================================================
    // 🔹 PAGOS POR FACTURA (SIN CAMBIOS)
    // =====================================================
    List<Pago> findByFacturaId(Long facturaId);

    // =====================================================
    // 🔥 HISTORIAL POR CLIENTE (CORREGIDO - EVITA 500)
    // =====================================================
    @Query("""
        SELECT p
        FROM Pago p
        JOIN FETCH p.factura f
        JOIN FETCH f.lectura l
        JOIN FETCH l.cliente c
        WHERE c.id = :clienteId
    """)
    List<Pago> findByClienteId(@Param("clienteId") Long clienteId);

    // =====================================================
    // 📊 TOTAL PAGOS HOY
    // =====================================================
    @Query("""
        SELECT COALESCE(SUM(p.monto), 0)
        FROM Pago p
        WHERE p.fechaPago >= CURRENT_DATE
        AND p.fechaPago < CURRENT_TIMESTAMP
    """)
    BigDecimal sumPagosHoy();

    // =====================================================
    // 🔹 ADMIN: TODOS LOS PAGOS ORDENADOS
    // =====================================================
    List<Pago> findAllByOrderByFechaPagoDesc();
}
