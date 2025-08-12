package me.demo.auth;

import me.demo.auth.AuthDTO.AuthResponse;
import me.demo.auth.AuthDTO.LoginDTO;
import me.demo.auth.AuthDTO.RegisterDTO;
import me.demo.security.TokenProvider;
import me.demo.user.User;
import me.demo.user.UserDTO.UserResponseDto;
import me.demo.user.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthService {
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final PasswordEncoder encoder;
  private final TokenProvider tokenProvider;

  public AuthService(AuthenticationManager authenticationManager,
      UserRepository userRepository,
      PasswordEncoder encoder,
      TokenProvider tokenProvider) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.encoder = encoder;
    this.tokenProvider = tokenProvider;
  }

  public AuthResponse authenticateUser(LoginDTO loginDto) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginDto.email,
            loginDto.password));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String token = tokenProvider.generateAccessToken(userDetails);

    User dbUser = userRepository.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

    UserResponseDto userResponse = new UserResponseDto(
        dbUser.getId(),
        dbUser.getFirstName(),
        dbUser.getLastName(),
        dbUser.getEmail(),
        dbUser.getDateOfBirth(),
        dbUser.getRoles());

    return new AuthResponse(token, userResponse);
  }

  public void registerUser(RegisterDTO user) {
    Optional<User> existingUser = userRepository.findByEmail(user.email);
    if (existingUser.isPresent()) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT, "Error: Email is already in use!");
    }

    String encodedPassword = encoder.encode(user.password);
    User newUser = new User(
        user.firstName,
        user.lastName,
        user.email,
        user.dateOfBirth,
        encodedPassword,
        null);
    userRepository.save(newUser);
  }
}
