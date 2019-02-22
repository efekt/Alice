package it.efekt.alice.listeners;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.GameStats;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.time.temporal.ChronoUnit;

public class GameListener extends ListenerAdapter {

    @Override
    public void onUserUpdateGame(UserUpdateGameEvent e) {
        User user = e.getUser();
        Guild guild = e.getGuild();

        if (e.getOldGame() == null || user.isBot() || e.getNewGame() == null){
            return;
        }
        String gameName = e.getOldGame().getName();
        GameStats gameStats = AliceBootstrap.alice.getGameStatsManager().getGameStats(user, guild, gameName);
        long elapsed = e.getOldGame().getTimestamps().getElapsedTime(ChronoUnit.SECONDS);
        gameStats.addTimePlayed((int) elapsed);
        gameStats.save();

    }
}
