package ttl.larku.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.time.Duration;
import java.time.LocalDate;

@Entity
@NamedQuery(name = "Track.getByTitle", query = "select t from Track t where t.title like :title")
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String artist;
    private String album;
    private Duration duration;
    private LocalDate date;

    public Track() {
        super();
    }

    public Track(String title, String artist, String album, Duration durStr, LocalDate dateStr) {
        init(title, artist, album, durStr, dateStr);
    }

    public void init(String title, String artist, String album, Duration duration, LocalDate date) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.date = date;
        this.duration = duration;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Track [id=" + id + ", title=" + title + ", artist=" + artist + ", album=" + album + ", duration="
                + duration + ", date=" + date + "]";
    }

    public static Builder id(int arg) {
        return new Builder().id(arg);
    }

    public static Builder title(String arg) {
        return new Builder().title(arg);
    }

    public static Builder album(String arg) {
        return new Builder().album(arg);
    }

    public static Builder artist(String arg) {
        return new Builder().artist(arg);
    }

    public static Builder duration(Duration arg) {
        return new Builder().duration(arg);
    }

    public static Builder date(LocalDate arg) {
        return new Builder().date(arg);
    }

    public static Builder date(String arg) {
        return new Builder().date(arg);
    }


    /**
     * Make us a Builder
     */
    public static class Builder {
        private int id;
        private String title;
        private String artist;
        private String album;
        private Duration duration;
        private LocalDate date;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder artist(String artist) {
            this.artist = artist;
            return this;
        }

        public Builder album(String album) {
            this.album = album;
            return this;
        }

        public Builder duration(Duration duration) {
            this.duration = duration;
            return this;
        }

        public Builder duration(String durString) {
            //tmp hack which wants the string as MM:SS
            this.duration = toDuration(durString);
            return this;
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder date(String date) {
            this.date = LocalDate.parse(date);
            return this;
        }

        public Track build() {
            Track t = new Track(title, artist, album, duration, date);
            return t;
        }

        /**
         * Convert HH:MM:SS into duration.  Anything else throws
         * exception for now
         *
         * @param durString
         * @return
         */
        public static Duration toDuration(String durString) {
            long totalSeconds = 0L;
            String[] arr = durString.split(":");
            if (arr.length == 3) {
                long hours = Integer.parseInt(arr[0]);
                long min = Integer.parseInt(arr[1]);
                totalSeconds = (hours * 60 * 60) + (min * 60) + Integer.parseInt(arr[2]);
            } else if (arr.length == 2) {
                long min = Integer.parseInt(arr[0]);
                totalSeconds = min * 60 + Integer.parseInt(arr[1]);
            } else {
                totalSeconds = Integer.parseInt(arr[0]);
            }
            Duration duration = Duration.ofSeconds(totalSeconds);
            return duration;
        }
    }

}
