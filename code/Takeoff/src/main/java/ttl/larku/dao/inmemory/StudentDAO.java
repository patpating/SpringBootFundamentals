package ttl.larku.dao.inmemory;

import ttl.larku.domain.Student;

import java.util.List;

public interface StudentDAO {
    boolean update(Student updateObject);

    boolean delete(Student student);

    Student create(Student newObject);

    Student get(int id);

    List<Student> getAll();
}
