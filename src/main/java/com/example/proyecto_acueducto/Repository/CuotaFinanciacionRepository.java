package com.example.proyecto_acueducto.Repository;

import com.example.proyecto_acueducto.Model.CuotaFinanciacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuotaFinanciacionRepository extends JpaRepository<CuotaFinanciacion, Long> {

    // =====================================
    // Obtener todas las cuotas de una financiación
    // =====================================
    List<CuotaFinanciacion> findByFinanciacionId(Long financiacionId);

    // =====================================
    // Obtener cuotas por estado
    // (PENDIENTE, PAGADA, ATRASADA)
    // =====================================
    List<CuotaFinanciacion> findByEstado(String estado);
}
