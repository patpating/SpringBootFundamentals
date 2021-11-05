package ttl.larku.dao.jpahibernate;

import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Student;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Transactional
public class JPAStudentDAO implements BaseDAO<Student> {

    @PersistenceContext
    private EntityManager entityManager;

    private String from;

    public JPAStudentDAO(String from) {
        this.from = from + ": ";
    }

    public JPAStudentDAO() {
        this("JPA");
    }

    public boolean update(Student updateObject) {
        entityManager.merge(updateObject);
        return true;
    }

    public boolean delete(Student student) {
        entityManager.remove(student);
        return true;
    }

    public Student create(Student newObject) {
        //Put our Mark
        newObject.setName(from + newObject.getName());

        entityManager.persist(newObject);

        return newObject;
    }

    public Student get(int id) {
        return entityManager.find(Student.class, id);
    }

    public List<Student> getAll() {
        TypedQuery<Student> query = entityManager.createQuery("select s from Student s", Student.class);

        List<Student> students = query.getResultList();
        return students;
    }

//    public void deleteStore() {
//        students = null;
//    }
//
//    public void createStore() {
//        students = new ConcurrentHashMap<>();
//        nextId = new AtomicInteger(1);
//    }
//
//    public Map<Integer, Student> getStudents() {
//        return students;
//    }
}
