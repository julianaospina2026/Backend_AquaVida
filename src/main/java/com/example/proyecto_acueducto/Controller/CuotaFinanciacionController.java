package com.example.proyecto_acueducto.Controller;

import com.example.proyecto_acueducto.Model.CuotaFinanciacion;
import com.example.proyecto_acueducto.Repository.CuotaFinanciacionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuotas")
public class CuotaFinanciacionController {

    private final CuotaFinanciacionRepository cuotaRepository;

    public CuotaFinanciacionController(CuotaFinanciacionRepository cuotaRepository) {
        this.cuotaRepository = cuotaRepository;
    }

    // =====================================
    // OBTENER CUOTAS POR FINANCIACIÓN
    // =====================================
    @GetMapping("/financiacion/{id}")
    public List<CuotaFinanciacion> obtenerPorFinanciacion(@PathVariable Long id) {
        return cuotaRepository.findByFinanciacionId(id);
    }

    // =====================================
    // OBTENER CUOTAS POR ESTADO
    // (PENDIENTE / PAGADA / ATRASADA)
    // =====================================
    @GetMapping("/estado/{estado}")
    public List<CuotaFinanciacion> obtenerPorEstado(@PathVariable String estado) {
        return cuotaRepository.findByEstado(estado);
    }
}