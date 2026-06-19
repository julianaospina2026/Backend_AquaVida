package com.example.proyecto_acueducto.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {

    @JsonProperty("identificacion")
    private String identificacion;

    @JsonProperty("password")
    private String password;

    // ========================
    // CONSTRUCTOR VACÍO (OBLIGATORIO)
    // ========================
    public LoginRequest() {
    }

    // ========================
    // CONSTRUCTOR COMPLETO
    // ========================
    public LoginRequest(String identificacion, String password) {
        this.identificacion = identificacion;
        this.password = password;
    }

    // ========================
    // GETTERS Y SETTERS
    // ========================
    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ========================
    // TO STRING (AYUDA EN DEBUG)
    // ========================
    @Override
    public String toString() {
        return "LoginRequest{" +
                "identificacion='" + identificacion + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
}