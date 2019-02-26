package it.efekt.alice.commands.fun;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class TomaszCmd extends Command {

    public TomaszCmd(String alias) {
        super(alias);
        setDescription(Message.CMD_TOMASZ_DESC);
        setCategory(CommandCategory.FUN);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        try {
            e.getChannel().sendFile(AliceBootstrap.class.getClassLoader().getResourceAsStream("assets/images/tomasz.jpg"), "tomasz.jpg").queue();
            return true;
        } catch (NullPointerException exc) {
            e.getChannel().sendMessage(Message.FILE_NOT_FOUND.get(e)).queue();
            return true;
        }
    }
}
