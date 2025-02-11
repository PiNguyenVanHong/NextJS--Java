package com.tutorial.apidemo.interfaces;

import jakarta.mail.MessagingException;

public interface EmailService {
    String sendEmail(String to, String subject, String body) throws MessagingException;

    String sendEmailWithAttachment(String to, String subject, String body) throws MessagingException;
}
