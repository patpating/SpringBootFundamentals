package ttl.larku.app;

import ttl.larku.domain.Student;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class JPADemo {

    public static void main(String[] args) {
        JPADemo jpaDemo = new JPADemo();
//        jpaDemo.insertStudent();
//        jpaDemo.updateStudent();
        jpaDemo.deleteStudent();
        jpaDemo.printStudents();
    }

    private EntityManagerFactory factory;

    public JPADemo() {
       factory = Persistence.createEntityManagerFactory("LarkUPU_SE");
    }

    public void printStudents() {
        EntityManager em = factory.createEntityManager();
        TypedQuery<Student> query = em.createQuery("select s from Student s", Student.class);

        List<Student> students = query.getResultList();

        System.out.println("students.size: " + students.size());
        students.forEach(System.out::println);
    }

    public void insertStudent() {
        Student student = new Student("Carly", "3838 93 9393", Student.Status.FULL_TIME);
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();

        em.persist(student);

        em.getTransaction().commit();
    }

    public void updateStudent() {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();

        Student student = em.find(Student.class, 8);
        student.setName("Ronald");

        em.getTransaction().commit();
    }

    public void deleteStudent() {
        EntityManager em = factory.createEntityManager();
//        em.getTransaction().begin();

        Student student = em.find(Student.class, 8);

        em.getTransaction().begin();
        em.remove(student);

        em.getTransaction().commit();
    }
}