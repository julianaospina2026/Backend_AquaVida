package com.example.proyecto_acueducto.Controller;

import com.example.proyecto_acueducto.Service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // ================================
    // RESUMEN DASHBOARD
    // ================================
    @GetMapping("/summary")
    public Map<String, Object> obtenerResumen() {
        return dashboardService.getSummary();
    }
}