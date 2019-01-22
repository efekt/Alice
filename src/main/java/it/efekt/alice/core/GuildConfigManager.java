package it.efekt.alice.core;

import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;

public class GuildConfigManager {
    private HashMap<Guild, GuildConfig> guildConfigs = new HashMap<>();
    private Alice alice;


    public GuildConfigManager(Alice alice){
        this.alice = alice;
    }

    public GuildConfig getGuildConfig(Guild guild){
        if (!this.guildConfigs.containsKey(guild)){
            this.guildConfigs.put(guild, new GuildConfig(guild.getId()));
            return this.guildConfigs.get(guild);
        }
        return this.guildConfigs.get(guild);
    }

    public void loadAll(){
        // Load all config files
    }

    public void saveAll(){
        // save all config files
    }

}
