package com.example.proyecto_acueducto.Dto;

public class LoginResponse {

    private String role;
    private String redirectUrl;
    private String message;
    private String token;
    private Long clienteId;
    private String nombreCompleto;
    private String email;
    private String cedula;

    public LoginResponse() {
    }

    public LoginResponse(
            String role,
            String redirectUrl,
            String message) {

        this.role = role;
        this.redirectUrl = redirectUrl;
        this.message = message;
    }

    public LoginResponse(
            String role,
            String redirectUrl,
            String message,
            String token) {

        this.role = role;
        this.redirectUrl = redirectUrl;
        this.message = message;
        this.token = token;
    }

    public LoginResponse(
            String role,
            String redirectUrl,
            String message,
            String token,
            Long clienteId) {

        this.role = role;
        this.redirectUrl = redirectUrl;
        this.message = message;
        this.token = token;
        this.clienteId = clienteId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getNombreCompleto() {
    return nombreCompleto;
}

public void setNombreCompleto(String nombreCompleto) {
    this.nombreCompleto = nombreCompleto;
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
}