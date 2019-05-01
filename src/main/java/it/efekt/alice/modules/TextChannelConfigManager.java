package it.efekt.alice.modules;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.TextChannelConfig;
import net.dv8tion.jda.core.entities.TextChannel;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public class TextChannelConfigManager {
    private Logger logger = LoggerFactory.getLogger(TextChannelConfigManager.class);
    private List<TextChannelConfig> textChannelConfigs = new ArrayList<>();

    public TextChannelConfigManager(){
        loadAll();
    }

    public void loadAll(){
        logger.info("Loading text channel configs...");
        Session session = AliceBootstrap.hibernate.getSession();
        session.beginTransaction();
        this.textChannelConfigs.addAll(session.createQuery("from TextChannelConfig").getResultList());
        session.getTransaction().commit();
        logger.info("Loaded all " + this.textChannelConfigs.size() + " text channel configs");
    }

    public void saveAll(){
        this.textChannelConfigs.forEach(TextChannelConfig::save);
    }

    public TextChannelConfig get(TextChannel textChannel){
        for (TextChannelConfig config : this.textChannelConfigs){
            if (config.getChannelId().equalsIgnoreCase(textChannel.getId())){
                return config;
            }
        }
        TextChannelConfig textChannelConfig = new TextChannelConfig(textChannel.getId());
        this.textChannelConfigs.add(textChannelConfig);
        return textChannelConfig;
    }

}
