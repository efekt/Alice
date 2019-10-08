package it.efekt.alice.listeners;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

//todo save only alfanumeric strings!
public class GameListener extends ListenerAdapter {
    private Logger logger = LoggerFactory.getLogger(GameListener.class);
    private HashMap<String, Long> lastUpdate = new HashMap<>();

    @Override
    public void onUserUpdateGame(UserUpdateGameEvent e) {
        long beforeTime = System.currentTimeMillis();
        try {
            User user = e.getUser();
            Guild guild = e.getGuild();

            if (e.getOldGame() == null || user.isBot()) {
                return;
            }

            if (!e.getMember().getOnlineStatus().equals(OnlineStatus.ONLINE)){
                return;
            }

            if (e.getNewGame() != null && e.getNewGame().getName().equalsIgnoreCase(e.getOldGame().getName())) {
                return;
            }

            if (e.getOldGame().getTimestamps() == null) {
                return;
            }

            String gameName = e.getOldGame().getName();

            long elapsed = e.getOldGame().getTimestamps().getElapsedTime(ChronoUnit.MINUTES);
            long elapsedMilis = e.getOldGame().getTimestamps().getElapsedTime(ChronoUnit.MILLIS);
//            long sinceStartupTime = System.currentTimeMillis() - AliceBootstrap.STARTUP_TIME;

            if (elapsed >= 1) {
//                if (elapsedMilis > sinceStartupTime){
//                    return;
//                }

                if (lastUpdate.containsKey(guild.getId().concat(user.getId()))){
                    long sinceLastUpdate = System.currentTimeMillis() - lastUpdate.get(guild.getId().concat(user.getId()));
                    logger.debug("sinceLastUpdate: " + sinceLastUpdate);
                    logger.debug("elapsedMillis: " + elapsedMilis);
                    if (elapsedMilis > sinceLastUpdate){
                        logger.debug("not saved");
                        return;
                    }
                }

                Runnable runnable = () -> {
//                    GameStats gameStats = AliceBootstrap.alice.getGameStatsManager().getGameStats(user, guild, gameName);
//                    gameStats.addTimePlayed(elapsed);
//                    gameStats.save();
                    AliceBootstrap.alice.getGameStatsManager().addTimePlayed(user, guild, gameName, elapsed);
                    lastUpdate.put(guild.getId().concat(user.getId()), System.currentTimeMillis());
                    logger.debug("saved");
                    logger.debug("user: " + user.getId() + " nick: " + user.getName() + " server: " + guild.getId() + " game: " + gameName + " addedTime: " + elapsed + "min" + " took: " + (System.currentTimeMillis() - beforeTime) + "ms");
                    AliceBootstrap.alice.getGuildLogger().log(e.getGuild(), Message.LOGGER_USER_STOPPED_PLAYING.get(e.getGuild(), user.getName(), gameName, String.valueOf(elapsed)));
                };
                new Thread(runnable).start();

            }
        } catch (Exception exc){
            exc.printStackTrace();
        }
    }
}
