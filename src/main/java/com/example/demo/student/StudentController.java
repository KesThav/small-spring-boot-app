package com.example.demo.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {

  private final StudentService studentService;

  @Autowired
  public StudentController(StudentService studentService) {
    this.studentService = studentService;
  }

  @GetMapping("/hello")
  public List<Student> getStudents() {
    return studentService.getStudents();
  }

  @PostMapping("/student")
  public void registerNewStudent(@RequestBody Student student) {
    studentService.addNewStudent(student);
  }

  @DeleteMapping("/student/{studentId}")
  public void deleteStudent(@PathVariable("studentId") Long studentId) {
    studentService.deleteStudent(studentId);
  }

  @PutMapping("/student/{studentId}")
  public void updateStudent(@PathVariable("studentId") Long studentId, @RequestBody Student student) {
    studentService.updateStudent(studentId, student);
  }

}
