package com.devopsbuddy.backend.service;

import com.devopsbuddy.web.domain.FeedBack;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    /**
     * Sends an email with the content in the FeedBack POJO
     * @param feedBack The FeedBack POJO
     */
    public void sendFeedBackEmail(FeedBack feedBack);

    /**
     * Sends an email with the content of the simple Mail Message object.
     * @param simpleMailMessage The object containing the email content
     */
    public void sendGenericEmailMessage(SimpleMailMessage simpleMailMessage);
}
