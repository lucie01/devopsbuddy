package com.devopsbuddy.backend.service;


import com.devopsbuddy.web.domain.FeedBack;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

public abstract class AbstractEmailService implements EmailService{

    @Value(value = "${default.to.address}")
    private String defaultToAddress;
    /**
     * Creates a simple Mail Message from a FeedBack POJO
     * @param feedBack The FeedBack POJO
     * @return
     */
    protected SimpleMailMessage prepareSimpleMailMessageFromFeedback(FeedBack feedBack){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(defaultToAddress);
        simpleMailMessage.setFrom(feedBack.getEmail());
        simpleMailMessage.setSubject("[DevOps Buddy]: Feedback received from " + feedBack.getFirstName() + " " + feedBack.getLastName() + "!");
        simpleMailMessage.setText(feedBack.getFeedBack());
        return simpleMailMessage;
    }

    @Override
    public void sendFeedBackEmail(FeedBack feedBack) {
        sendGenericEmailMessage(prepareSimpleMailMessageFromFeedback(feedBack));
    }
}
