package me.demo.student;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentConfig {
  
  @Bean(name = "studentCommandLineRunner")
  CommandLineRunner commandLineRunner(StudentRepository studentRepository) {
    return args -> {
      for (int i = 0; i < 50; i++) {
        String name = "Student " + (i + 1);
        String email = "student" + (i + 1) + "@example.com";
        LocalDate dateOfBirth = LocalDate.of(2000 + i, 1, 1);
        Student student = new Student(name, email, dateOfBirth);
        studentRepository.save(student);
      } 
    };
  }
}
