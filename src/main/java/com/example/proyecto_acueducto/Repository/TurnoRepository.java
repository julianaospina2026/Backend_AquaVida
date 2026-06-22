package com.example.proyecto_acueducto.Repository;

import com.example.proyecto_acueducto.Model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    // =========================
    // 📌 Turnos por estado
    // =========================
    List<Turno> findByEstado(String estado);

    // =========================
    // 👤 Turnos por cliente
    // =========================
    List<Turno> findByClienteId(Long clienteId);

    // =========================
    // 📅 Turnos de mañana (CORRECTO Y COMPATIBLE)
    // =========================
    @Query(
        value = "SELECT COUNT(*) " +
                "FROM turnos_mantenimiento " +
                "WHERE DATE(fecha_hora_programada) = DATE_ADD(CURDATE(), INTERVAL 1 DAY)",
        nativeQuery = true
    )
    long countTurnoManana();
}