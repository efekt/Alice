package it.efekt.alice.core;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AliceBootstrap {

public static Alice alice;
public final static Logger logger = LoggerFactory.getLogger(AliceBootstrap.class);
public static final int EMBED_COLOR = 15648332;
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
            logger.info("Looking for config.yml file...");
            if (!Files.exists(Paths.get("./config.yml"))){
                InputStream in = AliceBootstrap.class.getClassLoader().getResourceAsStream("config.yml"); //todo change this to config-default.yml before releasing
                try (OutputStream outputStream = new FileOutputStream(new File("./config.yml"))) {
                    logger.error("Didn't find config.yml file, copying default one...");
                    IOUtils.copy(in, outputStream);
                } catch (NullPointerException exc){
                    exc.printStackTrace();
                }
            }
            logger.info("Loading config file...");
            InputStream in = Files.newInputStream(Paths.get("./config.yml"));
            config = yaml.loadAs(in, Config.class);
            logger.info("Config loaded: \n" + config.toString());

            alice = new Alice(config);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}