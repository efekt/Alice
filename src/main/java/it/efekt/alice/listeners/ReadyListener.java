package it.efekt.alice.listeners;


import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public class ReadyListener implements EventListener {

    public void onEvent(Event event) {
        if (event instanceof ReadyEvent){
            System.out.println("Alice is ready...");
        }
    }
}
