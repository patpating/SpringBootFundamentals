package ttl.larku.service.props;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ttl.larku.service.props.AtValueDemoService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AtValueDemoServiceTest {

    @Autowired
    private AtValueDemoService avds;

    @Test
    public void testAtValuesSet() {
        String profile = avds.getProfiles();
        boolean isDev = avds.isDevelopment();

        String host = avds.getHost();

        String configName = avds.getConfigName();

        System.out.println("profile: " + profile + ", isDev: " + isDev + ", host: " + host + ", cf: " + configName);

        assertTrue(isDev);
        assertEquals("xyz.com", host);
    }
}
