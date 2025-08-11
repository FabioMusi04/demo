package me.demo.user;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import me.demo.auditable.Auditable;

@Table
@Entity(name = "users")
public class User extends Auditable<Long> implements IUser, UserDetails, Comparable<User> {
  @Id
  @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_sequence")
  private Long id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "email", nullable = false, unique = true, updatable = false)
  private String email;

  @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "user_roles", joinColumns = @jakarta.persistence.JoinColumn(name = "user_id"))
  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  private Set<UserRole> roles = new HashSet<>(Set.of(UserRole.ROLE_USER));

  @Column(name = "date_of_birth", nullable = false)
  private LocalDate dateOfBirth;

  @Column(name = "password", nullable = false)
  private String password;

  public Long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Set<UserRole> getRoles() {
    return roles;
  }

  public void setRoles(Set<UserRole> roles) {
    this.roles = roles;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getPassword() {
    return password;
  }

  public Integer getAge() {
    if (dateOfBirth == null)
      return null;
    return LocalDate.now().getYear() - dateOfBirth.getYear();
  }

  public User(String firstName, String lastName, String email, LocalDate dateOfBirth, String password, Set<UserRole> roles) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.dateOfBirth = dateOfBirth;
    this.password = password;
    this.roles = roles != null ? roles : new HashSet<>(Set.of(UserRole.ROLE_USER));
  }

  public User() {
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public int compareTo(User other) {
    return this.email.compareTo(other.email);
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        ", dateOfBirth=" + dateOfBirth +
        '}';
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.name()))
        .toList();
  }

  @Override
  public String getUsername() {
    return email;
  }

   @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
