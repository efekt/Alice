package it.efekt.alice.commands.core;

import it.efekt.alice.config.Config;
import it.efekt.alice.db.model.GameStats;
import it.efekt.alice.db.model.GuildConfig;
import it.efekt.alice.db.model.TextChannelConfig;
import it.efekt.alice.db.model.UserStats;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateHandler {
    private SessionFactory sessionFactory;

    public HibernateHandler(Config config){
        initSessionFactory(config);
    }

    public Session getSession(){
        return this.sessionFactory.getCurrentSession();
    }

    public SessionFactory getSessionFactory(){
        return this.sessionFactory;
    }

    private void initSessionFactory(Config config){
        Configuration configuration = new Configuration()
                .configure("hibernate.cfg.xml")
                .setProperty("hibernate.connection.url","jdbc:mysql://"+config.getMysqlUrl()+":"+config.getMysqlPort()+"/"+config.getMysqlDatabase()+"?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8")
                .setProperty("hibernate.connection.username", config.getMysqlUser())
                .setProperty("hibernate.connection.password", config.getMysqlPassword())
                .addAnnotatedClass(GuildConfig.class)
                .addAnnotatedClass(UserStats.class)
                .addAnnotatedClass(GameStats.class)
                .addAnnotatedClass(TextChannelConfig.class);
        sessionFactory = configuration.buildSessionFactory();
    }
}
