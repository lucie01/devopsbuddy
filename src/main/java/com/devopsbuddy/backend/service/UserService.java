package com.devopsbuddy.backend.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(User user, PlansEnum plansEnum, RolesEnum rolesEnum){
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        Plan plan = new Plan(plansEnum);
        if (!planRepository.existsById(plansEnum.getId())) {
           plan = planRepository.save(plan);
        }

        Set<UserRole> userRoles = new HashSet<>();
        UserRole userRole = new UserRole(user, new Role(rolesEnum));
        userRoles.add(userRole);

        user.setPlan(plan);

        userRoles.forEach( uRole -> {
            if (!roleRepository.existsById(uRole.getRole().getId())) {
                log.debug("Creating role with id {}", uRole.getRole().getId());
                roleRepository.save(uRole.getRole());
                log.debug("Created role with id {} successfully", uRole.getRole().getId());
            }
        });

        user.getUserRoles().addAll(userRoles);
        user.setUserRoles(userRoles);

        User userExistsVerification = userRepository.findByUsername(user.getUsername());
        boolean exists = false;
        try {
            exists = userExistsVerification.isEnabled();
            log.debug("User {}  exists", user.getUsername());
        } catch (NullPointerException e) {
            log.debug("User {}  don't exist", user.getUsername());
        }


        if (!exists) {
            log.debug("Creating user with id {}", user.getId());
            user = userRepository.save(user);
            log.debug("Created user with id {} successfully", user.getId());
        }
        return user;
    }

    @Transactional
    public void updateUserPassword(long userId, String password) {
        password = passwordEncoder.encode(password);
        userRepository.updateUserPassword(userId, password);
        log.debug("Password updated successfully for userId {}", userId);
    }
}
