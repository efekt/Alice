package it.efekt.alice.db;

import it.efekt.alice.core.Alice;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.model.GuildConfig;
import net.dv8tion.jda.core.entities.Guild;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

public class GuildConfigManager {
    private Logger logger = LoggerFactory.getLogger(GuildConfigManager.class);
    private HashMap<String, GuildConfig> guildConfigs = new HashMap<>();
    private Alice alice;


    public GuildConfigManager(Alice alice){
        this.alice = alice;
        loadAll();
    }

    public GuildConfig getGuildConfig(Guild guild){
        if (!this.guildConfigs.containsKey(guild.getId())){
            GuildConfig config = new GuildConfig(guild.getId(), AliceBootstrap.DEFAULT_PREFIX);
            this.guildConfigs.put(guild.getId(), config);
            return this.guildConfigs.get(guild.getId());
        }

        return this.guildConfigs.get(guild.getId());
    }

    public boolean hasConfig(String guildId){
        return this.guildConfigs.containsKey(guildId);
    }

    public void updateConfig(GuildConfig guildConfig){
        if (hasConfig(guildConfig.getId())){
            this.guildConfigs.put(guildConfig.getId(), guildConfig);
            guildConfig.save();
        }
    }

    public void loadAll(){
        // Load all config files
        Session session = AliceBootstrap.hibernate.getSession();
        session.beginTransaction();
        List<GuildConfig> configs = session.createQuery("from GuildConfig").getResultList();
        configs.forEach(config -> guildConfigs.put(config.getId(), config));
        session.getTransaction().commit();
        logger.info("Loaded all " + this.guildConfigs.size() + " guild configs");
    }

    public void saveAll(){
        logger.info("Saving all guild configs to database...");
        this.guildConfigs.values().forEach(GuildConfig::save);
        logger.info("Saving all guild configs to database... completed.");
    }

}
