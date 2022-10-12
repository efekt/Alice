package it.efekt.alice.commands.fun;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class ChooseCommand extends Command {
    public ChooseCommand(String alias) {
        super(alias);
        setDescription(AMessage.CMD_CHOOSE_DESC);
        setShortUsageInfo(AMessage.CMD_CHOOSE_SHORT_USAGE_INFO);
        setCategory(CommandCategory.FUN);

        optionData.add(new OptionData(OptionType.STRING, "args", "args", true));
        setSlashCommand();
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        if (getArgs().length == 0){
            return false;
        }

        if (getArgs().length > 5){
            e.sendMessageToChannel(AMessage.CMD_CHOOSE_MSG_MAX.get(e.getGuild()));
            return true;
        }

        int randomInt = ThreadLocalRandom.current().nextInt(0, getArgs().length);
        String output = getArgs()[randomInt];
        MessageCreateBuilder msgBuilder = new MessageCreateBuilder();
        msgBuilder.addContent(String.format("**%s**", output));
        msgBuilder = msgBuilder.setAllowedMentions(Collections.emptyList());
        e.sendMessageToChannel(msgBuilder.build().getContent());

        if (getArgs().length == 1){
            e.sendMessageToChannel(AMessage.CMD_CHOOSE_MSG_1_OPTION_GIVEN.get(e.getGuild()));
        }
        return true;
    }
}
