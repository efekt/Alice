package it.efekt.alice.listeners;

import net.dv8tion.jda.api.events.ExceptionEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ReadyListener extends ListenerAdapter {
    private Logger logger = LoggerFactory.getLogger(ReadyListener.class);

    @Override
    public void onReady(ReadyEvent event) {
            logger.info("Alice bot has been loaded completely.\n Started listening...");
    }

    @Override
    public void onException(ExceptionEvent event){
        event.getCause().printStackTrace();
    }
}
