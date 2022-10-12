package it.efekt.alice.core;

import net.dv8tion.jda.api.entities.Activity;
import org.discordbots.api.client.DiscordBotListAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotStatusRefresher implements Runnable{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Alice alice;

    public BotStatusRefresher(Alice alice){
        this.alice = alice;
    }

    @Override
    public void run() {
        setPresence();

        if (this.alice.getConfig().getDiscordBotListApiToken() != null){
            updateDiscordBotList();
        }
    }

    private void setPresence(){
        int guilds = this.alice.getShardManager().getGuilds().size();
        int users = this.alice.getShardManager().getUsers().size();
        this.alice.setActivity(Activity.playing(guilds + " servers | " + users + " users" + " | alice-bot.net"));
    }

    private void updateDiscordBotList(){
        DiscordBotListAPI api = new DiscordBotListAPI.Builder()
                .token(AliceBootstrap.alice.getConfig().getDiscordBotListApiToken())
                .botId(AliceBootstrap.alice.getShardManager().getShards().get(0).getSelfUser().getId())
                .build();
        api.setStats(this.alice.getShardManager().getGuilds().size());
    }
}
