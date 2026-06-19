package com.example.proyecto_acueducto.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // RELACIÓN CON CLIENTE
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cliente cliente;

    // =========================
    // RELACIÓN CON LECTURA
    // =========================
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lectura_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Lectura lectura;

    // =========================
    // DATOS FACTURA
    // =========================
    @Column(name = "numero_factura", unique = true, nullable = false)
    private String numeroFactura;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "valor_consumo", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorConsumo;

    @Column(name = "cargo_fijo", nullable = false, precision = 12, scale = 2)
    private BigDecimal cargoFijo = BigDecimal.ZERO;

    @Column(name = "otros_cobros", precision = 12, scale = 2)
    private BigDecimal otrosCobros = BigDecimal.ZERO;

    @Column(name = "total_pagar", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPagar;

    @Column(name = "estado", nullable = false)
    private String estado; // PENDIENTE, PAGADA, ANULADA

    // =========================
    // MÉTODO DE CÁLCULO
    // =========================
    public void calcularTotal() {
        BigDecimal total = valorConsumo != null ? valorConsumo : BigDecimal.ZERO;

        if (cargoFijo != null) {
            total = total.add(cargoFijo);
        }

        if (otrosCobros != null) {
            total = total.add(otrosCobros);
        }

        this.totalPagar = total;
    }

    // =========================
    // GETTERS Y SETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Lectura getLectura() {
        return lectura;
    }

    public void setLectura(Lectura lectura) {
        this.lectura = lectura;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public BigDecimal getValorConsumo() {
        return valorConsumo;
    }

    public void setValorConsumo(BigDecimal valorConsumo) {
        this.valorConsumo = valorConsumo;
    }

    public BigDecimal getCargoFijo() {
        return cargoFijo;
    }

    public void setCargoFijo(BigDecimal cargoFijo) {
        this.cargoFijo = cargoFijo;
    }

    public BigDecimal getOtrosCobros() {
        return otrosCobros;
    }

    public void setOtrosCobros(BigDecimal otrosCobros) {
        this.otrosCobros = otrosCobros;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
