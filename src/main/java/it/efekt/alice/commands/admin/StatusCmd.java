package it.efekt.alice.commands.admin;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class StatusCmd extends Command {

    public StatusCmd(String alias) {
        super(alias);
        setIsAdminCommand(true);
        setCategory(CommandCategory.ADMIN);
    }

    @Override
    public void onCommand(MessageReceivedEvent e) {
        String status = String.join(" ", getArgs());
        if (status != null){
            e.getJDA().getPresence().setGame(Game.watching(status));
        } else {
            e.getJDA().getPresence().setGame(Game.watching("Sword Art Online"));
        }
    }
}
