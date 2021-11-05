package ttl.larku.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ttl.larku.domain.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

//    @Override
//    @Query("select distinct s from Student s left join fetch s.classes sc left join fetch sc.course")
    //public List<Student> findAll();

    List<Student> findByName(String name);
    List<Student> findByNameIgnoreCaseContains(String name);
}
