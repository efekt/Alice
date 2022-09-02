package it.efekt.alice.commands.admin;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class StatusCmd extends Command {

    public StatusCmd(String alias) {
        super(alias);
        setIsAdminCommand(true);
        setCategory(CommandCategory.BOT_ADMIN);
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        String status = String.join(" ", getArgs());
        if (!status.isEmpty()){
            e.getJDA().getPresence().setActivity(Activity.listening(status));
            return true;
        } else {
            int guilds = e.getJDA().getGuilds().size();
            int users = e.getJDA().getUsers().size();

            e.getJDA().getPresence().setActivity(Activity.listening(users + " users on " + guilds + " servers"));
            return true;
        }
    }
}
