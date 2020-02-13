package com.devopsbuddy.test.integration;

import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.repositories.UserRepository;
import com.devopsbuddy.backend.service.UserService;
import com.devopsbuddy.enums.PlansEnum;
import com.devopsbuddy.enums.RolesEnum;
import com.devopsbuddy.utils.UserUtils;
import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractServiceIntegrationTest {

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserRepository userRepository;

    protected User createUser(TestName testName) {
        String username = testName.getClass().getName() + testName.getMethodName();
        String email = testName.getClass().getName() + testName.getMethodName() + "@gmail.com";

        User basicUser = UserUtils.createBasicUser(username, email);
        return userService.createUser(basicUser, PlansEnum.BASIC, RolesEnum.BASIC);
    }
}
