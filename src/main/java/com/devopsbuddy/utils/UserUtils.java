package com.devopsbuddy.utils;

import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.web.controllers.ForgotMyPassordController;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

public class UserUtils {

    private UserUtils(){
        throw new AssertionError("Non instantiable");
    }

    /**
     * Creates a user with basic attributes set.
     * @param username The username.
     * @param email The password.
     * @return A User entity
     */
    public static User createBasicUser(String username, String email){
        User user = new User();
        user.setUsername(username);
        user.setPassword("secret");
        user.setEmail(email);
        user.setFirstName("First Name");
        user.setLastName("Last Name");
        user.setPhoneNumber("123456789123");
        user.setCountry("GB");
        user.setEnabled(true);
        user.setDescription("A basic user");
        user.setProfileImageUrl("https://blabla.images.com/basicuser");
        return user;
    }

    /**
     * Builds and returns the URL to reset the user password.
     * @param httpServletRequest The Http Servlet Request
     * @param userId The user id
     * @param token The token
     * @return The URL to reset the user password.
     */
    public static String createPasswordReset(HttpServletRequest httpServletRequest, long userId, String token) {
        String passwordResetUrl = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort() +
                httpServletRequest.getContextPath() + ForgotMyPassordController.CHANGE_PASSWORD_PATH + "?id=" + userId + "&token=" + token;
        return  passwordResetUrl;
    }
}
