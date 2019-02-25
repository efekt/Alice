package it.efekt.alice.listeners;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.GameStats;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;

public class GameListener extends ListenerAdapter {
    private Logger logger = LoggerFactory.getLogger(GameListener.class);
    @Override
    public void onUserUpdateGame(UserUpdateGameEvent e) {
        User user = e.getUser();
        Guild guild = e.getGuild();

        if (e.getOldGame() == null || user.isBot()){
            return;
        }

        String gameName = e.getOldGame().getName();
        GameStats gameStats = AliceBootstrap.alice.getGameStatsManager().getGameStats(user, guild, gameName);

        if (e.getNewGame() != null && e.getNewGame().getName().equalsIgnoreCase(e.getOldGame().getName())){
            return;
        }

        try {
            long elapsed = e.getOldGame().getTimestamps().getElapsedTime(ChronoUnit.SECONDS);
            if (elapsed > 0) {
                gameStats.addTimePlayed((int) elapsed);
                gameStats.save();
            }
        } catch (NullPointerException exc){
            logger.debug("Closed game does not contain ElapsedTime value, omitting..");
        }
    }
}
