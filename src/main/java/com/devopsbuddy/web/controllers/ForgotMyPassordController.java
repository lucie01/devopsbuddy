package com.devopsbuddy.web.controllers;

import com.devopsbuddy.backend.persistence.domain.backend.PasswordResetToken;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.service.EmailService;
import com.devopsbuddy.backend.service.PasswordResetTokenService;
import com.devopsbuddy.backend.service.UserService;
import com.devopsbuddy.utils.UserUtils;
import com.devopsbuddy.backend.service.I18NService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Locale;

@Slf4j
@Controller
public class ForgotMyPassordController {

    public static final String EMAIL_ADDRESS_VIEW_NAME = "forgotmypassword/emailForm";
    public static final String FORGOT_PASSWORD_URL_MAPPING = "/forgotmypassword";
    public static final String MAIL_SENT_KEY = "emailSent";
    public static final String CHANGE_PASSWORD_PATH = "/changeuserpassword";
    public static final String EMAIL_MESSAGE_TEXT_PROPERTY_NAME = "forgotmypassword.email.text";
    public static final String CHANGE_PASSWORD_VIEW_NAME = "forgotmypassword/changePassword";
    private static final String PASSWORD_RESET_ATTRIBUTE_NAME = "passwordReset";
    private static final String MESSAGE_ATTRIBUTE_NAME = "message";


    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private I18NService i18NService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Value("${sysadmin.email}")
    String adminEmail;

    @RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.GET)
    public String forgotPasswordGet() {
        return EMAIL_ADDRESS_VIEW_NAME;
    }

    @RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.POST)
    public String forgotPasswordPost(HttpServletRequest request, @RequestParam("email") String email, ModelMap modelMap) {
        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenForEmail(email);

        if(null == passwordResetToken) {
            log.warn("Couldn't find a password reset token for email {}", email);
        } else {
            User user = passwordResetToken.getUser();
            String token = passwordResetToken.getToken();

            String passwordResetUrl = UserUtils.createPasswordReset(request, user.getId(), token);

            String emailText = i18NService.getMessage(EMAIL_MESSAGE_TEXT_PROPERTY_NAME, request.getLocale());

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(user.getEmail());
            simpleMailMessage.setSubject("[DevOpsBuddy]: How to reset my password");
            simpleMailMessage.setText(emailText + "\r\n" + passwordResetUrl);
            simpleMailMessage.setFrom(adminEmail);

            emailService.sendGenericEmailMessage(simpleMailMessage);

            log.info("Token value: {}", passwordResetToken.getToken());
            log.info("Username {}", passwordResetToken.getUser().getUsername());
            log.info("passwordResetUrl {}", passwordResetUrl);
        }

        modelMap.addAttribute(MAIL_SENT_KEY, "true");
        return EMAIL_ADDRESS_VIEW_NAME;
    }

    @RequestMapping(value = CHANGE_PASSWORD_PATH, method = RequestMethod.GET)
    public String changeUserPasswordGet(@RequestParam("id") long id, @RequestParam("token") String token, Locale locale, ModelMap modelMap){
        if(StringUtils.isEmpty(token) || id == 0){
            log.error("Invalid userId {} or token value {}", id, token);
            modelMap.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            //Include a value in message.properties
            modelMap.addAttribute(MESSAGE_ATTRIBUTE_NAME, "Invalid user id or token value!");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token);

        if(null == passwordResetToken){
            log.error("A token couldn't to be found with value {}", token);
            modelMap.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            //Include a value in message.properties
            modelMap.addAttribute(MESSAGE_ATTRIBUTE_NAME, "Token not found!");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        User user = passwordResetToken.getUser();
        if(null == user){
            log.error("A user couldn't to be found with the token value {}", token);
            modelMap.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            //Include a value in message.properties
            modelMap.addAttribute(MESSAGE_ATTRIBUTE_NAME, "User not found for associated token!");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        if (user.getId() != id) {
            log.error("The userId {} passed as parameter doesn´t match with the userId {} associated with the token {}", id, user.getId(), token);
            modelMap.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            modelMap.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("resetpassword.token.invalid", locale));
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        if(LocalDateTime.now(Clock.systemUTC()).isAfter(passwordResetToken.getExpiryDate())){
            log.error("The token {} has expired", id, user.getId(), token);
            modelMap.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            modelMap.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("resetpassword.token.expired", locale));
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        modelMap.addAttribute("principalId", user.getId());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user,null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return CHANGE_PASSWORD_VIEW_NAME;
    }

    @RequestMapping(value = CHANGE_PASSWORD_PATH, method = RequestMethod.POST)
    public String changeUserPasswordGet(@RequestParam("principal_id") long id, @RequestParam("password") String password, ModelMap modelMap){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(null == authentication){
            log.error("An unauthenticated user tried to invoke the reset password POST method");
            modelMap.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            // Put on I18NService
            modelMap.addAttribute(MESSAGE_ATTRIBUTE_NAME, "You are not authorizated to perform  this request.");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        User user = (User) authentication.getPrincipal();
        if(user.getId() != id) {
            log.error("Security breach! User {} is trying to make a password reset request for on behalf of {}",user.getId(),id);
            modelMap.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            // Put on I18NService
            modelMap.addAttribute(MESSAGE_ATTRIBUTE_NAME, "You are not authorizated to perform  this request.");

            return CHANGE_PASSWORD_VIEW_NAME;
        }

        userService.updateUserPassword(id, password);
        log.info("Password successfully updated for user {}", user.getUsername());
        modelMap.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "true");
        // Put on I18NService
        modelMap.addAttribute(MESSAGE_ATTRIBUTE_NAME, "Your password has been successfully changed.");
        return CHANGE_PASSWORD_VIEW_NAME;
    }

}
