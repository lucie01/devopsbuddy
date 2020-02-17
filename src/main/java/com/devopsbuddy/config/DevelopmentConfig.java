package com.devopsbuddy.config;

import com.devopsbuddy.backend.service.EmailService;
import com.devopsbuddy.backend.service.MockEmailService;
import com.devopsbuddy.backend.service.SmtpEmailService;
import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("dev")
@PropertySource("file:///c:/lucie/spring_aws/application-dev.properties")
public class DevelopmentConfig {

    @Bean
    public EmailService emailService(){
        //return new MockEmailService();
        return new SmtpEmailService();
    }

    @Bean
    public ServletRegistrationBean h2ConsoleServletRegistration(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new WebServlet());
        bean.addUrlMappings("/console/*");
        return bean;
    }
}
