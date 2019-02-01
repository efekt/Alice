package it.efekt.alice.commands;

import it.efekt.alice.commands.core.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class StopCmd extends Command {

    public StopCmd(String alias) {
        super(alias);
    }

    @Override
    public void onCommand(MessageReceivedEvent e) {
        Runtime.getRuntime().exit(0);
    }
}
