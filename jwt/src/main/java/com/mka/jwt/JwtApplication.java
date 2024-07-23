package com.mka.jwt;

import com.mka.jwt.entities.Role;
import com.mka.jwt.entities.User;
import com.mka.jwt.repository.UserRepository;
import com.mka.jwt.services.impl.AuthenticationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;

@SpringBootApplication
public class JwtApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		deleteLogFile();

		logger.info("Application started with command-line arguments: {}", (Object) args);

		User adminUserAccount = userRepository.findByRole(Role.ADMIN);
		if (adminUserAccount == null) {
			logger.info("Admin user account not found, creating new one.");

			User user = new User();
			user.setEmail("admin@gmail.com");
			user.setFirstname("admin");
			user.setSecondname("admin");
			user.setRole(Role.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));

			userRepository.save(user);

			logger.info("Admin user account created.");
		}
	}

	private void deleteLogFile() {
		File logsDir = new File("logs");
		if (!logsDir.exists()) {
			logsDir.mkdirs();
		}

		String logFileName = "logs/log-" + java.time.LocalDate.now() + ".log";
		File logFile = new File(logFileName);
		if (logFile.exists()) {
			if (logFile.delete()) {
				logger.info("Log file {} deleted successfully.", logFileName);
			} else {
				logger.error("Failed to delete log file {}", logFileName);
			}
		} else {
			logger.info("Log file {} does not exist.", logFileName);
		}
	}

}
