package it.efekt.alice.commands;

import it.efekt.alice.commands.core.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HistoryDeletionCmd extends Command {
    public HistoryDeletionCmd(String alias) {
        super(alias);
        addPermission(Permission.ADMINISTRATOR);
        setDescription("Usuwa n ostatnich wiadomości na kanale");
        setUsageInfo("<liczba wiadomości>");
    }

    @Override
    public void onCommand(MessageReceivedEvent e) {
        if (getArgs().length == 1){
            // Checks if String is a valid integer
            //-?    negative sign, could have none or one
            //\\d+  one or more digits
            if (getArgs()[0].matches("-?\\d+")) {
                removeLast(e.getTextChannel(), Integer.parseInt(getArgs()[0]));
            }
        e.getChannel().purgeMessages();
        }
    }

    private void removeLast(MessageChannel channel, int amount){
        // +1, including our command
        channel.purgeMessages(channel.getHistory().retrievePast(amount + 1).complete());
    }
}
