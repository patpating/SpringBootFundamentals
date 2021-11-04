package ttl.larku.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ttl.larku.domain.Student;
import ttl.larku.service.StudentService;

import java.util.List;

/*
REST == Representational State Transfer

 */
@RestController
@RequestMapping("/student")
public class StudentController {

//    @GetMapping
//    public String tryit() {
//        return "This really works????";
//    }

    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<Student> getStudents() {
       List<Student> students = studentService.getAllStudents();
       return students;
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable("id") int id) {
        Student student = studentService.getStudent(id);
        return student;
    }

    @PostMapping
    public Student insertStudent(@RequestBody Student student) {
        Student newStudent = studentService.createStudent(student);

        return newStudent;
    }

}
