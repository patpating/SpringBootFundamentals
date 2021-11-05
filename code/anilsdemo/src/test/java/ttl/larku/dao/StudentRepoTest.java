package ttl.larku.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ttl.larku.dao.repo.StudentRepository;
import ttl.larku.domain.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class StudentRepoTest {

    @Autowired
    private StudentRepository studentRepo;

    @Test
    public void testFindByName() {
        List<Student> names = studentRepo.findByNameIgnoreCaseContains("Ana");

        names.forEach(System.out::println);

    }

    @Test
    public void testGetAll() {
        List<Student> names = studentRepo.findAll();

        names.forEach(System.out::println);

        assertEquals(8, names.size());
    }
}
