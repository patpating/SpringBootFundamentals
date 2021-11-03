package ttl.larku.dao;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.Transactional;
import ttl.larku.dao.jpahibernate.JPATrackDAO;
import ttl.larku.domain.Track;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class JPATrackDAOTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private JPATrackDAO trackDAO;
    //private BaseDAO<Track> trackDAO;

    private Track track1, track2;
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

    @BeforeEach
    public void setup() {
//        trackDAO.createStore();
//
//        track1 = Track.title("The Shadow Of Your Smile").artist("Big John Patton")
//                .album("Let 'em Roll").duration("06:15").date("1965-02-03").build();
//        track2 = Track.title("I'll Remember April").artist("Jim Hall and Ron Carter")
//                .album("Alone Together").duration("05:54").date("1972-02-03").build();
//
//        trackDAO.create(track1);
//        trackDAO.create(track2);

        System.err.println(context.getBeanDefinitionCount() + " beans");
    }


    @Test
    public void testGetAll() {
        List<Track> tracks = trackDAO.getAll();
        assertTrue(tracks.size() > 0);
    }

    @Test
    public void testCreate() {
        Track track = Track.title("What's New").artist("John Coltrane")
                .album("Ballads").duration("03:47").build();

        int newId = trackDAO.create(track).getId();

        Track result = trackDAO.get(newId);

        assertEquals(newId, result.getId());
    }

    @Test
    public void testUpdate() {
        Track track = Track.title("What's New").artist("John Coltrane")
                .album("Ballads").duration("03:47").build();
        int newId = trackDAO.create(track).getId();

        Track result = trackDAO.get(newId);

        assertEquals(newId, result.getId());

        result.setTitle("Who do");
        trackDAO.update(result);

        result = trackDAO.get(result.getId());
        assertEquals("Who do", result.getTitle());
    }

    @Test
    @Transactional
    public void testDelete() {
        Track track = Track.title("What's New").artist("John Coltrane")
                .album("Ballads").duration("03:47").build();
        int newId = trackDAO.create(track).getId();

        Track result = trackDAO.get(newId);

        assertEquals(newId, result.getId());

        int beforeSize = trackDAO.getAll().size();

        trackDAO.delete(result);

        result = trackDAO.get(result.getId());

        assertEquals(beforeSize - 1, trackDAO.getAll().size());
        assertNull(result);

    }

    @Test
    public void testsGetByExample() {
        Track track = Track.album("Bossa").artist("Hall")
                .title("Shadow").build();

        List<Track> result = trackDAO.getByExample(track);

        System.out.println("Result:");
        result.forEach(System.out::println);

        assertEquals(3, result.size());

    }
}
