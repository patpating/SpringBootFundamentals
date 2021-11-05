package ttl.larku.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ttl.larku.domain.Student;
import ttl.larku.service.StudentRepoService;
import ttl.larku.service.StudentService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.URI;
import java.util.List;

/*
REST == Representational State Transfer

 */
@RestController
@RequestMapping("/repostudent")
public class StudentRepoController {

//    @GetMapping
//    public String tryit() {
//        return "This really works????";
//    }

    @Autowired
    private StudentRepoService studentService;

    @PersistenceContext
    private EntityManager entityManger;

    @GetMapping
    public List<Student> getStudents() {
       List<Student> students = studentService.getAllStudents();
       return students;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable("id") int id) {
        Student student = studentService.getStudent(id);
        if(student == null) {
            return ResponseEntity.status(404).body("No student with id: " + id);
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public  ResponseEntity<?> insertStudent(@RequestBody Student student) {
        Student newStudent = studentService.createStudent(student);

        URI newResource = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newStudent.getId())
                .toUri();


        return ResponseEntity.created(newResource).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") int id) {
        boolean result = studentService.deleteStudent(id);
        if(result) {
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.status(404).body("No student with id: " + id);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateStudent(@RequestBody Student student) {
        boolean result = studentService.updateStudent(student);
        if(result) {
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.status(404).body("No student with id: " + student.getId());
        }
    }

}
