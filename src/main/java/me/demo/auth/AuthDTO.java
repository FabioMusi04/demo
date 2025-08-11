package me.demo.auth;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import me.demo.user.UserDTO.UserResponseDto;

public class AuthDTO {
  public static class LoginDTO {
    @Email
    @NotBlank
    public String email;
    @NotBlank
    public String password;
  }

  public static class RegisterDTO {
    @NotBlank
    public String firstName;

    @NotBlank
    public String lastName;

    @Email
    @NotBlank
    public String email;

    @NotBlank
    @NotNull(message = "Date of birth cannot be null")
    @Size(min = 10, message = "Date of birth must be in the format YYYY-MM-DD")
    @Past(message = "Date of birth must be in the past")
    public LocalDate dateOfBirth;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters")
    public String password;
  }

  public static class AuthResponse {
    public String token;
    public UserResponseDto user;

    public AuthResponse(String token, UserResponseDto user) {
      this.token = token;
      this.user = user;
    }
  }
}