package me.demo.user;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserConfig {
  private final PasswordEncoder encoder;

  public UserConfig(PasswordEncoder encoder) {
    this.encoder = encoder;
  }

  @Bean(name = "userCommandLineRunner")
  CommandLineRunner commandLineRunner(UserRepository userRepository) {
    return args -> {
      String encodedPassword = encoder.encode("password123");
      User user = new User(
        "Fabio",
        "Musitelli",
        "fabio04musitelli@gmail.com",
        LocalDate.of(1990, 1, 1),
        encodedPassword,
        null
      );
      userRepository.save(user);
    };
  }
}
