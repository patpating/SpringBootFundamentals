package ttl.larku.domain;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author whynot
 */
public class TestParseDurations {

    @Test
    public void durationToStringAndBack() {
        String twoM20S = "PT02M20S";

        Duration twoM20SD = Duration.parse(twoM20S);
        System.out.println("should be 140 seconds: " + twoM20SD.toSeconds());
        assertEquals(140, twoM20SD.toSeconds());
    }
}
