package ttl.larku.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import ttl.larku.dao.jpahibernate.SearchSpec;
import ttl.larku.dao.jpahibernate.SearchWrapper;
import ttl.larku.domain.Track;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class TrackServiceFlexiSearchTest {

    @Autowired
    ApplicationContext context;
    @Autowired
    private TrackService trackService;

    @Test
    public void testFlexiSearch() {
        SearchSpec<String> ss = new SearchSpec<String>(SearchSpec.SearchType.ContainsStringIC, "title", "Shadow");
        SearchWrapper sw = SearchWrapper.or(ss);
        List<Track> result = trackService.getTracksWithFlexiSearch(sw);

        System.out.println(sw);
        System.out.println("Result size: " + result.size());
        result.forEach(System.out::println);

        assertEquals(1, result.size());
    }

    @Test
    public void testFlexiSearchEqualNotEqual() {
        SearchSpec<String> ss = new SearchSpec<>(SearchSpec.SearchType.Equal, "title", "h2-Leave It to Me");
        SearchWrapper sw = SearchWrapper.or(ss);
        List<Track> result = trackService.getTracksWithFlexiSearch(sw);

        System.out.println(sw);
        System.out.println("Result size: " + result.size());
        result.forEach(System.out::println);

        assertEquals(1, result.size());

        ss = new SearchSpec<>(SearchSpec.SearchType.NotEqual, "title", "h2-Leave It to Me");
        sw = SearchWrapper.or(ss);
        result = trackService.getTracksWithFlexiSearch(sw);

        System.out.println(sw);
        System.out.println("Result size: " + result.size());
        result.forEach(System.out::println);

        assertEquals(5, result.size());
    }

    @Test
    public void testFlexiSearchDuration() {
        Duration duration = Duration.ofMinutes(4);
        SearchSpec<Duration> ss = new SearchSpec<>(SearchSpec.SearchType.Greater,
                "duration", duration);
        SearchWrapper sw = SearchWrapper.or(ss);
        List<Track> result = trackService.getTracksWithFlexiSearch(sw);

        System.out.println(sw);
        System.out.println("Result size: " + result.size());
        result.forEach(System.out::println);

        assertEquals(2, result.size());
    }

    @Test
    public void testFlexiSearchDates() {
        LocalDate then = LocalDate.of(1970, 1, 1);
        SearchSpec<LocalDate> ss = new SearchSpec<>(SearchSpec.SearchType.Less,
                "date", then);

        SearchWrapper sw = SearchWrapper.or(ss);
        List<Track> result = trackService.getTracksWithFlexiSearch(sw);

        System.out.println(sw);
        System.out.println("Result size: " + result.size());
        result.forEach(System.out::println);

        assertEquals(4, result.size());

        SearchSpec<String> ss2 = new SearchSpec<>(SearchSpec.SearchType.ContainsStringIC,
                "album", "Guitars");

        sw = SearchWrapper.and(ss, ss2);
        result = trackService.getTracksWithFlexiSearch(sw);

        System.out.println(sw);
        System.out.println("Result size: " + result.size());
        result.forEach(System.out::println);

        assertEquals(1, result.size());
    }

    @Test
    public void testSearchForIds() {
        SearchSpec<Integer> ss = new SearchSpec<>(SearchSpec.SearchType.Less,
                "id", 3);

        SearchWrapper sw = SearchWrapper.or(ss);
        List<Track> result = trackService.getTracksWithFlexiSearch(sw);

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

        SearchWrapper sw = SearchWrapper.and(ss, ss2);
        List<Track> result = trackService.getTracksWithFlexiSearch(sw);

        System.out.println(sw);
        System.out.println("Result size: " + result.size());
        result.forEach(System.out::println);

        assertEquals(1, result.size());
    }
}