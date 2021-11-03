package ttl.larku.dao;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.Transactional;
import ttl.larku.dao.jpahibernate.JPATrackDAO;
import ttl.larku.dao.jpahibernate.SearchSpec;
import ttl.larku.dao.jpahibernate.SearchWrapper;
import ttl.larku.domain.Track;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class JPATrackDAOFlexiTest {

    @Autowired
    private JPATrackDAO trackDAO;

    /**
     * A Technique to run sql scripts just once per class.
     * This was to solve a tricky situation.  This class
     * might use a cached context if some test before it
     * used the same configuration as this one.
     *
     * Since no context is created for this test,
     * no DDL scripts are run.  So this test gets whatever
     * was in put into the database by the previous test for the
     * same context.
     * Tests in this class can fail in
     * strange ways that depend on which other tests are run.
     * This trick makes sure that this test starts with the
     * data it is expecting.  The @Transactional then takes
     * care of rolling back after each test.
     * @param dataSource
     * @throws SQLException
     */
    @BeforeAll
    public static void runSqlScriptsOnce(@Autowired DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("schema.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("data.sql"));
        }
    }


    @Test
    public void testFlexiSearch() {
        SearchWrapper searchWrapper = new SearchWrapper(SearchSpec.Operation.Or);
        searchWrapper.add(new SearchSpec<>(SearchSpec.SearchType.ContainsStringIC, "title", "Shadow"));
        List<Track> result = trackDAO.getByFlexiSearch(searchWrapper);

        System.out.println(searchWrapper);
        System.out.println("Result size: " + result.size());
        result.forEach(System.out::println);

        assertEquals(1, result.size());
    }

    @Test
    public void testFlexiSearchEqualNotEqual() {
        SearchWrapper searchWrapper = new SearchWrapper(SearchSpec.Operation.Or);
        searchWrapper.add(new SearchSpec<>(SearchSpec.SearchType.Equal, "title", "h2-Leave It to Me"));
        List<Track> result = trackDAO.getByFlexiSearch(searchWrapper);

        System.out.println("Result size: " + result.size());
        result.forEach(System.out::println);

        assertEquals(1, result.size());

        searchWrapper.clear();
        searchWrapper.add(new SearchSpec<>(SearchSpec.SearchType.NotEqual, "title", "h2-Leave It to Me"));
        result = trackDAO.getByFlexiSearch(searchWrapper);

        System.out.println(searchWrapper);
        System.out.println("Result size: " + result.size());
        result.forEach(System.out::println);

        assertEquals(5, result.size());
    }

    @Test
    public void testFlexiSearchDuration() {
        Duration duration = Duration.ofMinutes(4);
        SearchSpec<Duration> sw = new SearchSpec<>(SearchSpec.SearchType.Greater,
                "duration", duration);
        SearchWrapper searchWrapper = new SearchWrapper(SearchSpec.Operation.Or, sw);
        List<Track> result = trackDAO.getByFlexiSearch(searchWrapper);

        System.out.println(searchWrapper);
        System.out.println("Result size: " + result.size());
        result.forEach(System.out::println);

        assertEquals(2, result.size());
    }

    @Test
    public void testFlexiSearchDates() {
        LocalDate then = LocalDate.of(1970, 1, 1);
        SearchSpec<LocalDate> ss = new SearchSpec<>(SearchSpec.SearchType.Less,
                "date", then);

        SearchWrapper searchWrapper = new SearchWrapper(SearchSpec.Operation.Or, ss);
        List<Track> result = trackDAO.getByFlexiSearch(searchWrapper);

        System.out.println(searchWrapper);
        System.out.println("Result size: " + result.size());
        result.forEach(System.out::println);

        assertEquals(4, result.size());

        SearchSpec<String> ss2 = new SearchSpec<>(SearchSpec.SearchType.ContainsStringIC,
                "album", "Guitars");

        SearchWrapper sw2 = new SearchWrapper(SearchSpec.Operation.And, ss, ss2);
        result = trackDAO.getByFlexiSearch(sw2);

        System.out.println(sw2);
        System.out.println("Result size: " + result.size());
        result.forEach(System.out::println);

        assertEquals(1, result.size());
    }

    @Test
    public void testSearchForIds() {
        SearchSpec<Integer> ss = new SearchSpec<>(SearchSpec.SearchType.Less,
                "id", 3);

        SearchWrapper sw = new SearchWrapper(SearchSpec.Operation.Or, ss);
        List<Track> result = trackDAO.getByFlexiSearch(sw);

        System.out.println(sw);
        System.out.println("Result size: " + result.size());
        result.forEach(System.out::println);

        assertEquals(2, result.size());
    }

    @Test
    public void testSearchForStringsGreaterThan() {
        SearchSpec<Integer> ss = new SearchSpec<>(SearchSpec.SearchType.Less,
                "id", 3);

        SearchSpec<String> ss2 = new SearchSpec<>(SearchSpec.SearchType.Greater,
                "artist", "C");

        SearchWrapper sw = SearchWrapper.or(ss, ss2);
        List<Track> result = trackDAO.getByFlexiSearch(sw);

        System.out.println(sw);
        result.forEach(System.out::println);

        assertEquals(1, result.size());
    }
}
