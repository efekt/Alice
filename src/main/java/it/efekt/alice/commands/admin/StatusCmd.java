package it.efekt.alice.commands.admin;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class StatusCmd extends Command {

    public StatusCmd(String alias) {
        super(alias);
        setIsAdminCommand(true);
        setCategory(CommandCategory.BOT_ADMIN);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        String status = String.join(" ", getArgs());
        if (!status.isEmpty()){
            e.getJDA().getPresence().setGame(Game.listening(status));
            return true;
        } else {
            int guilds = e.getJDA().getGuilds().size();
            int users = e.getJDA().getUsers().size();

            e.getJDA().getPresence().setGame(Game.listening(users + " users on " + guilds + " servers"));
            return true;
        }
    }
}
