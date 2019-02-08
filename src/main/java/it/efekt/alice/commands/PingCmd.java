package it.efekt.alice.commands;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PingCmd extends Command {

    public PingCmd(String alias){
        super(alias);
        setCategory(CommandCategory.UTILS);
        setDescription("Sprawdza jak szybko Alice jest w stanie biec");
    }

    @Override
    public void onCommand(MessageReceivedEvent e) {
        e.getChannel().sendMessage("Pong: " + e.getJDA().getPing() + "ms").queue();
    }
}
