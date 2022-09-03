package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class VoteCmd extends Command {
    public static final String VOTE_URL = "https://top.gg/bot/537011515014774785/vote";
    public VoteCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.UTILS);
        setDescription(AMessage.CMD_VOTE_DESC);
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        e.sendMessageToChannel(AMessage.CMD_VOTE_RESPONSE.get(e)+"\n" + VOTE_URL);
        return true;
    }
}
