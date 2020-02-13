package com.devopsbuddy.test.integration;

import com.devopsbuddy.DevopsbuddyApplication;
import com.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.persistence.repositories.PasswordResetTokenRepository;
import com.devopsbuddy.backend.persistence.repositories.PlanRepository;
import com.devopsbuddy.backend.persistence.repositories.RoleRepository;
import com.devopsbuddy.backend.persistence.repositories.UserRepository;
import com.devopsbuddy.enums.PlansEnum;
import com.devopsbuddy.enums.RolesEnum;
import com.devopsbuddy.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DevopsbuddyApplication.class)
public abstract class AbstractIntegrationTest {

    @Autowired
    PlanRepository planRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    private static final int BASIC_PLAN_ID = 1;
    private static final int BASIC_ROLE_ID = 1;

    protected Plan createBasicPlan(){
        Plan plan = new Plan();
        plan.setId(BASIC_PLAN_ID);
        plan.setName("Basic");
        return plan;
    }

    protected Plan createBasicPlan(PlansEnum plansEnum){
        return new Plan(plansEnum);
    }

    protected Role createBasicRole(){
        Role role = new Role();
        role.setId(BASIC_ROLE_ID);
        role.setName("ROLE_USER");
        return role;
    }

    protected Role createBasicRole(RolesEnum rolesEnum){
        return new Role(rolesEnum);
    }

    protected User createUser(String username, String email){
        Plan basicPlan = new Plan(PlansEnum.BASIC);
        if (!planRepository.existsById(PlansEnum.BASIC.getId())) {
            basicPlan = planRepository.save(basicPlan);
        }
        User basicUser = UserUtils.createBasicUser(username, email);
        basicUser.setPlan(basicPlan);

        Role basicRole = new Role(RolesEnum.BASIC);
        if (!roleRepository.existsById(basicRole.getId())) {
            basicRole = roleRepository.save(basicRole);
        }

        Set<UserRole> userRoles = new HashSet<>();
        UserRole userRole = new UserRole(basicUser, basicRole);
        userRoles.add(userRole);

        basicUser.getUserRoles().addAll(userRoles);
        basicUser.setUserRoles(userRoles); //TODO: necessário?

        if (!userRepository.existsById(basicUser.getId())) {
            log.debug("Creating user with id {}", basicUser.getId());
            basicUser = userRepository.save(basicUser);
            log.debug("Created user with id {} successfully", basicUser.getId());
        }
        return basicUser;
    }

    protected User createUser(TestName testName){
        return createUser(testName.getMethodName(), testName.getMethodName() + "@gmail.com");
    }

}
