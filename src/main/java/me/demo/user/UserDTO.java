package me.demo.user;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public class UserDTO {
  public static class UserResponseDto {
    public Long id;
    public String firstName;
    public String lastName;
    public String email;
    public LocalDate dateOfBirth;
    public Set<UserRole> roles;

    public UserResponseDto(Long id, String firstName, String lastName, String email, LocalDate dateOfBirth,
        Set<UserRole> roles) {
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
      this.dateOfBirth = dateOfBirth;
      this.roles = roles;
    }
  }

  public static class UserCreationDto {
    @NotBlank
    public String firstName;

    @NotBlank
    public String lastName;

    @Email
    @NotBlank
    public String email;

    @NotNull
    @Past
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate dateOfBirth;

    @NotEmpty
    public Set<UserRole> roles;
  }

  public static class UserUpdateDto {
    @NotBlank
    public String firstName;

    @NotBlank
    public String lastName;

    @NotNull
    @Past
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate dateOfBirth;

    @NotEmpty
    public Set<UserRole> roles;
  }
}
