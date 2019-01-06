package it.efekt.alice.listeners;

import net.dv8tion.jda.core.events.DisconnectEvent;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class JoinQuitListener extends ListenerAdapter {

    @Override
    public void onShutdown(ShutdownEvent e){
        System.out.println("Bye bye!");

    }

    @Override
    public void onDisconnect(DisconnectEvent e){
        System.out.println("Byee!");
    }
}


