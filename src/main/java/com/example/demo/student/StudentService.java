package com.example.demo.student;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class StudentService {

  private final StudentRepository studentRepository;

  @Autowired
  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  public List<Student> getStudents() {
    return studentRepository.findAll();
  }

  public void addNewStudent(Student student) {
    Optional<Student> studentByEmail = studentRepository
        .findStudentByEmail(student.getEmail());
    if (studentByEmail.isPresent()) {
      throw new IllegalStateException("email taken");
    } else {
      studentRepository.save(student);
    }
  }

  public void deleteStudent(Long studentId) {
    boolean exists = studentRepository.existsById(studentId);
    if (!exists) {
      throw new IllegalStateException("student with id " + studentId + " does not exist");
    } else {
      studentRepository.deleteById(studentId);
    }
  }

  @Transactional
  public void updateStudent(Long studentId, Student updatedStudent) {
    Student existingStudent = studentRepository.findById(studentId)
        .orElseThrow(() -> new IllegalStateException("Student with id " + studentId + " does not exist"));

    existingStudent.setName(updatedStudent.getName());
    existingStudent.setEmail(updatedStudent.getEmail());
    existingStudent.setDob(updatedStudent.getDob());
    studentRepository.save(existingStudent);
  }
}
