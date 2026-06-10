package com.example.proyecto_acueducto.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "financiaciones")
public class Financiacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔵 RELACIÓN CON CLIENTE
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cliente cliente;

    @Column(name = "monto_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoTotal;

    @Column(name = "cuotas_totales", nullable = false)
    private Integer cuotasTotales;

    @Column(name = "cuotas_pagadas", nullable = false)
    private Integer cuotasPagadas;

    @Column(name = "valor_cuota", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorCuota;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private String concepto;

    // =========================
    // GETTERS Y SETTERS
    // =========================

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }

    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public BigDecimal getMontoTotal() { return montoTotal; }

    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }

    public Integer getCuotasTotales() { return cuotasTotales; }

    public void setCuotasTotales(Integer cuotasTotales) { this.cuotasTotales = cuotasTotales; }

    public Integer getCuotasPagadas() { return cuotasPagadas; }

    public void setCuotasPagadas(Integer cuotasPagadas) { this.cuotasPagadas = cuotasPagadas; }

    public BigDecimal getValorCuota() { return valorCuota; }

    public void setValorCuota(BigDecimal valorCuota) { this.valorCuota = valorCuota; }

    public LocalDate getFechaInicio() { return fechaInicio; }

    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getConcepto() { return concepto; }

    public void setConcepto(String concepto) { this.concepto = concepto; }
}
