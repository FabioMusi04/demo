package me.demo.student;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class StudentService {
  private final StudentRepository studentRepository;

  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  public List<IStudent> getStudentsPage(int page, int size) {
    int offset = page * size;
    return studentRepository.findAll()
        .stream()
        .skip(offset)
        .limit(size)
        .map(student -> (IStudent) student)
        .toList();
  }

  public IStudent getStudentById(Long id) {
    return studentRepository.findById(id).orElse(null);
  }

  public IStudent addStudent(IStudent student) {
    if (student == null || student.getName() == null || student.getEmail() == null || student.getDateOfBirth() == null) {
      throw new IllegalArgumentException("Student details cannot be null");
    }

    if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
      throw new IllegalArgumentException("Email already exists");
    }

    return (IStudent) studentRepository.save((Student) student);
  }

  @Transactional
  public boolean updateStudent(Long id, Student updatedStudent) {
    if (studentRepository.existsById(id)) {
      Student existingStudent = (Student) studentRepository.findById(id).orElse(null);
      if (existingStudent != null) {

        if (updatedStudent.getName() == null || updatedStudent.getEmail() == null || updatedStudent.getDateOfBirth() == null) {
          throw new IllegalArgumentException("Student details cannot be null");
        }
        if (!existingStudent.getEmail().equals(updatedStudent.getEmail()) &&
            studentRepository.findByEmail(updatedStudent.getEmail()).isPresent()) {
          throw new IllegalArgumentException("Email already exists");
        }

        existingStudent.setName(updatedStudent.getName());
        existingStudent.setEmail(updatedStudent.getEmail());
        existingStudent.setDateOfBirth(updatedStudent.getDateOfBirth());

        studentRepository.save(existingStudent);
        return true;
      }
    }
    return false;
  }

  public boolean deleteStudent(Long id) {
    if (studentRepository.existsById(id)) {
      studentRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
