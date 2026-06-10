package com.example.proyecto_acueducto.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "lecturas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Lectura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔵 RELACIÓN CON CLIENTE
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnoreProperties({
            "hibernateLazyInitializer",
            "handler",
            "lecturas"
    })
    private Cliente cliente;

    // 🔵 PERIODO (Ej: 2026-04)
    @Column(nullable = false, length = 7)
    private String periodo;

    // 🔵 FECHA DE LECTURA
    @Column(name = "fecha_lectura", nullable = false)
    private LocalDate fechaLectura;

    // 🔵 LECTURA ANTERIOR
    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal lecturaAnterior;

    // 🔵 LECTURA ACTUAL
    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal lecturaActual;

    // 🔵 CONSUMO EN M3
    @Column(precision = 12, scale = 3)
    private BigDecimal consumoM3;

    // 🔵 OBSERVACIÓN
    @Column(length = 255)
    private String observacion;

    // 🔵 VALOR DE LA LECTURA
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor = BigDecimal.ZERO;

    // 🔵 AUDITORÍA
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // =========================
    // MÉTODOS DE CICLO DE VIDA
    // =========================

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();

        if (fechaLectura == null) {
            fechaLectura = LocalDate.now();
        }

        calcularConsumo();
    }

    @PreUpdate
    protected void onUpdate() {
        calcularConsumo();
    }

    // =========================
    // CÁLCULO DE CONSUMO
    // =========================

    private void calcularConsumo() {

        if (lecturaActual != null && lecturaAnterior != null) {
            consumoM3 = lecturaActual.subtract(lecturaAnterior);
        } else {
            consumoM3 = BigDecimal.ZERO;
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
}