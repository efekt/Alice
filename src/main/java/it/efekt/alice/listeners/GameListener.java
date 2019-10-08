package it.efekt.alice.listeners;

import it.efekt.alice.core.AliceBootstrap;
import it.efekt.alice.lang.Message;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivityOrderEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

//todo save only alfanumeric strings!
public class GameListener extends ListenerAdapter {
    private Logger logger = LoggerFactory.getLogger(GameListener.class);
    private HashMap<String, Long> lastUpdate = new HashMap<>();

    @Override
    public void onUserUpdateActivityOrder(UserUpdateActivityOrderEvent e) {
        long beforeTime = System.currentTimeMillis();
        try {
            User user = e.getUser();
            Guild guild = e.getGuild();

            Activity oldGame;
            Activity newGame;

            try {
                oldGame = e.getOldValue().get(0);
                newGame = e.getNewValue().get(0);

            } catch (NullPointerException exc){
                return;
            }

            if (oldGame == null || user.isBot()) {
                return;
            }

            if (!e.getMember().getOnlineStatus().equals(OnlineStatus.ONLINE)){
                return;
            }

            if (newGame != null && newGame.getName().equalsIgnoreCase(oldGame.getName())) {
                return;
            }

            if (oldGame.getTimestamps() == null) {
                return;
            }

            String gameName = oldGame.getName();

            long elapsed = oldGame.getTimestamps().getElapsedTime(ChronoUnit.MINUTES);
            long elapsedMilis = oldGame.getTimestamps().getElapsedTime(ChronoUnit.MILLIS);
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
