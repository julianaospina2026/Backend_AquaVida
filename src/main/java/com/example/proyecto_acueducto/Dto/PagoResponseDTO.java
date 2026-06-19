package com.example.proyecto_acueducto.Dto;

public class PagoResponseDTO {

    // Este debe ser el ID que usarás para PDF y correo (FACTURA)
    private Long facturaId;

    private String mensaje;

    // Constructor vacío
    public PagoResponseDTO() {
    }

    // Constructor con campos
    public PagoResponseDTO(Long facturaId, String mensaje) {
        this.facturaId = facturaId;
        this.mensaje = mensaje;
    }

    // Getter y Setter
    public Long getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(Long facturaId) {
        this.facturaId = facturaId;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
