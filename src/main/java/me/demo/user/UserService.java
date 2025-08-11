package me.demo.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.demo.user.UserDTO.UserCreationDto;
import me.demo.user.UserDTO.UserResponseDto;
import me.demo.user.UserDTO.UserUpdateDto;

import java.util.List;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<UserResponseDto> getUsersPage(int page, int size) {
    int offset = page * size;
    return userRepository.findAll()
        .stream()
        .skip(offset)
        .limit(size)
        .map(user -> new UserResponseDto(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getDateOfBirth(),
            user.getRoles()
        ))
        .toList();
  }

  public UserResponseDto getUserById(Long id) {
    return userRepository.findById(id)
        .map(user -> new UserResponseDto(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getDateOfBirth(),
            user.getRoles()
        ))
        .orElse(null);
  }

  public UserResponseDto addUser(UserCreationDto user) {
    if (userRepository.findByEmail(user.email).isPresent()) {
      throw new IllegalArgumentException("Email already exists");
    }

    User newUser = new User(
        user.firstName,
        user.lastName,
        user.email,
        user.dateOfBirth,
        "password",
        user.roles
    );
    User savedUser = userRepository.save(newUser);
    return new UserResponseDto(
        savedUser.getId(),
        savedUser.getFirstName(),
        savedUser.getLastName(),
        savedUser.getEmail(),
        savedUser.getDateOfBirth(),
        savedUser.getRoles()
    );
  }

  @Transactional
  public boolean updateUser(Long id, UserUpdateDto updatedUser) {
    if (userRepository.existsById(id)) {
      User existingUser = (User) userRepository.findById(id).orElse(null);
      if (existingUser != null) {

        existingUser.setFirstName(updatedUser.firstName);
        existingUser.setLastName(updatedUser.lastName);
        existingUser.setDateOfBirth(updatedUser.dateOfBirth);
        existingUser.setRoles(updatedUser.roles);

        userRepository.save(existingUser);
        return true;
      }
    }
    return false;
  }

  public boolean deleteUser(Long id) {
    if (userRepository.existsById(id)) {
      userRepository.deleteById(id);
      return true;
    }
    return false;
  }

  public UserResponseDto getUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .map(user -> new UserResponseDto(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getDateOfBirth(),
            user.getRoles()
        ))
        .orElse(null);
  }
}