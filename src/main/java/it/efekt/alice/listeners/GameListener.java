package it.efekt.alice.listeners;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.db.GameStats;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;
//todo save only alfanumeric strings!
public class GameListener extends ListenerAdapter {
    private Logger logger = LoggerFactory.getLogger(GameListener.class);
    @Override
    public void onUserUpdateGame(UserUpdateGameEvent e) {
        try {
            User user = e.getUser();
            Guild guild = e.getGuild();

            if (e.getOldGame() == null || user.isBot()) {
                return;
            }

            if (e.getMember().getOnlineStatus().equals(OnlineStatus.OFFLINE) || e.getMember().getOnlineStatus().equals(OnlineStatus.INVISIBLE)){
                return;
            }

            String gameName = e.getOldGame().getName();
            GameStats gameStats = AliceBootstrap.alice.getGameStatsManager().getGameStats(user, guild, gameName);

            if (e.getNewGame() != null && e.getNewGame().getName().equalsIgnoreCase(e.getOldGame().getName())) {
                return;
            }

            if (e.getOldGame().getTimestamps() == null) {
                return;
            }

            long elapsed = e.getOldGame().getTimestamps().getElapsedTime(ChronoUnit.MINUTES);
            if (elapsed >= 1 && gameName.length() <= 128) {
                gameStats.addTimePlayed(elapsed);
                gameStats.save();
                logger.debug("user: " + user.getId() + " server: " + guild.getId() + " game: " + gameName + " addedTime: " + elapsed + "min");
                AliceBootstrap.alice.getGuildLogger().log(e.getGuild(), Message.LOGGER_USER_STOPPED_PLAYING.get(e.getGuild(), user.getName(), gameName,String.valueOf(elapsed)));
            }
        } catch (Exception exc){
            exc.printStackTrace();
        }
    }
}
