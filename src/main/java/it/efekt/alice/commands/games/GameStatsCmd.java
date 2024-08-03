package it.efekt.alice.commands.games;

import it.efekt.alice.commands.core.CombinedCommandEvent;
import it.efekt.alice.commands.core.Command;
import it.efekt.alice.commands.core.CommandCategory;
import it.efekt.alice.lang.AMessage;

public class GameStatsCmd extends Command {
    public GameStatsCmd(String alias) {
        super(alias);
        setCategory(CommandCategory.GAMES);
        setDescription(AMessage.CMD_GAMESTATS_DESC);
        setSlashCommand();
    }

    @Override
    public boolean onCommand(CombinedCommandEvent e) {
        e.sendMessageToChannel("This feature is scheduled for removal and is no longer active.");
        return true;
    }
}
