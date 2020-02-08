package com.devopsbuddy.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmtpEmailService extends AbstractEmailService {

    @Autowired
    private MailSender mailSender;

    @Override
    public void sendGenericEmailMessage(SimpleMailMessage simpleMailMessage) {
        log.debug("Sending email message {}", simpleMailMessage);
        mailSender.send(simpleMailMessage);
        log.info("Email sent.");
    }
}
