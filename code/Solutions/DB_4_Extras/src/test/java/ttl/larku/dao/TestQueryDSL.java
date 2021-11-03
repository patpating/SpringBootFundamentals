package ttl.larku.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ttl.larku.domain.QTrack;
import ttl.larku.domain.Track;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author whynot
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class TestQueryDSL {

    @PersistenceContext
    private EntityManager em;

    @Test
    public void simpleQdsl() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QTrack track = QTrack.track;
        queryFactory.selectFrom(track).stream().forEach(System.out::println);
    }

    @Test
    public void lessSimpleQdsl() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QTrack track = QTrack.track;
        List<Track> gt2Minutes = queryFactory.selectFrom(track).where(track.duration.gt(Duration.ofMinutes(4)))
                .fetch();
//                .stream().collect(toList());

        System.out.println("g2Minutes.size: " + gt2Minutes.size());
        gt2Minutes.forEach(System.out::println);

        assertEquals(2, gt2Minutes.size());
    }


}
