package com.aledguedes.reccos_v3_back.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);

    void sendVerificationCodeEmail(String to, String code);
}