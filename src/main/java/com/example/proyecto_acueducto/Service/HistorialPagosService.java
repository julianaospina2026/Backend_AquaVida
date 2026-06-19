package com.example.proyecto_acueducto.Service;

import com.example.proyecto_acueducto.Model.Pago;
import com.example.proyecto_acueducto.Repository.PagoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialPagosService {

    private final PagoRepository pagoRepository;

    public HistorialPagosService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    // =========================
    // PAGOS POR CLIENTE
    // =========================
    public List<Pago> obtenerPorCliente(Long clienteId) {
        return pagoRepository.findByClienteId(clienteId);
    }
}
