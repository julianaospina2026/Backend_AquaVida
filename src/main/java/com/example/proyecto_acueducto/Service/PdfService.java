package com.example.proyecto_acueducto.Service;

import com.example.proyecto_acueducto.Dto.ReciboDTO;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

public byte[] generarReciboPdf(ReciboDTO recibo) {

    try {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Document document = new Document();

        PdfWriter.getInstance(document, output);

        document.open();

        document.add(new Paragraph("MANANTIAL DE VIDA"));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("RECIBO DE PAGO"));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Pago ID: " + recibo.getIdPago()));
        document.add(new Paragraph("Factura: " + recibo.getNumeroFactura()));
        document.add(new Paragraph("Cliente: " + recibo.getClienteNombre()));
        document.add(new Paragraph("Monto: $" + recibo.getMonto()));
        document.add(new Paragraph("Método: " + recibo.getMetodoPago()));
        document.add(new Paragraph("Fecha: " + recibo.getFechaPago()));

        document.close();

        return output.toByteArray();

    } catch (Exception e) {

        throw new RuntimeException("Error generando PDF", e);
    }
}

}
