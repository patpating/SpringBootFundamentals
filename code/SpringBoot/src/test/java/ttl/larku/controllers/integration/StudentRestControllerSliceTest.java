package ttl.larku.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ttl.larku.domain.Student;
import ttl.larku.service.StudentService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@WebMvcTest(controllers = StudentRestController.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Tag("integration")
@Disabled
public class StudentRestControllerSliceTest {

    @MockBean
    private StudentService studentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext context;

    private final int goodStudentId = 1;
    private final int badStudentId = 10000;


    @BeforeEach
    public void setup() {

        List<Student> students = Arrays.asList(new Student("Manoj", "282 929 9292", Student.Status.FULL_TIME),
                new Student("Alice", "393 9393 030", Student.Status.HIBERNATING));
        //Create a mock for the one controller we want to test.

        Mockito.when(studentService.getAllStudents()).thenReturn(students);
        Mockito.when(studentService.getStudent(goodStudentId)).thenReturn(students.get(0));
        Mockito.when(studentService.getStudent(badStudentId)).thenReturn(null);
        Student student = new Student("Yogita");
        Mockito.when(studentService.createStudent(any(Student.class))).thenReturn(student);
    }

    @Test
    public void testGetOneStudentGoodJson() throws Exception {
        MediaType accept = MediaType.APPLICATION_JSON;
        MediaType contentType = accept;

        MockHttpServletRequestBuilder builder = get("/adminrest/student/{id}", goodStudentId)
                .accept(accept)
                .contentType(contentType);


        ResultActions actions = mockMvc.perform(builder);

        actions = actions.andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.entity.name").value(containsString("Manoj")));

        // Get the result and return it
        MvcResult result = actions.andReturn();

        MockHttpServletResponse response = result.getResponse();
        String jsonString = response.getContentAsString();
        System.out.println("One student good resp = " + jsonString);
        
        Mockito.verify(studentService).getStudent(goodStudentId);
    }

    @Test
    public void testGetOneStudentBadId() throws Exception {

        ResultActions actions = mockMvc
                .perform(get("/adminrest/student/{id}", badStudentId)
                        .accept(MediaType.APPLICATION_JSON));

        MvcResult result = actions
                .andExpect(status().is4xxClientError())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        String jsonString = response.getContentAsString();
        System.out.println("resp = " + jsonString);

        Mockito.verify(studentService, never()).getStudent(goodStudentId);
    }

    @Test
    public void testAddStudentGood() throws Exception {

        Student student = new Student("Yogita");
        student.setPhoneNumber("202 383-9393");
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(student);

        ResultActions actions = mockMvc.perform(post("/adminrest/student/").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString));

        actions = actions.andExpect(content().contentType(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(status().isCreated());

        actions = actions.andExpect(jsonPath("$.entity.name").value(Matchers.containsString("Yogita")));


        MvcResult result = actions.andReturn();

        MockHttpServletResponse response = result.getResponse();

        jsonString = response.getContentAsString();
        System.out.println("resp = " + jsonString);
        
        Mockito.verify(studentService).createStudent(student);

    }

    @Test
    public void testAddStudentWithNoContentType() throws Exception {

        Student student = new Student("Yogita");
        student.setPhoneNumber("202 383-9393");
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(student);

        //ResultActions actions = mockMvc.perform(post("/adminrest/student/").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString));
        ResultActions actions = mockMvc.perform(post("/adminrest/student/")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonString));

        actions = actions.andExpect(status().isUnsupportedMediaType());

        Mockito.verify(studentService, never()).createStudent(student);
    }

    @Test
    public void testGetAllStudentsGood() throws Exception {

        ResultActions actions = mockMvc.perform(get("/adminrest/student/").accept(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(status().isOk());

        actions = actions.andExpect(jsonPath("$.entity", hasSize(2)));
        MvcResult result = actions.andReturn();

        MockHttpServletResponse response = result.getResponse();

        String jsonString = response.getContentAsString();
        System.out.println("resp = " + jsonString);
        
        Mockito.verify(studentService).getAllStudents();
    }
}
