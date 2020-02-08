package com.devopsbuddy.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MockEmailService extends AbstractEmailService {

    @Override
    public void sendGenericEmailMessage(SimpleMailMessage simpleMailMessage) {
        log.debug("Simulating an email service...");
        log.info(simpleMailMessage.toString());
        log.debug("Email sent.");
    }
}
