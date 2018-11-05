package signerman.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import signerman.example.Config;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SignerTest {

    Signer signer ;



    @Test
    public void testInit() {
        signer = new Signer();
        signer.setContentFile(Paths.get(System.getProperty("user.home"), "Downloads", "teste", "listagem_logs.pdf"));
        signer.setKeyStoreParams(Config.getConfig().getValue("pfx.path"), Config.getConfig().getValue("pfx.password"));

        assertTrue(signer.getContentFile().toFile().exists());
        assertEquals(Config.getConfig().getValue("pfx.path"), signer.getKeyStorePath());
        assertEquals(Config.getConfig().getValue("pfx.password"), signer.getKeyStorePassword());


    }

    @Test
    public void testLoadKeyStore() {
        testInit();
        signer.loadKeyStore();
        assertNotNull(signer.getKeyStore());
    }

    @Test
    public void testBuilPathp7s() {
        testInit();
        Path p7s = signer.buildPathP7s();
        System.out.println(p7s.toFile().getAbsolutePath());
        assertTrue(p7s.toFile().getAbsolutePath().endsWith("p7s"));

    }

    @Test
    public void sign() {
        testInit();
        signer.sign();
        File file = Paths.get(System.getProperty("user.home"), "Downloads", "teste", "listagem_logs.pdf").toFile();
        assertTrue(file.exists());
    }
}
