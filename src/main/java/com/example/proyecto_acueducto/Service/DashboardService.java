package com.example.proyecto_acueducto.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.proyecto_acueducto.Repository.FacturaRepository;
import com.example.proyecto_acueducto.Repository.PagoRepository;
import com.example.proyecto_acueducto.Repository.TurnoRepository;
import com.example.proyecto_acueducto.Repository.UsuarioRepository;

@Service
public class DashboardService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private TurnoRepository turnoRepository;

    // =====================================================
    // 📊 RESUMEN DEL DASHBOARD
    // =====================================================
    public Map<String, Object> getSummary() {

        Map<String, Object> resumen = new HashMap<>();

        try {

            // =========================
            // 👥 USUARIOS
            // =========================
            long users = usuarioRepository.count();
            resumen.put("users", users);

            // =========================
            // 💰 PAGOS HOY
            // =========================
            BigDecimal pagosHoy = pagoRepository.sumPagosHoy();
            resumen.put(
                "paymentsToday",
                pagosHoy != null ? pagosHoy : BigDecimal.ZERO
            );

            // =========================
            // 📄 FACTURAS PENDIENTES
            // =========================
            long facturasPendientes =
                    facturaRepository.countByEstado("PENDIENTE");

            resumen.put("pendingInvoices", facturasPendientes);

            // =========================
            // 📅 TURNOS MAÑANA
            // =========================
            long turnosManana = turnoRepository.countTurnoManana();
            resumen.put("tomorrowShifts", turnosManana);

        } catch (Exception e) {

            e.printStackTrace();

            resumen.put("users", 0);
            resumen.put("paymentsToday", BigDecimal.ZERO);
            resumen.put("pendingInvoices", 0);
            resumen.put("tomorrowShifts", 0);
        }

        return resumen;
    }
}