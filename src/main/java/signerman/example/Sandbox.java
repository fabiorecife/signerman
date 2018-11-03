package signerman.example;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Sandbox {
    public static void main(String[] args) throws IOException {
        System.out.println(new File(".").getAbsolutePath());
        ObjectMapper mapper = new ObjectMapper(); // create once, reuse
        JsonNode root = mapper.readTree(Paths.get(System.getProperty("user.home"),
                ".signerman", "config.env").toFile());
        System.out.println(root.get("pfx").get("path").asText());
        System.out.println(root.get("pfx").get("password").asText());
        String[] names = "teste.nome".split("[.]");
        System.out.println(names[0]);
        System.out.println(names[1]);
    }
}
