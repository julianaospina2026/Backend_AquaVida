package com.example.proyecto_acueducto.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios_sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // LOGIN
    // =========================
    @Column(nullable = false, unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String cedula;

    @Column(nullable = false)
    private String password;

    // =========================
    // INFORMACIÓN PERSONAL
    // =========================
    @Column(nullable = false)
    private String nombreCompleto;

    // 🔥 NUEVO: TELÉFONO
    @Column
    private String telefono;

    // =========================
    // ROL
    // =========================
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "rol_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USUARIO_ROL")
    )
    @JsonIgnoreProperties("usuarios")
    private Rol rol;

    // =========================
    // ESTADO
    // =========================
    @Column(nullable = false)
    private Boolean activo = true;

    // =========================
    // RELACIÓN CON CLIENTE 🔥
    // =========================
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "cliente_id")
    @JsonIgnoreProperties({"lecturas"})
    private Cliente cliente;

    // =========================
    // GETTERS Y SETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    // 🔥 TELÉFONO
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    // 🔥 CLIENTE
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}