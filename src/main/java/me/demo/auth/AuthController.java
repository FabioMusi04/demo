package me.demo.auth;

import me.demo.auth.AuthDTO.AuthResponse;
import me.demo.auth.AuthDTO.LoginDTO;
import me.demo.auth.AuthDTO.RegisterDTO;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/signin")
  public AuthResponse authenticateUser(@RequestBody LoginDTO user) {
    return authService.authenticateUser(user);
  }

  @PostMapping("/signup")
  public String registerUser(@RequestBody RegisterDTO user) {
    authService.registerUser(user);
    return "User registered successfully";
  }
}