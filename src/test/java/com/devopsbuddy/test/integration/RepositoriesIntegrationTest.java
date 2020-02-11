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
import com.devopsbuddy.utils.UsersUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DevopsbuddyApplication.class)
public class RepositoriesIntegrationTest {

    private static final int BASIC_PLAN_ID = 1;
    private static final int BASIC_ROLE_ID = 1;
    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void init(){
        Assert.assertNotNull(planRepository);
        Assert.assertNotNull(roleRepository);
        Assert.assertNotNull(userRepository);
    }

    @Test
    public void testCreateNewPlan() throws Exception {
        Plan basicPlan = createBasicPlan();
        planRepository.save(basicPlan);
        Optional<Plan> retrievedPlan = planRepository.findById(BASIC_PLAN_ID);
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
        Role newRole = roleRepository.save(role);
        Optional<Role> retrievedRole = roleRepository.findById(newRole.getId());
        Assert.assertNotNull(retrievedRole);
    }

    @Test
    public void testCreateNewUser() {
        Plan basicPlan = createBasicPlan();
        planRepository.save(basicPlan);

        User basicUser = UsersUtils.createBasicUser();
        basicUser.setPlan(basicPlan);

        Role basicRole = createBasicRole();
        Set<UserRole> userRoles = new HashSet<>();
        UserRole userRole = new UserRole(basicUser, basicRole);
        //userRole.setUser(basicUser);
        //userRole.setRole(basicRole);
        userRoles.add(userRole);

        //basicUser.setUserRoles(userRoles);
        basicUser.getUserRoles().addAll(userRoles);

        userRoles.forEach( ur -> {
            roleRepository.save(ur.getRole());
        });

        basicUser.setUserRoles(userRoles);

        User newUser = userRepository.save(basicUser);

        Optional<User> newlyCreatedUser = userRepository.findById(newUser.getId());
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

    private Plan createBasicPlan(){
        Plan plan = new Plan();
        plan.setId(BASIC_PLAN_ID);
        plan.setName("Basic");
        return plan;
    }

    private Plan createBasicPlan(PlansEnum plansEnum){
        return new Plan(plansEnum);
    }

    private Role createBasicRole(){
        Role role = new Role();
        role.setId(BASIC_ROLE_ID);
        role.setName("ROLE_USER");
        return role;
    }

    private Role createBasicRole(RolesEnum rolesEnum){
        return new Role(rolesEnum);
    }
}
