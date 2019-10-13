package it.efekt.alice.listeners;

import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.api.events.ExceptionEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ReadyListener extends ListenerAdapter {
    private Logger logger = LoggerFactory.getLogger(ReadyListener.class);

    @Override
    public void onReady(ReadyEvent event) {
        int shardId = event.getJDA().getShardInfo().getShardId();
        int totalShards = event.getJDA().getShardInfo().getShardTotal();


        logger.info("Loaded shard id: " + shardId + ", loaded shards: " + (shardId + 1) + "/" + totalShards);
        logger.info("Guilds in shard id " + shardId + ": " + event.getGuildTotalCount());
            if (shardId + 1 == totalShards){
                logger.info("All shards has been fully loaded!");
                AliceBootstrap.alice.startSchedulers();
                // Wait for all shards to load before registering commands and listeners
                AliceBootstrap.alice.registerCommands();
                logger.info("Registering commands...");
                AliceBootstrap.alice.registerListeners();
                logger.info("Registered " + AliceBootstrap.alice.getCmdManager().getCommands().size() + " commands");
                logger.info("Alice bot has been fully loaded, started listening...");
            }
    }

    @Override
    public void onException(ExceptionEvent event){
        event.getCause().printStackTrace();
    }
}
