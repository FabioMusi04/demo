package me.demo.user;

import java.util.List;


import me.demo.user.UserDTO.UserCreationDto;
import me.demo.user.UserDTO.UserResponseDto;
import me.demo.user.UserDTO.UserUpdateDto;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping(path = "users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping()
  public ResponseEntity<List<UserResponseDto>> getAll(@RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "30") int size) {
    List<UserResponseDto> users = userService.getUsersPage(page, size);
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDto> getById(@PathVariable String id) {
    if (id == null || id.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    if (!id.matches("\\d+")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    long userId = Long.parseLong(id);
    if (userId <= 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    UserResponseDto responsedDto = userService.getUserById(userId);
    if (responsedDto == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(responsedDto);
  }

  @PostMapping("")
  public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserCreationDto user) {
    UserResponseDto createdUser = userService.addUser(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @PutMapping("/{id}")
  public ResponseEntity<String> update(@PathVariable String id, @RequestBody UserUpdateDto user) {
    if (id == null || id.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID cannot be null or empty");
    }
    if (!id.matches("\\d+")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID must be a number");
    }
    long userId = Long.parseLong(id);
    if (userId <= 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID must be a positive number");
    }

    boolean updated = userService.updateUser(userId, user);
    if (updated) {
      return ResponseEntity.ok("User updated successfully");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable String id) {
    if (id == null || id.isEmpty() || !id.matches("\\d+") || Long.parseLong(id) <= 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID");
    }
    boolean deleted = userService.deleteUser(Long.parseLong(id));
    if (deleted) {
      return ResponseEntity.ok("User deleted successfully");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
  }

  @GetMapping("/me")
  public ResponseEntity<UserResponseDto> getMe(@AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    UserResponseDto user = userService.getUserByEmail(userDetails.getUsername());
    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(user);
  }
}
