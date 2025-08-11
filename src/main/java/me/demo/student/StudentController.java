package me.demo.student;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping(path = "students")
public class StudentController {

  private final StudentService studentService;

  // @Autowired
  public StudentController(StudentService studentService) {
    this.studentService = studentService;
  }

  @GetMapping()
  public List<IStudent> getAll(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "30") int size) {
    return studentService.getStudentsPage(page, size);
  }

  @GetMapping("/{id}")
  public IStudent getById(@PathVariable String id) {
    if (id == null || id.isEmpty()) {
      throw new IllegalArgumentException("ID cannot be null or empty");
    }
    if (!id.matches("\\d+")) {
      throw new IllegalArgumentException("ID must be a number");
    }
    long studentId = Long.parseLong(id);
    if (studentId <= 0) {
      throw new IllegalArgumentException("ID must be a positive number");
    }
    return studentService.getStudentById(studentId);
  }

  @PostMapping("")
  public IStudent create(@RequestBody IStudent student) {
    return studentService.addStudent(student);
  }

  @PutMapping("/{id}")
  public String putMethodName(@PathVariable String id, @RequestBody IStudent student) {
    if (id == null || id.isEmpty()) {
      throw new IllegalArgumentException("ID cannot be null or empty");
    }
    if (!id.matches("\\d+")) {
      throw new IllegalArgumentException("ID must be a number");
    }
    long studentId = Long.parseLong(id);
    if (studentId <= 0) {
      throw new IllegalArgumentException("ID must be a positive number");
    }

    boolean updated = studentService.updateStudent(studentId, (Student) student);
    if (updated) {
      return "Student updated successfully";
    } else {
      return "Student not found";
    }
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable String id) {
    boolean deleted = studentService.deleteStudent(Long.parseLong(id));
    if (deleted) {
      return "Student deleted successfully";
    } else {
      return "Student not found";
    }
  }
}
