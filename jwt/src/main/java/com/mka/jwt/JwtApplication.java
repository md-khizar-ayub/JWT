package com.mka.jwt;

import com.mka.jwt.entities.Role;
import com.mka.jwt.entities.User;
import com.mka.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;

@SpringBootApplication
//@EnableCaching
public class JwtApplication implements CommandLineRunner {



	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User adminUserAccount = userRepository.findByRole(Role.ADMIN);
		if (adminUserAccount == null) {


			User user = new User();
			user.setEmail("admin@gmail.com");
			user.setFirstname("admin");
			user.setSecondname("admin");
			user.setRole(Role.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));

			userRepository.save(user);
		}
	}

}
