package com.example.proyecto_acueducto.Controller;

import com.example.proyecto_acueducto.Model.Lectura;
import com.example.proyecto_acueducto.Service.LecturaService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/lecturas")
public class LecturaController {

    private final LecturaService lecturaService;

    public LecturaController(LecturaService lecturaService) {
        this.lecturaService = lecturaService;
    }

    // ==========================
    // OBTENER POR CLIENTE
    // ==========================
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Lectura>> obtenerPorCliente(
            @PathVariable Long clienteId) {

        return ResponseEntity.ok(
                lecturaService.obtenerPorCliente(clienteId)
        );
    }

    // ==========================
    // REGISTRAR LECTURA
    // ==========================
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Lectura lectura) {

        try {

            Lectura nuevaLectura = lecturaService.guardar(lectura);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(nuevaLectura);

        } catch (IllegalArgumentException e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (IllegalStateException e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            "ERROR: "
                                    + e.getClass().getName()
                                    + " -> "
                                    + e.getMessage()
                    );
        }
    }

    // ==========================
    // OBTENER POR ID
    // ==========================
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(
            @PathVariable Long id) {

        return lecturaService.buscarPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("Lectura no encontrada")
                );
    }

    // ==========================
    // LISTAR
    // ==========================
    @GetMapping
    public ResponseEntity<Page<Lectura>> listar(

            @RequestParam(required = false)
            Long clienteId,

            @RequestParam(required = false)
            String periodo,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "fechaLectura")
            String sort,

            @RequestParam(defaultValue = "desc")
            String dir
    ) {

        Sort.Direction direction =
                dir.equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(direction, sort)
                );

        return ResponseEntity.ok(
                lecturaService.listarConFiltros(
                        clienteId,
                        periodo,
                        pageable
                )
        );
    }

    // ==========================
    // ELIMINAR
    // ==========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @PathVariable Long id) {

        try {

            lecturaService.eliminar(id);

            return ResponseEntity.ok(
                    "Lectura eliminada correctamente"
            );

        } catch (EntityNotFoundException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            "ERROR: "
                                    + e.getClass().getName()
                                    + " -> "
                                    + e.getMessage()
                    );
        }
    }
}