package com.devopsbuddy;

import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.service.UserService;
import com.devopsbuddy.enums.PlansEnum;
import com.devopsbuddy.enums.RolesEnum;
import com.devopsbuddy.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;
@Slf4j
@SpringBootApplication
public class DevopsbuddyApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;
	@Value("${sysadmin.username}")
	private String sysAdminUsername;
	@Value("${sysadmin.password}")
	private String sysAdminPassword;
	@Value("${sysadmin.email}")
	private String sysAdminEmail;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(DevopsbuddyApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		User basicUser = UserUtils.createBasicUser(sysAdminUsername, sysAdminEmail);
		basicUser.setPassword(sysAdminPassword);
		log.debug("Creating user with username {}", basicUser.getUsername());

		User user = userService.createUser(basicUser, PlansEnum.PRO, RolesEnum.PRO);
		log.debug("User {} created successfully!", basicUser.getUsername());
	}
}
