package ttl.larku.dao;

import ttl.larku.dao.inmemory.InMemoryStudentDAO;
import ttl.larku.dao.inmemory.StudentDAO;
import ttl.larku.dao.jpa.JpaStudentDAO;
import ttl.larku.service.StudentService;

import java.util.ResourceBundle;

public class TheFactory {

    public static StudentDAO studentDAO() {
        ResourceBundle bundle = ResourceBundle.getBundle("larkUContext");
        String profile = bundle.getString("larku.profile.active");
        switch (profile) {
            case "dev":
                return new InMemoryStudentDAO();
            case "prod":
                return new JpaStudentDAO();
            default:
                throw new RuntimeException("Unknow profile: " + profile);
        }
    }

    public static StudentService studentService() {
        StudentService ss = new StudentService();
        return ss;
    }
}
