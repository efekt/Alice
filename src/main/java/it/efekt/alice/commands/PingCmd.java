package it.efekt.alice.commands;

import it.efekt.alice.core.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PingCmd extends Command {

    public PingCmd(String alias){
        super(alias);
        this.setDesc("Sprawdza jak szybko Alice jest w stanie biec");
    }

    public void onCommand(MessageReceivedEvent e) {
        e.getChannel().sendMessage("Pong: " + e.getJDA().getPing() + "ms").queue();
    }
}
