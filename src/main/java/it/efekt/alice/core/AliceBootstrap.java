package it.efekt.alice.core;

import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AliceBootstrap {
public static Config config;

    public static void main(String[] args) throws Exception{
        Yaml yaml = new Yaml();

        try {
            if (!Files.exists(Paths.get("./config.yml"))){
                InputStream in = AliceBootstrap.class.getClassLoader().getResourceAsStream("config.yml");
                try (OutputStream outputStream = new FileOutputStream(new File("./config.yml"))) {
                    IOUtils.copy(in, outputStream);
                }
            }

            InputStream in = Files.newInputStream(Paths.get("./config.yml"));
            config = yaml.loadAs(in, Config.class);
            System.out.println("Znaleziono token: " + config.getToken());

        } catch (IOException e) {
            e.printStackTrace();
        }

        new Alice();
    }
}