package it.efekt.alice.commands.util;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.AMessage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class HistoryDeletionCmd extends Command {
    public HistoryDeletionCmd(String alias) {
        super(alias);
        addPermission(Permission.ADMINISTRATOR);
        setCategory(CommandCategory.DISCORD_ADMIN_UTILS);
        setDescription(AMessage.CMD_HISTORYDEL_DESC);
        setShortUsageInfo(AMessage.CMD_HISTORYDEL_SHORT_USAGE_INFO);

        optionData.add(new OptionData(OptionType.INTEGER, "amount", "amount", true));
        setSlashCommand();
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        if (getArgs().length == 1){
            // Checks if String is a valid integer
            //-?    negative sign, could have none or one
            //\\d+  one or more digits
            if (getArgs()[0].matches("-?\\d+")) {
                int toRemove = Integer.parseInt(getArgs()[0]);
                if (toRemove <= 99){
                    removeLast(e.getChannel(), toRemove);
                    e.sendSlashConfirmation(AMessage.CMD_HISTORYDEL_DESC.get(e)); //add specific message and fix it
                    return true;
                } else {
                    e.sendMessageToChannel(AMessage.CMD_HISTORYDEL_RANGE.get(e));
                    return true;
                }
            }
        }
        return false;
    }

    private void removeLast(MessageChannel channel, int amount){
        // +1, including our command
        channel.purgeMessages(channel.getHistory().retrievePast(amount + 1).complete());
    }
}
