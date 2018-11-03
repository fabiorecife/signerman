package signerman;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import signerman.example.Config;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigTest {
    private static final Logger logger = LoggerFactory.getLogger(ConfigTest.class);

    @Test
    public void testGetValue() {
        logger.info("teste");
        String nome = Config.getConfig().getValue("test.name");
        assertEquals("mytest",nome);
    }
}
