package com.example.proyecto_acueducto.Dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReciboDTO {

private Long idPago;
private Long facturaId;
private String numeroFactura;

private BigDecimal monto;
private String metodoPago;
private LocalDateTime fechaPago;

private String clienteNombre;

public Long getIdPago() {
    return idPago;
}

public void setIdPago(Long idPago) {
    this.idPago = idPago;
}

public Long getFacturaId() {
    return facturaId;
}

public void setFacturaId(Long facturaId) {
    this.facturaId = facturaId;
}

public String getNumeroFactura() {
    return numeroFactura;
}

public void setNumeroFactura(String numeroFactura) {
    this.numeroFactura = numeroFactura;
}

public BigDecimal getMonto() {
    return monto;
}

public void setMonto(BigDecimal monto) {
    this.monto = monto;
}

public String getMetodoPago() {
    return metodoPago;
}

public void setMetodoPago(String metodoPago) {
    this.metodoPago = metodoPago;
}

public LocalDateTime getFechaPago() {
    return fechaPago;
}

public void setFechaPago(LocalDateTime fechaPago) {
    this.fechaPago = fechaPago;
}

public String getClienteNombre() {
    return clienteNombre;
}

public void setClienteNombre(String clienteNombre) {
    this.clienteNombre = clienteNombre;
}

}
