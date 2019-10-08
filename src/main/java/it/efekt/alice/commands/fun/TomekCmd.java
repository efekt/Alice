package it.efekt.alice.commands.fun;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class TomekCmd extends Command {

    public TomekCmd(String alias) {
        super(alias);
        setDescription(Message.CMD_TOMEK_DESC);
        setCategory(CommandCategory.FUN);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        try {
            e.getChannel().sendFile(AliceBootstrap.class.getClassLoader().getResourceAsStream("assets/images/tomek.png"), "tomek.png").complete();
            return true;
        } catch (NullPointerException exc) {
            e.getChannel().sendMessage(Message.FILE_NOT_FOUND.get(e)).complete();
            return true;
        }
    }
}
