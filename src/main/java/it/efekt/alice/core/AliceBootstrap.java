package it.efekt.alice.core;

import it.efekt.alice.commands.analytics.AliceAnalytics;
import it.efekt.alice.db.HibernateHandler;
import it.efekt.alice.config.Config;
import it.efekt.alice.web.WebServer;
import net.dv8tion.jda.api.requests.RestAction;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class AliceBootstrap {

    public final static Logger logger = LoggerFactory.getLogger(AliceBootstrap.class);
    public static Alice alice;
    public static final int EMBED_COLOR = 15648332;
    public static final String DEFAULT_PREFIX = "<";
    public static final long STARTUP_TIME = System.currentTimeMillis();
    public static HibernateHandler hibernate;
    public static AliceAnalytics analytics;

    public static void main(String[] args) {
        long beforeTime = System.currentTimeMillis();
        init();
        long totalTime = System.currentTimeMillis() - beforeTime;
        logger.info("Alice loaded in " + TimeUnit.MILLISECONDS.toSeconds(totalTime) + " seconds ("+totalTime+"ms)");
        new WebServer();
        RestAction.setPassContext(true);
        RestAction.setDefaultFailure(Throwable::printStackTrace);
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