package it.efekt.alice.commands.fun;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ChooseCommand extends Command {
    public ChooseCommand(String alias) {
        super(alias);
        setDescription(AMessage.CMD_CHOOSE_DESC);
        setShortUsageInfo(AMessage.CMD_CHOOSE_SHORT_USAGE_INFO);
        setCategory(CommandCategory.FUN);
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        if (getArgs().length == 0){
            return false;
        }

        if (getArgs().length > 5){
            e.getChannel().sendMessage(AMessage.CMD_CHOOSE_MSG_MAX.get(e.getGuild())).complete();
            return true;
        }

        int randomInt = ThreadLocalRandom.current().nextInt(0, getArgs().length);
        String output = getArgs()[randomInt];
        MessageCreateBuilder msgBuilder = new MessageCreateBuilder();
        msgBuilder.addContent(String.format("**%s**", output));
        msgBuilder.addContent(e.getGuild().getName());
        e.getChannel().sendMessage(msgBuilder.build()).complete();

        if (getArgs().length == 1){
            e.getChannel().sendMessage(AMessage.CMD_CHOOSE_MSG_1_OPTION_GIVEN.get(e.getGuild())).completeAfter(2, TimeUnit.SECONDS);
        }
        return true;
    }
}
