package me.demo.student;

import me.demo.auditable.Auditable;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student extends Auditable<Long> implements IStudent, Comparable<Student>, Serializable {
  @Id
  @SequenceGenerator(name = "student_sequence", sequenceName = "student_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "date_of_birth", nullable = false)
  private LocalDate dateOfBirth;

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public Integer getAge() {
    if (dateOfBirth == null)
      return null;
    return LocalDate.now().getYear() - dateOfBirth.getYear();
  }

  public Student(String name, String email, LocalDate dateOfBirth) {
    this.name = name;
    this.email = email;
    this.dateOfBirth = dateOfBirth;
  }

  public Student() {
  }

  @Override
  public String toString() {
    return "Student{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", email='" + email + '\'' +
        ", dateOfBirth=" + dateOfBirth +
        ", age=" + getAge() +
        '}';
  }

  @Override
  public int compareTo(Student other) {
    return this.name.compareToIgnoreCase(other.name);
  }
}
