package ttl.larku;

import ttl.larku.dao.BaseDAO;
import ttl.larku.dao.inmemory.InMemoryTrackDAO;
import ttl.larku.domain.Track;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TrackerTestDataConfig {

    public Track track1() {
        Track track = Track.title("The Shadow Of Your Smile").artist("Big John Patton")
                .album("Let 'em Roll").duration("06:15").date("1965-01-02").build();
        return track;
    }

    public Track track2() {
        Track track = Track.title("I'll Remember April").artist("Jim Hall and Ron Carter")
                .album("Alone Together").duration("05:54").date("1972-01-02").build();
        return track;
    }

    public Track track3() {
        Track track = Track.title("What's New").artist("John Coltrane")
                .album("Ballads").duration("03:47").build();
        return track;
    }


    /**
     * These tracks do NOT have id's yet
     *
     * @return
     */
    public List<Track> tracks() {
        List<Track> tracks = new ArrayList<>();
        tracks.add(Track.title("The Shadow Of Your Smile").artist("Big John Patton")
                .album("Let 'em Roll").duration("06:15").date("1965-01-02").build());
        tracks.add(Track.title("I'll Remember April").artist("Jim Hall and Ron Carter")
                .album("Alone Together").duration("05:54").date("1972-01-02").build());
        tracks.add(Track.title("What's New").artist("John Coltrane")
                .album("Ballads").duration("03:47").build());
        tracks.add(Track.title("Leave It to Me").artist("Herb Ellis")
                .album("Three Guitars in Bossa Nova Time").duration("03:13").date("1963-01-02").build());

        tracks.add(Track.title("Have you met Miss Jones").artist("George Van Eps")
                .album("Pioneers of the Electric Guitar").duration("02:18").date("2013-01-02").build());

        tracks.add(Track.title("My Funny Valentine").artist("Johnny Smith")
                .album("Moonlight in Vermont").duration("02:48").build());

        return tracks;
    }

    private Date convertToDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);

        return cal.getTime();
    }

    public BaseDAO<Track> trackDAOWithInitData() {
        InMemoryTrackDAO dao = new InMemoryTrackDAO();
        dao.createStore();

        //tracks().forEach(t -> dao.create(t));
        tracks().forEach(dao::create);

        return dao;
    }
}
