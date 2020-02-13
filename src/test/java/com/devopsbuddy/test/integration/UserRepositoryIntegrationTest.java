package com.devopsbuddy.test.integration;

import com.devopsbuddy.DevopsbuddyApplication;
import com.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.persistence.repositories.PlanRepository;
import com.devopsbuddy.backend.persistence.repositories.RoleRepository;
import com.devopsbuddy.backend.persistence.repositories.UserRepository;
import com.devopsbuddy.enums.PlansEnum;
import com.devopsbuddy.enums.RolesEnum;
import com.devopsbuddy.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DevopsbuddyApplication.class)
public class UserRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Rule
    public TestName testName = new TestName();
    @Before
    public void init(){
        Assert.assertNotNull(planRepository);
        Assert.assertNotNull(roleRepository);
        Assert.assertNotNull(userRepository);
    }

    @Test
    public void testCreateNewPlan() throws Exception {
        Plan basicPlan = super.createBasicPlan();
        planRepository.save(basicPlan);
        Optional<Plan> retrievedPlan = planRepository.findById(basicPlan.getId());
        Assert.assertNotNull(retrievedPlan.get());
    }

    @Test
    public void testCreateNewPlanEnum() throws Exception {
        Plan basicPlan = createBasicPlan(PlansEnum.BASIC);
        Plan newPlan = planRepository.save(basicPlan);
        Optional<Plan> retrievedPlan = planRepository.findById(newPlan.getId());
        Assert.assertNotNull(retrievedPlan.get());
    }

    @Test
    public void testCreateNewRole() throws Exception {
        Role role = createBasicRole(RolesEnum.BASIC);
        Role newRole = new Role();
        if (!roleRepository.existsById(role.getId())) {
            log.debug("Creating role with id {}", role.getId());
            newRole = roleRepository.save(role);
            log.debug("Created role with id {} successfully", newRole.getId());
        }
        Optional<Role> retrievedRole = roleRepository.findById(newRole.getId());
        Assert.assertNotNull(retrievedRole);
    }

    @Test
    public void testCreateNewUser() {
        String username = testName.getMethodName();
        String email = testName.getMethodName() + "@gmail.com";
        User basicUser = createUser(username, email);

        Optional<User> newlyCreatedUser = userRepository.findById(basicUser.getId());
        Assert.assertNotNull(newlyCreatedUser);
        Assert.assertTrue(newlyCreatedUser.get().getId() != 0);
        Assert.assertNotNull(newlyCreatedUser.get().getPlan());
        Assert.assertNotNull(newlyCreatedUser.get().getPlan().getId());
        Set<UserRole> newlyCreatedUserRoles = newlyCreatedUser.get().getUserRoles();
        newlyCreatedUserRoles.forEach(ur -> {
            Assert.assertNotNull(ur);
            Assert.assertNotNull(ur.getRole());
            Assert.assertNotNull(ur.getRole().getId());
        });
    }

    @Test
    public void testDeleteUser() throws Exception {
        String username = testName.getMethodName();
        String email = testName.getMethodName() + "@gmail.com";
        User basicUser = createUser(username, email);
        userRepository.deleteById(basicUser.getId());
    }

    public void testGetUserByEmail() throws Exception {
        User basicUser = createUser(testName);

        User newUser = userRepository.findByEmail(basicUser.getEmail());
        Assert.assertNotNull(newUser);
        Assert.assertNotNull(newUser.getId());
        Assert.assertNotNull(newUser.getEmail());
    }

}
