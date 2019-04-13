package it.efekt.alice.core;

import it.efekt.alice.commands.analytics.AliceAnalytics;
import it.efekt.alice.commands.core.HibernateHandler;
import it.efekt.alice.config.Config;
import it.efekt.alice.web.AliceWebServer;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AliceBootstrap {

public final static Logger logger = LoggerFactory.getLogger(AliceBootstrap.class);
public static Alice alice;
public static final int EMBED_COLOR = 15648332;
public static final String DEFAULT_PREFIX = "<";
public static final String ICON_URL = "https://images-ext-2.discordapp.net/external/YZ0U9nMuSvG1cb1raXhrkw8Ut8ZBVQT4ia-alVadE7E/https/i.imgur.com/qZe2WZz.jpg";
public static HibernateHandler hibernate;
public static AliceAnalytics analytics;
public static AliceWebServer webServer;


    public static void main(String[] args) {
        webServer = new AliceWebServer();

        //init();
    }

    private static void init(){
        try {
            Config config = initializeConfig();
            hibernate = new HibernateHandler(config);
            analytics = new AliceAnalytics(config);
            alice = new Alice(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Checks if there is config.yml file in the directory of .jar
    // if not, creates a default config file
    // if correct token is provided, proceed to starting the bot
    private static Config initializeConfig() throws IOException{
        logger.info("Looking for config.yml file...");
        if (!Files.exists(Paths.get("./config.yml"))){
            InputStream in = AliceBootstrap.class.getClassLoader().getResourceAsStream("config-default.yml");
            try (OutputStream outputStream = new FileOutputStream(new File("./config.yml"))) {
                logger.warn("Didn't find config.yml file, copying default one...");
                IOUtils.copy(in, outputStream);
            } catch (NullPointerException exc){
                exc.printStackTrace();
            }
        }
        logger.info("Loading config file...");
        InputStream in = Files.newInputStream(Paths.get("./config.yml"));
        Config config = new Yaml().loadAs(in, Config.class);
        logger.info("Config loaded: \n" + config.toString());
        return config;
    }
}