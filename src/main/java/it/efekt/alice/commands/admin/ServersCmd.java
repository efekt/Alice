package it.efekt.alice.commands.admin;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ServersCmd extends Command {
    private Logger logger = LoggerFactory.getLogger(ServersCmd.class);

    public ServersCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.BOT_ADMIN);
        setIsAdminCommand(true);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        List<Guild> guilds = new ArrayList<>();
        guilds.addAll(e.getJDA().getGuilds());
        guilds.sort(Comparator.comparing((Guild item) -> item.getMembers().size()).reversed());

        for (Guild guild : guilds){
            logger.info(guild.getName() + " : " + guild.getMembers().size());
        }

        e.getTextChannel().sendMessage("List printed out into console.");

        return true;
    }
}
