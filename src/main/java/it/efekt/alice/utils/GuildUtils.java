package it.efekt.alice.utils;

import it.efekt.alice.core.AliceBootstrap;

public class GuildUtils {

    public static boolean exists(String guildId){
        return AliceBootstrap.alice.getShardManager().getGuildById(guildId) != null;
    }

}
