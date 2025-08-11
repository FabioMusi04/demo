package me.demo.user;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.demo.user.UserDTO.UserCreationDto;
import me.demo.user.UserDTO.UserResponseDto;
import me.demo.user.UserDTO.UserUpdateDto;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  public List<UserResponseDto> getAll(@RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "30") int size) {
    return userService.getUsersPage(page, size);
  }

  @GetMapping("/{id}")
  public UserResponseDto getById(@PathVariable String id) {
    if (id == null || id.isEmpty()) {
      throw new IllegalArgumentException("ID cannot be null or empty");
    }
    if (!id.matches("\\d+")) {
      throw new IllegalArgumentException("ID must be a number");
    }
    long userId = Long.parseLong(id);
    if (userId <= 0) {
      throw new IllegalArgumentException("ID must be a positive number");
    }
    return userService.getUserById(userId);
  }

  @PostMapping("")
  public UserResponseDto create(@RequestBody UserCreationDto user) {
    return userService.addUser(user);
  }

  @PutMapping("/{id}")
  public String update(@PathVariable String id, @RequestBody UserUpdateDto user) {
    if (id == null || id.isEmpty()) {
      throw new IllegalArgumentException("ID cannot be null or empty");
    }
    if (!id.matches("\\d+")) {
      throw new IllegalArgumentException("ID must be a number");
    }
    long userId = Long.parseLong(id);
    if (userId <= 0) {
      throw new IllegalArgumentException("ID must be a positive number");
    }

    boolean updated = userService.updateUser(userId, user);
    if (updated) {
      return "User updated successfully";
    } else {
      return "User not found";
    }
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable String id) {
    boolean deleted = userService.deleteUser(Long.parseLong(id));
    if (deleted) {
      return "User deleted successfully";
    } else {
      return "User not found";
    }
  }

  @GetMapping("/me")
  public UserResponseDto getMe(@AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      throw new IllegalArgumentException("User not authenticated");
    }
    return userService.getUserByEmail(userDetails.getUsername());
  }
}
