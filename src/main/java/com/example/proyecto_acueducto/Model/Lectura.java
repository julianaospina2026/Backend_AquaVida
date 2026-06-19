package com.example.proyecto_acueducto.Model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lecturas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Lectura {

    // =========================
    // ID
    // =========================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // RELACIÓN CLIENTE
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnoreProperties({"lecturas", "hibernateLazyInitializer", "handler"})
    private Cliente cliente;

    // =========================
    // DATOS DE LECTURA
    // =========================
    @Column(name = "periodo")
    private String periodo;

    @Column(name = "fecha_lectura")
    private LocalDate fechaLectura;

    @Column(name = "lectura_anterior", precision = 12, scale = 3)
    private BigDecimal lecturaAnterior;

    @Column(name = "lectura_actual", precision = 12, scale = 3)
    private BigDecimal lecturaActual;

    @Column(name = "consumo_m3", precision = 12, scale = 3)
    private BigDecimal consumoM3;

    @Column(name = "observacion")
    private String observacion;

    @Column(name = "valor", precision = 12, scale = 2)
    private BigDecimal valor = BigDecimal.ZERO;

    // =========================
    // AUDITORÍA
    // =========================
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // =========================
    // CICLO DE VIDA
    // =========================
    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();

        if (fechaLectura == null) {
            fechaLectura = LocalDate.now();
        }

        // consumo
        if (lecturaActual != null && lecturaAnterior != null) {
            consumoM3 = lecturaActual.subtract(lecturaAnterior);
        }

        // valor (ejemplo tarifa fija)
        if (consumoM3 != null) {
            valor = consumoM3.multiply(new BigDecimal("2000"));
        }
    }

    // =========================
    // GETTERS Y SETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public LocalDate getFechaLectura() {
        return fechaLectura;
    }

    public void setFechaLectura(LocalDate fechaLectura) {
        this.fechaLectura = fechaLectura;
    }

    public BigDecimal getLecturaAnterior() {
        return lecturaAnterior;
    }

    public void setLecturaAnterior(BigDecimal lecturaAnterior) {
        this.lecturaAnterior = lecturaAnterior;
    }

    public BigDecimal getLecturaActual() {
        return lecturaActual;
    }

    public void setLecturaActual(BigDecimal lecturaActual) {
        this.lecturaActual = lecturaActual;
    }

    public BigDecimal getConsumoM3() {
        return consumoM3;
    }

    public void setConsumoM3(BigDecimal consumoM3) {
        this.consumoM3 = consumoM3;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}