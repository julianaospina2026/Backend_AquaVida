package com.example.proyecto_acueducto.Service;

import com.example.proyecto_acueducto.Repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private PagoRepository pagoRepository;

    // =====================================================
    // 📊 RESUMEN DEL DASHBOARD
    // =====================================================
    public Map<String, Object> getSummary() {

        Map<String, Object> resumen = new HashMap<>();

        try {

            // =========================
            // 👥 USUARIOS (placeholder si no tienes repo)
            // =========================
            resumen.put("users", 0);

            // =========================
            // 💰 PAGOS HOY
            // =========================
            BigDecimal pagosHoy = pagoRepository.sumPagosHoy();
            resumen.put(
                "paymentsToday",
                pagosHoy != null ? pagosHoy : BigDecimal.ZERO
            );

            // =========================
            // 📄 FACTURAS PENDIENTES (placeholder)
            // =========================
            resumen.put("pendingInvoices", 0);

            // =========================
            // 📅 TURNOS MAÑANA (placeholder)
            // =========================
            resumen.put("tomorrowShifts", 0);

        } catch (Exception e) {

            // 🔥 IMPORTANTE: evita que el backend reviente (500)
            e.printStackTrace();

            resumen.put("users", 0);
            resumen.put("paymentsToday", 0);
            resumen.put("pendingInvoices", 0);
            resumen.put("tomorrowShifts", 0);
        }

        return resumen;
    }
}