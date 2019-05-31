package it.efekt.alice.listeners;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadyListener implements EventListener {
    private Logger logger = LoggerFactory.getLogger(ReadyListener.class);

    @Override
    public void onEvent(Event event) {
        if (event instanceof ReadyEvent){
            logger.info("Alice bot has been loaded completely.\n Started listening...");
        }
    }
}
