package com.example.proyecto_acueducto.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarRecibo(String destino, byte[] pdf, String nombreArchivo) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(destino);
        helper.setSubject("Comprobante de pago - Acueducto");
        helper.setText("Adjuntamos su comprobante de pago en PDF.");

        helper.addAttachment(nombreArchivo, new ByteArrayResource(pdf));

        mailSender.send(message);
    }
}
