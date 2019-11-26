package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PingCmd extends Command {

    public PingCmd(String alias){
        super(alias);
        setCategory(CommandCategory.UTILS);
        setDescription(AMessage.CMD_PING_DESC);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        e.getChannel().sendMessage(AMessage.CMD_PING_RESPONSE.get(e, String.valueOf(e.getJDA().getGatewayPing()))).complete();
        return true;
    }
}
