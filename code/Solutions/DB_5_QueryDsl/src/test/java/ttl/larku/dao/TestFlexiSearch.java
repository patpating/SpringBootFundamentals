package ttl.larku.dao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ttl.larku.dao.jpahibernate.SearchSpec;
import ttl.larku.domain.Track;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author whynot
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class TestFlexiSearch {

    @PersistenceContext
    private EntityManager manager;

    @Test
    public void testFlexiSearch() {
        SearchSpec<String> sw = new SearchSpec<String>(SearchSpec.SearchType.ContainsStringIC, "title", "Shadow");
        List<Track> result = getByExampleFlexiSearch(List.of(sw), SearchSpec.Operation.Or);

        System.out.println("Result size: " + result.size());
        System.out.println(result);

        assertEquals(1, result.size());
    }

    @Test
    public void testFlexiSearchEqualNotEqual() {
        SearchSpec<String> sw = new SearchSpec<>(SearchSpec.SearchType.Equal, "title", "h2-Leave It to Me");
        List<Track> result = getByExampleFlexiSearch(List.of(sw), SearchSpec.Operation.Or);

        System.out.println("Result size: " + result.size());
        System.out.println(result);

        assertEquals(1, result.size());

        sw = new SearchSpec<>(SearchSpec.SearchType.NotEqual, "title", "h2-Leave It to Me");
        result = getByExampleFlexiSearch(List.of(sw), SearchSpec.Operation.Or);

        System.out.println("Result size: " + result.size());
        System.out.println(result);

        assertEquals(5, result.size());
    }

    @Test
    public void testFlexiSearchDuration() {
        Duration duration = Duration.ofMinutes(4);
        SearchSpec<Duration> sw = new SearchSpec<>(SearchSpec.SearchType.Greater,
                "duration", duration);
        List<Track> result = getByExampleFlexiSearch(List.of(sw), SearchSpec.Operation.Or);

        System.out.println("Result size: " + result.size());
        System.out.println(result);

        assertEquals(2, result.size());
    }

    @Test
    public void testFlexiSearchDates() {
        LocalDate then = LocalDate.of(1970, 1, 1);
        SearchSpec<LocalDate> sw = new SearchSpec<>(SearchSpec.SearchType.Less,
                "date", then);

        List<Track> result = getByExampleFlexiSearch(List.of(sw), SearchSpec.Operation.Or);

        System.out.println("Result size: " + result.size());
        System.out.println(result);

        assertEquals(4, result.size());

        SearchSpec<String> sw2 = new SearchSpec<>(SearchSpec.SearchType.ContainsStringIC,
                "album", "Guitars");

        result = getByExampleFlexiSearch(List.of(sw, sw2), SearchSpec.Operation.And);

        System.out.println("Result size: " + result.size());
        System.out.println(result);

        assertEquals(1, result.size());
    }

    @Test
    public void testSearchForIds() {
        SearchSpec<Integer> sw = new SearchSpec<>(SearchSpec.SearchType.Less,
                "id", 3);

        List<Track> result = getByExampleFlexiSearch(List.of(sw), SearchSpec.Operation.Or);

        System.out.println("Result size: " + result.size());
        System.out.println(result);

        assertEquals(2, result.size());
    }

    @Test
    public void testSearchForStringsGreaterThan() {
        SearchSpec<Integer> sw = new SearchSpec<>(SearchSpec.SearchType.Less,
                "id", 3);

        SearchSpec<String> sw2 = new SearchSpec<>(SearchSpec.SearchType.Greater,
                "artist", "C");

        List<Track> result = getByExampleFlexiSearch(List.of(sw, sw2), SearchSpec.Operation.And);

        System.out.println("Result size: " + result.size());
        System.out.println(result);

        assertEquals(1, result.size());
    }

    public List<Track> getByExampleFlexiSearch(List<SearchSpec<?>> searchWrappers, SearchSpec.Operation operation) {
        //Get the builder
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        //Create a query that will return Tracks
        CriteriaQuery<Track> cq = builder.createQuery(Track.class);

        //Tracks is also going to be the (only) root entity we will
        //be searching from.  This need not always be the same as the
        //type returned from the query.  This is the 'From' clause.
        Root<Track> queryRoot = cq.from(Track.class);

        //We are going to be selecting Tracks.
        cq.select(queryRoot).distinct(true);

        List<javax.persistence.criteria.Predicate> preds = new ArrayList<>();
        for (SearchSpec<?> searchWrapper : searchWrappers) {
            //All the work is being done in the SearchWrapper class.
            preds.add(searchWrapper.makePredicate(builder, queryRoot));
        }

        Predicate finalPredicate;
        if(operation == SearchSpec.Operation.Or) {
            //Now 'or' them together.
            finalPredicate = builder.or(preds.toArray(new javax.persistence.criteria.Predicate[0]));
        } else {
            finalPredicate = builder.and(preds.toArray(new javax.persistence.criteria.Predicate[0]));
        }

        //And set them as the where clause of our query.
        cq.where(finalPredicate);

        List<Track> result = manager.createQuery(cq).getResultList();

        return result;
    }
}
