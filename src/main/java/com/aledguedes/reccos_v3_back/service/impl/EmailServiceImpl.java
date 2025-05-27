package com.aledguedes.reccos_v3_back.service.impl;

import com.aledguedes.reccos_v3_back.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final String registrationUrl;

    public EmailServiceImpl(JavaMailSender mailSender,
            SpringTemplateEngine templateEngine,
            @Value("${reccos.registration.url}") String registrationUrl) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.registrationUrl = registrationUrl;
    }

    @Override
    @Async
    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(mimeMessage);
            logger.info("E-mail enviado para: {}", to);
        } catch (MessagingException e) {
            logger.error("Falha ao enviar e-mail para {}: {}", to, e.getMessage(), e);
        }
    }

    @Override
    @Async
    public void sendVerificationCodeEmail(String to, String code) {
        Context context = new Context();
        context.setVariable("code", code);
        context.setVariable("registrationUrl", registrationUrl);

        String emailContent = templateEngine.process("verification-email", context);

        sendEmail(to, "Seu Código de Verificação - Reccos", emailContent);
    }
}