package me.demo.auth;

import me.demo.auth.AuthDTO.AuthResponse;
import me.demo.auth.AuthDTO.LoginDTO;
import me.demo.auth.AuthDTO.RegisterDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/signin")
  public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginDTO loginDto) {
    AuthResponse response = authService.authenticateUser(loginDto);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/signup")
  public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterDTO user) {
    authService.registerUser(user);
    return ResponseEntity.ok("User registered successfully");
  }
}