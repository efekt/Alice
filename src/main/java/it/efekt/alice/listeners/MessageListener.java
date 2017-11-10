package it.efekt.alice.listeners;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if (e.getMessage().getContent().startsWith("!ping")){
            e.getChannel().sendMessage("Pong: " + e.getJDA().getPing() + "ms").queue();
        }

    }
}
