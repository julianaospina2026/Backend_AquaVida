package com.example.proyecto_acueducto.Controller;

import com.example.proyecto_acueducto.Model.Medidor;
import com.example.proyecto_acueducto.Service.MedidorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/medidores")
public class MedidorController {
    private final MedidorService medidorService;

    public MedidorController(MedidorService medidorService) {
        this.medidorService = medidorService;
    }

    @GetMapping
    public List<Medidor> listar() {
        return medidorService.listarTodos();
    }

    @PostMapping
    public Medidor guardar(@RequestBody Medidor medidor) {
        return medidorService.guardar(medidor);
    }

    @GetMapping("/{serial}")
    public ResponseEntity<Medidor> obtenerPorSerial(@PathVariable String serial) {
        return medidorService.buscarPorSerial(serial)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
}
