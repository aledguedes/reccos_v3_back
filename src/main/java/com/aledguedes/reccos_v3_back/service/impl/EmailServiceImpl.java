package com.aledguedes.reccos_v3_back.service.impl;

import org.springframework.stereotype.Service;

import com.aledguedes.reccos_v3_back.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendEmail(String to, String subject, String body) {
        System.out.println("Enviando e-mail para: " + to + "\nAssunto: " + subject + "\nCorpo: " + body);
    }
}