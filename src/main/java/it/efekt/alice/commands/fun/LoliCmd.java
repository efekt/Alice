package it.efekt.alice.commands.fun;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LoliCmd extends Command {
    public LoliCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.FUN);
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        e.getChannel().sendMessage("FBI OPEN UP").complete();
        return true;
    }
}
