package com.example.proyecto_acueducto.Controller;

import com.example.proyecto_acueducto.Service.ReportesService;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
public class ReportesController {
    private final ReportesService reportesService;

    public ReportesController(ReportesService reportesService) {
        this.reportesService = reportesService;
    }

    @GetMapping("/recaudo-mes")
    public Map<String, BigDecimal> obtenerRecaudoPorMes(@RequestParam String periodo) {
        return reportesService.calcularRecaudoPeriodo(periodo);
    }

    @GetMapping("/clientes-morosos")
    public Long contarClientesEnMora() {
        return reportesService.contarClientesConDeuda();
    }
    
}
