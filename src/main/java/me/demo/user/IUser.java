package me.demo.user;

import java.time.LocalDate;
import java.util.Set;

public interface IUser {
  Long getId();
  String getFirstName();
  String getLastName();
  String getEmail();
  Set<UserRole> getRoles();
  LocalDate getDateOfBirth();
  String getPassword();
  Integer getAge();
}
