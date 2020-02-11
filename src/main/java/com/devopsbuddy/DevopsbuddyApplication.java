package com.devopsbuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.devopsbuddy.backend.persistence.repositories")
public class DevopsbuddyApplication {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(DevopsbuddyApplication.class, args);
	}

}
