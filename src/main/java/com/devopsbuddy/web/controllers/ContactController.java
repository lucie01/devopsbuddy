package com.devopsbuddy.web.controllers;

import com.devopsbuddy.backend.service.EmailService;
import com.devopsbuddy.web.domain.FeedBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
public class ContactController {

    private static final String FEEDBACK_MODEL_KEY = "feedback";
    private static final String CONTACT_US_VIEW_NAME = "contact/contact";

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String contactGet(ModelMap modelMap){
        FeedBack feedBack = new FeedBack();
        modelMap.addAttribute(ContactController.FEEDBACK_MODEL_KEY, feedBack);
        return ContactController.CONTACT_US_VIEW_NAME;
    }

    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public String contactPost(@ModelAttribute(FEEDBACK_MODEL_KEY) FeedBack feedBack){
        log.debug("FeedBack POJO content {}", feedBack);
        emailService.sendFeedBackEmail(feedBack);
        return ContactController.CONTACT_US_VIEW_NAME;
    }
}
