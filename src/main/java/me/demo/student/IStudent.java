package me.demo.student;

import java.time.LocalDate;

public interface IStudent {
  Long getId();
  String getName();
  String getEmail();
  LocalDate getDateOfBirth();
  Integer getAge();
  String toString();
}
