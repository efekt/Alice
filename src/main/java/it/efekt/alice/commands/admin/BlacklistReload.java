package it.efekt.alice.commands.admin;

import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.commands.games.GameStatsCmd;
import it.efekt.alice.core.AliceBootstrap;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.FileNotFoundException;

public class BlacklistReload extends Command {
    public BlacklistReload(String alias) {
        super(alias);
        setIsAdminCommand(true);
        setCategory(CommandCategory.BOT_ADMIN);
    }

    @Override
    public boolean onCommand(MessageReceivedEvent e) {
        if (getArgs().length == 1){
            if (getArgs()[0].equalsIgnoreCase("reload")){
                GameStatsCmd gameStatsCmd = (GameStatsCmd) AliceBootstrap.alice.getCmdManager().getCommand("topgames");
                try {
                    gameStatsCmd.loadBlacklist();
                    e.getTextChannel().sendMessage("Games blacklist reloaded.").complete();
                } catch (FileNotFoundException e1) {
                    e.getTextChannel().sendMessage("Couldn't find game blacklist file.").complete();
                }
                return true;
            }
        }
        return false;
    }
}
