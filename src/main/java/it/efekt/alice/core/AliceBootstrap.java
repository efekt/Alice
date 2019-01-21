package it.efekt.alice.core;

import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AliceBootstrap {
public static Alice alice;

    public static void main(String[] args){
        init();
    }

    // Checks if there is config.yml file in the directory of .jar
    // if not, it creates a default config file
    // if correct token is provided, proceed to starting the bot
    private static void init(){
        Yaml yaml = new Yaml();
        Config config;

        try {
            System.out.println("Looking for config.yml file...");
            if (!Files.exists(Paths.get("./config.yml"))){
                InputStream in = AliceBootstrap.class.getClassLoader().getResourceAsStream("config.yml"); //todo change this to config-default.yml before releasing
                try (OutputStream outputStream = new FileOutputStream(new File("./config.yml"))) {
                    IOUtils.copy(in, outputStream);
                }
            }
            System.out.println("Loading config file...");
            InputStream in = Files.newInputStream(Paths.get("./config.yml"));
            config = yaml.loadAs(in, Config.class);
            System.out.println("Config loaded: \n" + config.toString());

            alice = new Alice(config);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}