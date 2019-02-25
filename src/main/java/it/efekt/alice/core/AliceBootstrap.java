package it.efekt.alice.core;

import it.efekt.alice.config.Config;
import it.efekt.alice.db.GameStats;
import it.efekt.alice.db.GuildConfig;
import it.efekt.alice.db.UserStats;
import org.apache.commons.io.IOUtils;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
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
public static SessionFactory sessionFactory;
public static final String DEFAULT_PREFIX = "<";


    public static void main(String[] args) {
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
            initSessionFactory(config);
            alice = new Alice(config);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initSessionFactory(Config config){
        Configuration configuration = new Configuration()
                .configure("hibernate.cfg.xml")
                .setProperty("hibernate.connection.url","jdbc:mysql://"+config.getMysqlUrl()+":"+config.getMysqlPort()+"/"+config.getMysqlDatabase()+"?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8")
                .setProperty("hibernate.connection.username", config.getMysqlUser())
                .setProperty("hibernate.connection.password", config.getMysqlPassword())
                .addAnnotatedClass(GuildConfig.class)
                .addAnnotatedClass(UserStats.class)
                .addAnnotatedClass(GameStats.class);
        sessionFactory = configuration.buildSessionFactory();
    }


}