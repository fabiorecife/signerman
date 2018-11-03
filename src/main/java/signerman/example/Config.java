package signerman.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Config {
    private ObjectMapper mapper;
    private JsonNode root;
    private static Config instance;


    private Config() {
        mapper = new ObjectMapper();
        try {
            root = mapper.readTree(Paths.get(System.getProperty("user.home"),
                    ".signerman", "config.env").toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getValue(String name) {
        JsonNode result ;
        if (name.indexOf(".") > 0) {
            result = getValueFromPath(name);
        } else {
            result = getValueFromRoot(name);
        }
        return result == null ? null : result.asText();
    }

    private JsonNode getValueFromPath(String name) {
        String[] names = name.split("[.]");
        JsonNode result = getValueFromRoot(names[0]);
        for (int i = 1; i < names.length; i++) {
            result = result.get(names[i]);
        }
        return result;
    }

    private JsonNode getValueFromRoot(String name) {
        return this.root.get(name);
    }

    public static Config getConfig() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }
}
