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
        int guilds = this.alice.getJDA().getGuilds().size();
        int users = this.alice.getJDA().getUsers().size();
        this.alice.getJDA().getPresence().setActivity(Activity.listening(users + " users on " + guilds + " servers"));
    }

    private void updateDiscordBotList(){
        DiscordBotListAPI api = new DiscordBotListAPI.Builder()
                .token(AliceBootstrap.alice.getConfig().getDiscordBotListApiToken())
                .botId(AliceBootstrap.alice.getJDA().getSelfUser().getId())
                .build();
        api.setStats(this.alice.getJDA().getGuilds().size());
    }
}
