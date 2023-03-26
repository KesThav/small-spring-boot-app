package com.example.demo.student;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentConfig {

  @Bean
  CommandLineRunner commandLineRunner(StudentRepository repository) {
    return args -> {
      Student james = new Student("James Bond", LocalDate.of(2000, 03, 02), "james@bond.com");
      Student jamesy = new Student("Jamesy Bondy", LocalDate.of(2000, 03, 02), "james@bond.com");

      repository.saveAll(List.of(james, jamesy));
    };

  }
}
