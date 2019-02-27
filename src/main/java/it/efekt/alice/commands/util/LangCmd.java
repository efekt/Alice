package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class LangCmd extends Command {

    public LangCmd(String alias) {
        super(alias);
        addPermission(Permission.ADMINISTRATOR);
        setCategory(CommandCategory.UTILS);
        setDescription(Message.CMD_LANG_DESC);
        setShortUsageInfo(Message.CMD_LANG_SHORT_USAGE_INFO);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        return true;
    }
}
