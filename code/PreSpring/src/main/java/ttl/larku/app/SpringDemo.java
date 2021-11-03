package ttl.larku.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ttl.larku.domain.Course;
import ttl.larku.domain.Student;
import ttl.larku.jconfig.LarkUConfig;
import ttl.larku.service.CourseService;
import ttl.larku.service.StudentService;

import java.util.List;

public class SpringDemo {

    public static void main(String[] args) {
        SpringDemo sd = new SpringDemo();
        sd.goCourses();
    }


    public void goCourses() {//ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(LarkUConfig.class);

        CourseService cs = context.getBean("courseService", CourseService.class);

        List<Course> result = cs.getAllCourses();
        System.out.println("size: " + result.size());
        result.forEach(System.out::println);
    }

    public void goStudents() {
        //ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        ApplicationContext context = new AnnotationConfigApplicationContext(LarkUConfig.class);

        StudentService ss = context.getBean("studentService", StudentService.class);
        StudentService ss2 = context.getBean("studentService", StudentService.class);

        List<Student> students = ss.getAllStudents();
        System.out.println("student.size: " + students.size());
        students.forEach(System.out::println);
    }
}
